package com.sid.gl.advisors;

import com.sid.gl.exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class SpotifyAdvisor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMethodArgumentException(MethodArgumentNotValidException exception){
        ApiResponse<?> apiResponse = new ApiResponse<>();
        List<ErrorDTO> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    ErrorDTO errorDTO = new ErrorDTO(fieldError.getField(), fieldError.getDefaultMessage());
                    errors.add(errorDTO);
                });

        apiResponse.setStatus("FAILED");
        apiResponse.setErrorDTOS(errors);
        return apiResponse;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleServiceBussnessValidatorException(BadRequestException exception){
        ApiResponse<?> response = new ApiResponse<>();
        response.setErrorDTOS(Collections.singletonList(new ErrorDTO("", exception.getMessage())));
        response.setStatus("FAILED");
        return response;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleServiceBadCredentials(BadCredentialsException exception){
        ApiResponse<?> response = new ApiResponse<>();
        response.setErrorDTOS(Collections.singletonList(new ErrorDTO("", exception.getMessage())));
        response.setStatus("FAILED");
        return response;
    }



//    @ExceptionHandler(OTPException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ApiResponse<?> handleServiceOTPException(OTPException exception){
//        ApiResponse<?> response = new ApiResponse<>();
//        response.setErrorDTOS(Collections.singletonList(new ErrorDTO("", exception.getMessage())));
//        response.setStatus("FAILED");
//        return response;
//    }


    /*@ExceptionHandler(SNSharedNotFoundException.class)
    public ApiResponse<?> handleServiceException(SNSharedNotFoundException exception){
        ApiResponse<?> response = new ApiResponse<>();
        response.setErrorDTOS(Collections.singletonList(new ErrorDTO("", exception.getMessage())));
        response.setStatus("FAILED");
        return response;
    }*/
}
