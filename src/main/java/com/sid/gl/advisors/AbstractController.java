package com.sid.gl.advisors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AbstractController {
    public static final String SUCCESS="SUCCESS";

    public UserDetails getUserPrincipal () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        return userDetail;

    }

    public <T> ResponseEntity<ApiResponse> getResponseEntity(T response){
        ApiResponse<T> responseDTO = new ApiResponse<>();
        responseDTO.setStatus(SUCCESS);
        responseDTO.setResults(response);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
