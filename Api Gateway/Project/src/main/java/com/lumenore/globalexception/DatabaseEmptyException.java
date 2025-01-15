package com.lumenore.globalexception;

public class DatabaseEmptyException extends RuntimeException{
    public DatabaseEmptyException(String message) {
        super(message);
    }

}
