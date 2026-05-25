package com.miguigongal.todo_app_backend.rest.common;

public interface JwtGenerator {

    String generate(JwtInfo info);

    JwtInfo getInfo(String token);  

}
