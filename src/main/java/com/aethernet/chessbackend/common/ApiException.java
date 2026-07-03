package com.aethernet.chessbackend.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private HttpStatus status = HttpStatus.BAD_REQUEST;

    protected ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    protected ApiException(String message) {
        super(message);
    }

}
