package com.ssafy.soltravel.service.exchange;

import com.ssafy.soltravel.common.Header;
import com.ssafy.soltravel.domain.ExchangeRate;
import com.ssafy.soltravel.domain.redis.PreferenceRate;
import com.ssafy.soltravel.dto.exchange.AccountInfoDto;
import com.ssafy.soltravel.dto.exchange.ExchangeCurrencyDto;
import com.ssafy.soltravel.dto.exchange.ExchangeRateDto;
import com.ssafy.soltravel.dto.exchange.ExchangeRateRegisterRequestDto;
import com.ssafy.soltravel.dto.exchange.ExchangeRateResponseDto;
import com.ssafy.soltravel.dto.exchange.ExchangeRequestDto;
import com.ssafy.soltravel.dto.exchange.ExchangeResponseDto;
import com.ssafy.soltravel.repository.ExchangeRateRepository;
import com.ssafy.soltravel.repository.redis.PreferenceRateRepository;
import com.ssafy.soltravel.service.NotificationService;
import com.ssafy.soltravel.service.account.AccountService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ExchangeService {

  private final Map<String, String> apiKeys;

  private final WebClient webClient;
  private final ModelMapper modelMapper;
  private final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1/edu";

  private final ExchangeRateRepository exchangeRateRepository;
  private final PreferenceRateRepository preferenceRateRepository;
  private final NotificationService notificationService;
  private final AccountService accountService;

  /**
   * 실시간 환율 받아오는 메서드 매시 0분, 10분, 20분, 30분, 40분, 50분에 data 가져온다
   */
  @Scheduled(cron = "0 0/10 * * * *")
  public void ScheduledGetExchangeRate() {

    String API_NAME = "exchangeRate";
    String API_URL = BASE_URL + "/" + API_NAME;

    Header header = Header.builder()
        .apiName(API_NAME)
        .apiServiceCode(API_NAME)
        .apiKey(apiKeys.get("API_KEY")).build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);

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

      ExchangeRate rate = exchangeRateRepository.findByCurrency(dto.getCurrency());
      double prevRate = -1D;

      if (rate != null) {
        //이전 환율 저장
        prevRate = rate.getExchangeRate();
      } else {
        rate = ExchangeRate.builder()
            .currency(dto.getCurrency())
            .build();
      }

      double updatedRate = getDoubleExchangeRate(dto.getExchangeRate());

      //변동된 환율을 DB에 저장
      rate = rate.toBuilder()
          .exchangeRate(updatedRate)
          .exchangeMin(Double.parseDouble(dto.getExchangeMin()))
          .created(getLocalDateTime(dto.getCreated()))
          .build();
      exchangeRateRepository.save(rate);

      /**
       * 환율이 변동되었다 -> 자동 환전
       */
      if (prevRate != updatedRate) {
        // ID에 등록된 account를 가져온다
        String id = makeId(dto.getCurrency(), updatedRate);
        Optional<PreferenceRate> exchangeOpt = preferenceRateRepository.findById(id);

        if (exchangeOpt.isPresent()) {
          PreferenceRate preferenceRate = exchangeOpt.get();
          log.info("{}의 환율이 달라졌어요", dto.getCurrency());

          for (long accountId : preferenceRate.getAccounts()) {
            log.info("{} 통장의 환전을 시작합니다.", accountId);

            //환전 실행
            ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();
            exchangeRequestDto.setExchangeCurrency(dto.getCurrency());
            //TODO: preferenceRateRepository id를 통해 계좌번호 얻어올 것.
            exchangeRequestDto.setAccountNo("0883473075115544");


            //TODO: 얼마를 환전할 것인지 설정할 것.
            exchangeRequestDto.setExchangeAmount("3000");
            executeExchange(exchangeRequestDto);
          }
        }
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
      preference = new PreferenceRate(id, new HashSet<>());
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

    Header header = Header.builder()
        .apiName(API_NAME)
        .apiServiceCode(API_NAME)
        .apiKey(apiKeys.get("API_KEY"))
        .userKey(apiKeys.get("USER_KEY"))
        .build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);
    body.put("accountNo", dto.getAccountNo());
    body.put("exchangeCurrency", dto.getExchangeCurrency());
//    body.put("exchangeAmount", String.valueOf(dto.getExchangeAmount()));
    body.put("exchangeAmount", "3000");

    ResponseEntity<Map<String, Object>> response = webClient.post()
        .uri(API_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body).retrieve()
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
        }).block();

    // REC 부분을 Object 타입으로 받기 -> List<Map<String, Object>>로 변환
    Object recObject = response.getBody().get("REC");
    Map<String, Object> recList = (Map<String, Object>) recObject;

    ModelMapper modelMapper = new ModelMapper();

// recObject를 Map<String, Object>로 변환
    Map<String, Object> recMap = (Map<String, Object>) recObject;

// exchangeCurrency와 accountInfo를 각각 DTO로 변환
    ExchangeCurrencyDto exchangeCurrencyDto = modelMapper.map(recMap.get("exchangeCurrency"),
        ExchangeCurrencyDto.class);
    AccountInfoDto accountInfoDto = modelMapper.map(recMap.get("accountInfo"),
        AccountInfoDto.class);

// DTO를 ExchangeResponseDto에 설정
    ExchangeResponseDto responseDto = new ExchangeResponseDto();
    responseDto.setExchangeCurrencyDto(exchangeCurrencyDto);
    accountInfoDto.setAccountId(dto.getAccountId());
    responseDto.setAccountInfoDto(accountInfoDto);
    responseDto.setExecuted_at(RFC1123toLocalDateTime(response.getHeaders().getFirst(HttpHeaders.DATE)));

    //TODO: 환전 log 저장


    //TODO: 환전 알림 구현, accountId 넘길것.
    notificationService.notifyMessage(responseDto);

    return responseDto;
  }


  /**
   * 아래부터는 형 변환 메서드 모음
   */
  public LocalDateTime getLocalDateTime(String str) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.parse(str, formatter);
  }

  public double getDoubleExchangeRate(String exchangeRate) {

    String exchangeRateStr = exchangeRate.replace(",", "");
    return Double.parseDouble(exchangeRateStr);
  }

  public String makeId(String currency, double rate) {
    return String.format("%s(%.2f)", currency, rate);
  }
  public LocalDateTime RFC1123toLocalDateTime(String str) throws DateTimeParseException {
    // RFC 1123 형식의 Date 문자열을 파싱
    DateTimeFormatter rfc1123Formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(str, rfc1123Formatter);

    // 한국 시간대 (Asia/Seoul)로 변환
    ZonedDateTime seoulZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));

    // ZonedDateTime을 LocalDateTime으로 변환하여 반환
    return seoulZonedDateTime.toLocalDateTime();
  }
}
