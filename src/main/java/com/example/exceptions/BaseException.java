package com.example.exceptions;

public class BaseException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public BaseException(String message) {
        super(message);
    }
    
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}