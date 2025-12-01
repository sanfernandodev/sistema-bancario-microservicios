package com.banksystem.cliente.exception;

public class ClienteAlreadyExistsException extends RuntimeException {
    public ClienteAlreadyExistsException(String message) {
        super(message);
    }

    public ClienteAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
