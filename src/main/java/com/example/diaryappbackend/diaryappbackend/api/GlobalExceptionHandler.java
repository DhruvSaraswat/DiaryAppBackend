package com.example.diaryappbackend.diaryappbackend.api;

import com.example.diaryappbackend.diaryappbackend.model.error.DataDeletionException;
import com.example.diaryappbackend.diaryappbackend.model.error.DataPersistenceException;
import com.example.diaryappbackend.diaryappbackend.model.error.ErrorResponse;
import com.example.diaryappbackend.diaryappbackend.model.error.NoRecordFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoRecordFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNoRecordFoundException(NoRecordFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("No Record Found");
        return errorResponse;
    }

    @ExceptionHandler(DataPersistenceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleDataPersistenceException(DataPersistenceException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Internal Server Error");
        return errorResponse;
    }

    @ExceptionHandler(DataDeletionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleDataDeletionException(DataDeletionException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Internal Server Error");
        return errorResponse;
    }

}
