package com.example.pahanaedubackend.service;

import com.example.pahanaedubackend.dao.AdminDAO;
import com.example.pahanaedubackend.dao.UserDAO;
import com.example.pahanaedubackend.model.Admin;
import com.example.pahanaedubackend.util.PasswordUtil;

public class AdminService {
    private final AdminDAO adminDAO = new AdminDAO();
    private final UserDAO userDAO = new UserDAO();

    public boolean registerAdmin(Admin admin, String plainPassword) {
        boolean profileSaved = adminDAO.addAdmin(admin);

        if (profileSaved) {
            String hashedPassword = PasswordUtil.hashPassword(plainPassword);
            return userDAO.createUser(admin.getUsername(), hashedPassword, "A");
        }

        return false;
    }
}
