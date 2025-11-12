package com.example.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String email = request.getParameter("email");

        if (name == null || email == null || name.trim().isEmpty() || email.trim().isEmpty()) {
            out.println("<h3>Invalid input. Both name and email are required.</h3>");
            return;
        }

        // JDBC connection settings (provided)
        String jdbcUrl = "jdbc:mysql://localhost:3306/studentdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String dbUser = "aman";
        String dbPass = "12345";

        try {
            // Load the driver (optional with modern drivers)
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(jdbcUrl, dbUser, dbPass)) {
                String sql = "INSERT INTO students (name, email) VALUES (?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, name);
                    ps.setString(2, email);
                    int rows = ps.executeUpdate();
                    if (rows > 0) {
                        out.println("<h3>Registration Successful!</h3>");
                    } else {
                        out.println("<h3>Registration Failed.</h3>");
                    }
                }
            }
        } catch (Exception e) {
            out.println("<h3>Error:</h3>");
            out.println("<pre>" + e.getMessage() + "</pre>");
        }
    }
}
