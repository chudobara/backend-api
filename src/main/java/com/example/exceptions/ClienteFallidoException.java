package com.example.exceptions;

public class ClienteFallidoException extends BaseException {
    
    private static final long serialVersionUID = 1L;
    
    public ClienteFallidoException(String message) {
        super(message);
    }
    
    public ClienteFallidoException(String message, Throwable cause) {
        super(message, cause);
    }
}