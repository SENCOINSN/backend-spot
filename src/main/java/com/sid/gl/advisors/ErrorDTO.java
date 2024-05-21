package com.sid.gl.advisors;

public class ErrorDTO {
    private String field;
    private String errorMessage;

    public ErrorDTO(String field, String errorMessage) {
        this.field = field;
        this.errorMessage = errorMessage;
    }

    public ErrorDTO() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
