//package com.example.social_media.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//
//@RestControllerAdvice
//public class ApiExceptionHandler {
//    // IndexOutOfBoundsException được xử lí tại đây
//    @ExceptionHandler(IndexOutOfBoundsException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handleIndexOutOfBoundsException(Exception ex, WebRequest request) {
//        return new ErrorResponse(HttpStatus.NOT_FOUND, "Đối tượng không tồn tại");
//    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handleAllException(Exception ex, WebRequest request) {
//        // quá trình kiểm soat lỗi diễn ra ở đây
//        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi nội bộ. Vui lòng thử lại sau.");
//    }
//}
