package com.boarelli.playground.resources;

import com.boarelli.playground.model.errors.ErrorMessage;
import com.boarelli.playground.model.errors.UrlAlreadyExistsException;
import com.boarelli.playground.model.errors.UrlNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UrlNotFoundException.class)
    @ResponseBody
    ErrorMessage
    handleNotFoundRequest(HttpServletRequest req, Exception ex) {
        return new ErrorMessage("url not found");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UrlAlreadyExistsException.class)
    @ResponseBody
    ErrorMessage
    handleConflictRequest(HttpServletRequest req, Exception ex) {
        return new ErrorMessage("url already has compressed version");
    }

}
