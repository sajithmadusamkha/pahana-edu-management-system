package com.example.pahanaedubackend.service;

import com.example.pahanaedubackend.dao.AdminDAO;
import com.example.pahanaedubackend.dao.UserDAO;
import com.example.pahanaedubackend.factory.DAOFactory;
import com.example.pahanaedubackend.model.Admin;
import com.example.pahanaedubackend.util.PasswordUtil;

public class AdminService {
    private final AdminDAO adminDAO = new AdminDAO();

    public boolean registerAdmin(Admin admin, String plainPassword) {
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        admin.setPassword(hashedPassword);
        return adminDAO.addAdmin(admin);
    }

    public Admin login(String username, String plainPassword) {
        Admin admin = adminDAO.getAdminByUsername(username);
        if (admin != null && PasswordUtil.checkPassword(plainPassword, admin.getPassword())) {
            return admin;
        }
        return null;
    }
}
