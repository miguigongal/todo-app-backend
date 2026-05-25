package com.miguigongal.todo_app_backend.model.services;

import com.miguigongal.todo_app_backend.model.domain.User;
import com.miguigongal.todo_app_backend.model.exceptions.DuplicateInstanceException;
import com.miguigongal.todo_app_backend.model.exceptions.IncorrectLoginExcecption;
import com.miguigongal.todo_app_backend.model.exceptions.IncorrectPasswordException;
import com.miguigongal.todo_app_backend.model.exceptions.InstanceNotFoundException;

public interface UserService {
    
    void signUp(User user) throws DuplicateInstanceException;

    User login(String userName, String password) throws IncorrectLoginExcecption;

    User loginFromId(Long id) throws InstanceNotFoundException;

    User updateProfile(Long id, String firstName, String lastName, String email) throws InstanceNotFoundException;

    void changePassword(Long id, String oldPassword , String newPassword) throws InstanceNotFoundException, IncorrectPasswordException;

}
