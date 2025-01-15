package com.lumenore2.exceptionhandler;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException (ChangeSetPersister.NotFoundException notFoundException) {
        return new ResponseEntity<String>("Rating not found.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSQLException(SQLException sqlException){
        return new ResponseEntity<>("Email already exist.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException mismatchException) {
        return new ResponseEntity<>("Enter valid id.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<String> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException invalidDataAccessApiUsageException) {
        return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException argumentException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(argumentException.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException nullPointerException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nullPointerException.getMessage());
    }

    @ExceptionHandler(DatabaseEmptyException.class)
    public ResponseEntity<String> handleDatabaseEmptyException(DatabaseEmptyException databaseEmptyException) {
        String message = databaseEmptyException.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
