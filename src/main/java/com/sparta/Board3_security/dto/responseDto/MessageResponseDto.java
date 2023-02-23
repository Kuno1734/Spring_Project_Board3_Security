package com.sparta.Board3_security.dto.responseDto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MessageResponseDto {

    private String message;
    private HttpStatus statusCode;


    @Builder
    public MessageResponseDto(String message, HttpStatus statusCode) {
        this.message =  message;
        this.statusCode = statusCode;
    }
}
