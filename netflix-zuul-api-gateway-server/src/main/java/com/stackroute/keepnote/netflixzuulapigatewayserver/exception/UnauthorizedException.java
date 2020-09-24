package com.stackroute.keepnote.netflixzuulapigatewayserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.ServletException;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Missing or invalid Authorization headers")
public class UnauthorizedException extends ServletException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
