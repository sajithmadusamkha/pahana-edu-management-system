package com.example.pahanaedubackend.service;

import com.example.pahanaedubackend.dao.UserDAO;
import com.example.pahanaedubackend.model.User;
import com.example.pahanaedubackend.util.PasswordUtil;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public User authenticate(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user == null) return null;

        boolean valid = PasswordUtil.checkPassword(password, user.getPassword());
        return valid ? user : null;
    }
}
