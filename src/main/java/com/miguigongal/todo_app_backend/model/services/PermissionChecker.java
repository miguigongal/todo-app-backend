package com.miguigongal.todo_app_backend.model.services;

import com.miguigongal.todo_app_backend.model.domain.User;
import com.miguigongal.todo_app_backend.model.exceptions.InstanceNotFoundException;

public interface PermissionChecker {
    
    void checkUserExists(Long userId) throws InstanceNotFoundException;

    User checkUser (Long userId) throws InstanceNotFoundException;

}