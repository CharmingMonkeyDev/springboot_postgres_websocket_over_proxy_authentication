package com.sunshineoxygen.inhome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class WrongOtpException extends RuntimeException{

    public WrongOtpException(String message) {
        super(message);
    }

}