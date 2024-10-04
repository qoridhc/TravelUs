package com.ssafy.soltravel.v2.config;

import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateCacheDto;
import com.ssafy.soltravel.v2.service.exchange.ExchangeService;
import com.ssafy.soltravel.v2.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;

  @Bean
  public Job exchangeRateJob(Step exchangeRateStep) {
    return new JobBuilder("exchangeRateJob", jobRepository)
        .start(exchangeRateStep)
        .build();
  }

  @Bean
  public Step exchangeRateStep(Tasklet exchangeRateTasklet) {
    return new StepBuilder("exchangeRateStep", jobRepository)
        .tasklet(exchangeRateTasklet, transactionManager)
        .build();
  }

  @Bean
  public Tasklet exchangeRateTasklet(ExchangeService exchangeService) {
    return (contribution, chunkContext) -> {
      //RabbitMQ를 통해 받은 message
      String message = (String) chunkContext.getStepContext().getJobParameters().get("message");

      // 메시지 파싱
      String[] parts = message.split(", ");
      String currencyCode = parts[0].split(": ")[1];
      Double exchangeRate = Double.parseDouble(parts[1].split(": ")[1]);
      String timeLastUpdateUtc = parts[2].split(": ")[1];
      int exchangeMin = Integer.parseInt(parts[3].split(": ")[1]);

      // 이전 환율 가져오기
      ExchangeRateCacheDto cachedDto = exchangeService.getExchangeRateFromCache(currencyCode);

      if (cachedDto == null || !exchangeRate.equals(cachedDto.getExchangeRate())) {
        exchangeService.updateExchangeRateCache(
            new ExchangeRateCacheDto(currencyCode, exchangeRate, timeLastUpdateUtc, String.valueOf(exchangeMin))
        );

        exchangeService.processCurrencyConversions(currencyCode, exchangeRate);
      } else {

        LogUtil.info(String.format("환율 변동 없음. 통화 코드: %s, 기존 환율: %s, 새로운 환율: %s",
            currencyCode, cachedDto.getExchangeRate(), exchangeRate));
      }
      return RepeatStatus.FINISHED;
    };
  }

}
