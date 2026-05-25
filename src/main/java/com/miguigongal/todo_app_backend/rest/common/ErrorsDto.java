package com.miguigongal.todo_app_backend.rest.common;

public class ErrorsDto {

    private String errorCode;
    private String message;

    public ErrorsDto(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getErrorCode() {
        return errorCode;
    }


    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    

}
