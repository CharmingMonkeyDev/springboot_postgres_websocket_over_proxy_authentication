package com.sunshineoxygen.inhome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenNotFoundException extends RuntimeException{

    private int errorcode;

    public TokenNotFoundException(int errorcode, String message) {
        super(message);
        this.errorcode = errorcode;
    }
}