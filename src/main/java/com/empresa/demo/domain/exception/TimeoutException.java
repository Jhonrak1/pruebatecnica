package com.empresa.demo.domain.exception;

public class TimeoutException extends RuntimeException {
    public TimeoutException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
