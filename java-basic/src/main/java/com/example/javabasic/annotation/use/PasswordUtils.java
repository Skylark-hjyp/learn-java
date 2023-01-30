package com.example.javabasic.annotation.use;

import com.example.javabasic.annotation.define.UseCase;

public class PasswordUtils {

    @UseCase(id = 47, description = "passwords must contain one numeric")
    public boolean validatePassword(String password) {
        return (password.matches("\\w*\\d\\w*"));
    }

    @UseCase(id = 49)
    public String encryptPassword(String password) {
        return new StringBuilder(password).reverse().toString();
    }
}
