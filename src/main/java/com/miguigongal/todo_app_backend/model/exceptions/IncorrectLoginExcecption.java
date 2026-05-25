package com.miguigongal.todo_app_backend.model.exceptions;

@SuppressWarnings("serial")
public class IncorrectLoginExcecption extends Exception{
    private String userName;
    private String password;
    public IncorrectLoginExcecption (String userName, String password)
    {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
    
}
