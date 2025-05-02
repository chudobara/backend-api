package com.example.exceptions;

public class TicketException extends BaseException {
    
    private static final long serialVersionUID = 1L;
    
    public TicketException(String message) {
        super(message);
    }
    
    public TicketException(String message, Throwable cause) {
        super(message, cause);
    }
}