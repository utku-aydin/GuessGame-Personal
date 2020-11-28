/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mthree.guessgamerest.controllers;

import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author utkua
 */
@ControllerAdvice
@RestController
public class GuessGameControllerExceptionHandler {
    
    private static final String SQL_EXCEPTION = "Could not process your guess. "
            + "Please ensure it is valid and try again.";
    
    private static final String EMPTY_RESULT_EXCEPTION = "The result of your query is empty. "
            + "Please try another id.";
    
    private static final String REQUEST_UNSUPPORTED = "That URL does not support your request. "
            + "Please ensure the request is valid for the input URL and try again.";
    
    private static final String CANNOT_GET_CONNECTION = "Could not connect to database. "
            + "Ensure you have started your apache/mysql if you are connecting to localhost.";
    
    private static final String OUT_OF_BOUNDS = "A String index is out of bounds. "
            + "Ensure the 'guess' attribute value length is at most 4.";

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public final ResponseEntity<Error> handleSqlException(
            SQLIntegrityConstraintViolationException ex,
            WebRequest request) {

        Error err = new Error();
        err.setMessage(SQL_EXCEPTION);
        return new ResponseEntity<>(err, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public final ResponseEntity<Error> handleEmptyResultException(
            EmptyResultDataAccessException ex,
            WebRequest request) {

        Error err = new Error();
        err.setMessage(EMPTY_RESULT_EXCEPTION);
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<Error> handleUnsupportedRequestException(
            HttpRequestMethodNotSupportedException ex,
            WebRequest request) {

        Error err = new Error();
        err.setMessage(REQUEST_UNSUPPORTED);
        return new ResponseEntity<>(err, HttpStatus.METHOD_NOT_ALLOWED);
    }
    
    @ExceptionHandler(CannotGetJdbcConnectionException.class)
    public final ResponseEntity<Error> handleJdbcConnectionException(
            CannotGetJdbcConnectionException ex,
            WebRequest request) {

        Error err = new Error();
        err.setMessage(CANNOT_GET_CONNECTION);
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(StringIndexOutOfBoundsException.class)
    public final ResponseEntity<Error> handleStringIndexOutOfBoundsException(
            StringIndexOutOfBoundsException ex,
            WebRequest request) {

        Error err = new Error();
        err.setMessage(OUT_OF_BOUNDS);
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
