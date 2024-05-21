package com.sid.gl.advisors;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String status;
    private List<ErrorDTO> errorDTOS;
    private T results;

    public ApiResponse() {
    }

    public ApiResponse(String status, List<ErrorDTO> errorDTOS, T results) {
        this.status = status;
        this.errorDTOS = errorDTOS;
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ErrorDTO> getErrorDTOS() {
        return errorDTOS;
    }

    public void setErrorDTOS(List<ErrorDTO> errorDTOS) {
        this.errorDTOS = errorDTOS;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
