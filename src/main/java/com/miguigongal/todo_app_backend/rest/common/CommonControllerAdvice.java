package com.miguigongal.todo_app_backend.rest.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.miguigongal.todo_app_backend.model.exceptions.DuplicateInstanceException;
import com.miguigongal.todo_app_backend.model.exceptions.InstanceNotFoundException;
import com.miguigongal.todo_app_backend.model.exceptions.PermissionException;

@RestControllerAdvice
public class CommonControllerAdvice {
    
    @ExceptionHandler(DuplicateInstanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDto handleDuplicateInstanceException(DuplicateInstanceException ex) {
        return new ErrorsDto("DUPLICATE_INSTANCE", ex.getMessage());
    }

    @ExceptionHandler(InstanceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsDto handleInstanceNotFoundException(InstanceNotFoundException ex) {
        return new ErrorsDto("INSTANCE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(PermissionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDto handlePermissionException(PermissionException ex) {
        return new ErrorsDto("PERMISSION_DENIED", ex.getMessage());
    }
}
