package com.ssafy.soltravel.handler;

import com.ssafy.soltravel.dto.ResponseDto;
import com.ssafy.soltravel.util.LogUtil;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleGeneralException(Exception e) {

        ResponseDto errorResponseDto = new ResponseDto();
        errorResponseDto.setStatus("INTERNAL_SERVER_ERROR");
        errorResponseDto.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException e) {
        return new ResponseEntity<String >(e.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
    }

}
