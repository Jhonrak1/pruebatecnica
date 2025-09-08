package com.empresa.demo.domain.exception;

public class ServerErrorException extends RuntimeException {
    public ServerErrorException(String msg) {
        super(msg);
    }
}
