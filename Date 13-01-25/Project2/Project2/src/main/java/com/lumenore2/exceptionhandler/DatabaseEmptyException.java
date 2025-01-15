package com.lumenore2.exceptionhandler;

public class DatabaseEmptyException extends RuntimeException{
    public DatabaseEmptyException(String message) {
        super(message);
    }

}
