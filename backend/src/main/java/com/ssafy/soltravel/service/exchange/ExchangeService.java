package com.ssafy.soltravel.service.exchange;

import com.ssafy.soltravel.common.Header;
import com.ssafy.soltravel.domain.ExchangeRate;
import com.ssafy.soltravel.dto.exchange.ExchangeRateDto;
import com.ssafy.soltravel.dto.exchange.ExchangeRateResponseDto;
import com.ssafy.soltravel.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeService {

    private final Map<String, String> apiKeys;

    private final WebClient webClient;
    private final ModelMapper modelMapper;
    private final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1/edu";

    private final ExchangeRateRepository exchangeRateRepository;

    //매시 2분, 12분, 22분, 32분, 42분, 52분에 data 가져온다
    @Scheduled(cron = "0 2/10 * * * *")
    public void ScheduledGetExchangeRate() {

        String API_NAME = "exchangeRate";
        String API_URL = BASE_URL + "/" + API_NAME;

        Header header = Header.builder()
                .apiName(API_NAME)
                .institutionCode("00100")
                .fintechAppNo("001")
                .apiServiceCode(API_NAME)
                .apiKey(apiKeys.get("API_KEY"))
                .build();

        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);

        try {
            ResponseEntity<Map<String, Object>> response = webClient.post()
                    .uri(API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .block();

            // REC 부분을 Object 타입으로 받기 -> List<Map<String, Object>>로 변환
            Object recObject = response.getBody().get("REC");
            List<Map<String, Object>> recList = (List<Map<String, Object>>) recObject;

            // ModelMapper를 사용하여 각 Map을 ExchangeRateDto로 변환
            List<ExchangeRateDto> responseDtoList = recList.stream()
                    .map(map -> modelMapper.map(map, ExchangeRateDto.class))
                    .collect(Collectors.toList());
            
            //DB 업데이트
            updateExchangeRates(responseDtoList);

        } catch (WebClientResponseException e) {
            throw e;
        }
    }

    public ExchangeRateResponseDto getExchangeRate(String currency){

        ExchangeRateResponseDto responseDto = new ExchangeRateResponseDto();
        ExchangeRate rateEntity=exchangeRateRepository.findByCurrency(currency);

        responseDto.setCurrency(currency);
        responseDto.setExchangeRate(rateEntity.getExchangeRate());
        responseDto.setExchangeMin(rateEntity.getExchangeMin());
        return responseDto;
    }


    public void updateExchangeRates(List<ExchangeRateDto> dtoList) {
        for (ExchangeRateDto dto : dtoList) {
            ExchangeRate existingRate = exchangeRateRepository.findByCurrency(dto.getCurrency());
            
            if (existingRate != null) {
                // 데이터가 이미 존재하면 업데이트
                existingRate.setExchangeRate(dto.getExchangeRate());
                existingRate.setExchangeMin(dto.getExchangeMin());
                existingRate.setCreated(dto.getCreated());
                exchangeRateRepository.save(existingRate);
            } else {
                // 데이터가 없으면 새로 삽입
                ExchangeRate newRate = modelMapper.map(dto, ExchangeRate.class);
                exchangeRateRepository.save(newRate);
            }
        }
    }
}
