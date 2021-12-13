package com.letscode.starwarsresistence.gateways.http;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.letscode.starwarsresistence.domain.exceptions.ApplicationBusinessException;
import com.letscode.starwarsresistence.domain.exceptions.ErrorMessage;
import com.letscode.starwarsresistence.domain.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NotFoundException.class)
    public ErrorMessage handleNotFoundException(NotFoundException notFoundException) {
        ErrorMessage errorMessage = new ErrorMessage(notFoundException.getCause().getLocalizedMessage(), new Date());
        return errorMessage;
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(value = ApplicationBusinessException.class)
    public ErrorMessage handleApplicationBusinessErrors(ApplicationBusinessException appBusinessException) {
        ErrorMessage errorMessage = new ErrorMessage(appBusinessException.getCause().getLocalizedMessage(), new Date());
        return errorMessage;
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(value = InvalidFormatException.class)
    public ErrorMessage handleJsonParseError(InvalidFormatException invalidFormatException) {
        ErrorMessage errorMessage = new ErrorMessage("You need to send a valid UUID type", new Date());
        return errorMessage;
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ErrorMessage handleUnexpectedError(Exception exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), new Date());
        return errorMessage;
    }
}
