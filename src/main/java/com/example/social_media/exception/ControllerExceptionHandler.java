package com.example.social_media.exception;

import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
@ControllerAdvice
public class ControllerExceptionHandler {
    // IndexOutOfBoundsException được xử lí tại đây
//    @ExceptionHandler(IndexOutOfBoundsException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String handleIndexOutOfBoundsException(Exception ex, WebRequest request) {
//        return "web/pages-error";
//    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public String handleAllException(Exception ex, WebRequest request) {
//        // quá trình kiểm soat lỗi diễn ra ở đây
//        return "web/pages-error";
//    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "web/pages-error";
    }
    // TTP Status 400 – Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "web/pages-error";
    }



}
