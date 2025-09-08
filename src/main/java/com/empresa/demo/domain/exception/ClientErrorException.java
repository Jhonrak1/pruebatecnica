package com.empresa.demo.domain.exception;

public class ClientErrorException extends RuntimeException {
    public ClientErrorException(String msg) {
        super(msg);
    }
}
