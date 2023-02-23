package com.sparta.Board3_security.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final Exception exception; //Excepton Enum 생성자 주입

    public CustomException(Exception exception) {
        this.exception = exception;
    }
}
