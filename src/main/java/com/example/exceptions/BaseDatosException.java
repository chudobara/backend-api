package com.example.exceptions;

public class BaseDatosException extends BaseException {
    
    private static final long serialVersionUID = 1L;
    
    public BaseDatosException(String message) {
        super(message);
    }
    
    public BaseDatosException(String message, Throwable cause) {
        super(message, cause);
    }
}