package com.ssafy.soltravel.service.exchange;

import com.ssafy.soltravel.common.Header;
import com.ssafy.soltravel.domain.ExchangeRate;
import com.ssafy.soltravel.domain.redis.PreferenceRate;
import com.ssafy.soltravel.dto.exchange.ExchangeRateDto;
import com.ssafy.soltravel.dto.exchange.ExchangeRateRegisterRequestDto;
import com.ssafy.soltravel.dto.exchange.ExchangeRateResponseDto;
import com.ssafy.soltravel.dto.exchange.ExchangeRequestDto;
import com.ssafy.soltravel.dto.exchange.ExchangeResponseDto;
import com.ssafy.soltravel.repository.ExchangeRateRepository;
import com.ssafy.soltravel.repository.redis.PreferenceRateRepository;
import com.ssafy.soltravel.service.NotificationService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeService {

  private final Map<String, String> apiKeys;

  private final WebClient webClient;
  private final ModelMapper modelMapper;
  private final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1/edu";

  private final ExchangeRateRepository exchangeRateRepository;
  private final PreferenceRateRepository preferenceRateRepository;
  private final NotificationService notificationService;

  /**
   * 실시간 환율 받아오는 메서드 매시 0분, 10분, 20분, 30분, 40분, 50분에 data 가져온다
   */
  @Scheduled(cron = "0 0/10 * * * *")
  public void ScheduledGetExchangeRate() {

    String API_NAME = "exchangeRate";
    String API_URL = BASE_URL + "/" + API_NAME;

    Header header = Header.builder().apiName(API_NAME).apiServiceCode(API_NAME)
        .apiKey(apiKeys.get("API_KEY")).build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);

    try {
      ResponseEntity<Map<String, Object>> response = webClient.post().uri(API_URL)
          .contentType(MediaType.APPLICATION_JSON).bodyValue(body).retrieve()
          .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
          }).block();

      // REC 부분을 Object 타입으로 받기 -> List<Map<String, Object>>로 변환
      Object recObject = response.getBody().get("REC");
      List<Map<String, Object>> recList = (List<Map<String, Object>>) recObject;

      // ModelMapper를 사용하여 각 Map을 ExchangeRateDto로 변환
      List<ExchangeRateDto> responseDtoList = recList.stream()
          .map(map -> modelMapper.map(map, ExchangeRateDto.class)).collect(Collectors.toList());

      //DB 업데이트
      updateExchangeRates(responseDtoList);

    } catch (WebClientResponseException e) {
      throw e;
    }
  }

  /**
   * 현재 환율 조회
   */
  public ExchangeRateResponseDto getExchangeRate(String currency) {

    ExchangeRateResponseDto responseDto = new ExchangeRateResponseDto();
    ExchangeRate rateEntity = exchangeRateRepository.findByCurrency(currency);

    responseDto.setCurrency(currency);
    responseDto.setExchangeRate(rateEntity.getExchangeRate());
    responseDto.setExchangeMin(rateEntity.getExchangeMin());
    return responseDto;
  }

  /**
   * 실시간 환율 데이터 db에 저장
   */
  public void updateExchangeRates(List<ExchangeRateDto> dtoList) {
    for (ExchangeRateDto dto : dtoList) {
      ExchangeRate existingRate = exchangeRateRepository.findByCurrency(dto.getCurrency());

      if (existingRate != null) {

        // 데이터가 이미 존재하면 업데이트
        float prevRate = existingRate.getExchangeRate();
        float updatedRate = getFloatExchangeRate(dto.getExchangeRate());

        existingRate.setExchangeRate(updatedRate);
        existingRate.setExchangeMin(Long.parseLong(dto.getExchangeMin()));
        existingRate.setCreated(getLocalDateTime(dto.getCreated()));
        exchangeRateRepository.save(existingRate);

        if (prevRate != updatedRate) {

          // ID에 등록된 account를 가져온다
          String id = makeId(dto.getCurrency(), updatedRate);
          Optional<PreferenceRate> exchangeOpt = preferenceRateRepository.findById(id);

          if (exchangeOpt.isPresent()) {
            PreferenceRate preferenceRate = exchangeOpt.get();
            log.info("{}의 환율이 달라졌어요", dto.getCurrency());

            for (long accountId : preferenceRate.getAccounts()) {
              log.info("{} 통장의 환전을 시작합니다.", accountId);

              //Notification test용
              notificationService.notifyMessage("2번한테보내");
//              executeExchange();
            }
          }
        }


      } else {
        // 데이터가 없으면 새로 삽입
        ExchangeRate newRate = modelMapper.map(dto, ExchangeRate.class);
        exchangeRateRepository.save(newRate);
      }
    }
  }

  /**
   * 환전 선호 금액 설정
   */
  public void setPreferenceRate(ExchangeRateRegisterRequestDto dto) {

    String id = makeId(dto.getCurrency(), dto.getExchangeRate());
    Optional<PreferenceRate> exchangeOpt = preferenceRateRepository.findById(id);

    PreferenceRate preference;
    if (exchangeOpt.isPresent()) {
      preference = exchangeOpt.get();
    } else {
      preference = new PreferenceRate(id, new ArrayList<>());
    }

    preference.getAccounts().add(dto.getGeneralAccountId());
    preferenceRateRepository.save(preference);
  }

  /**
   * 환전 api 호출
   */
  public ExchangeResponseDto executeExchange(ExchangeRequestDto dto) {

    String API_NAME = "updateDemandDepositAccountWithdrawal";
    String API_URL = BASE_URL + "/exchange";

    Header header = Header.builder().apiName(API_NAME).apiServiceCode(API_NAME)
        .apiKey(apiKeys.get("API_KEY"))
        .userKey(apiKeys.get("USER_KEY"))
        .build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);

    try {
      ResponseEntity<Map<String, Object>> response = webClient.post().uri(API_URL)
          .contentType(MediaType.APPLICATION_JSON).bodyValue(body).retrieve()
          .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
          }).block();

      // REC 부분을 Object 타입으로 받기 -> List<Map<String, Object>>로 변환
      Object recObject = response.getBody().get("REC");
      List<Map<String, Object>> recList = (List<Map<String, Object>>) recObject;

      // REC 부분을 Object 타입으로 받기
      ModelMapper modelMapper = new ModelMapper();
      ExchangeResponseDto responseDto = modelMapper.map(recObject, ExchangeResponseDto.class);

      //TODO: 환전 알림 구현
      notificationService.notifyMessage("account명들어가야함");

      return responseDto;
    } catch (WebClientResponseException e) {
      throw e;
    }
  }


  /**
   * 아래부터는 형 변환 메서드 모음
   */
  public LocalDateTime getLocalDateTime(String str) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.parse(str, formatter);
  }

  public float getFloatExchangeRate(String exchangeRate) {

    String exchangeRateStr = exchangeRate.replace(",", "");
    return Float.parseFloat(exchangeRateStr);
  }

  public String makeId(String currency, float rate) {
    return String.format("%s(%.2f)", currency, rate);
  }
}
