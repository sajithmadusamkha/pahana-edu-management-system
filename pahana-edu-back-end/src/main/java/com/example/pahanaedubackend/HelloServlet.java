package com.example.pahanaedubackend;

import com.example.pahanaedubackend.util.DBUtil;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(value = "/test-db")
public class HelloServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try (Connection conn = DBUtil.getInstance().getConnection()) {
            response.getWriter().println("DB Connection Successful!");
        } catch (SQLException e) {
            response.getWriter().println("Connection Failed: " + e.getMessage());
        }
    }

    public void destroy() {
    }
}