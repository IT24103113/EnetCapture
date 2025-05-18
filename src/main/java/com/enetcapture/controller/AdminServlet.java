package com.enetcapture.controller;

import com.enetcapture.model.User;
import com.enetcapture.model.Admin;
import com.enetcapture.service.AdminService;
import com.enetcapture.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/adminLogin", "/admin/*", "/admins/*", "/adminDashboard", "/users", "/users/*"})
public class AdminServlet extends HttpServlet {
    private final AdminService adminService = AdminService.getInstance();
    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        String pathInfo = request.getPathInfo();
        System.out.println("AdminServlet: Handling GET request - path: " + path + ", pathInfo: " + pathInfo + ", contextPath: " + request.getContextPath());
        try {
            HttpSession session = request.getSession(false);
            if ("/admin".equals(path) && "/register".equals(pathInfo)) {
                System.out.println("AdminServlet: Accessing register admin form without authentication");
                request.getRequestDispatcher("/WEB-INF/views/registerAdmin.jsp").forward(request, response);
                return;
            }
            if ("/admin".equals(path) && "/add".equals(pathInfo)) {
                if (session == null || session.getAttribute("user") == null) {
                    response.sendRedirect(request.getContextPath() + "/adminLogin");
                    return;
                }
                request.getRequestDispatcher("/WEB-INF/views/addAdmin.jsp").forward(request, response);
                return;
            }
            switch (path) {
                case "/adminLogin":
                    if (session != null && session.getAttribute("user") != null) {
                        response.sendRedirect(request.getContextPath() + "/adminDashboard");
                        return;
                    }
                    request.getRequestDispatcher("/WEB-INF/views/adminLogin.jsp").forward(request, response);
                    break;
                case "/admin":
                case "/admins":
                    if (session == null || session.getAttribute("user") == null) {
                        response.sendRedirect(request.getContextPath() + "/adminLogin");
                        return;
                    }
                    if ("/dashboard".equals(pathInfo) || pathInfo == null || "/".equals(pathInfo)) {
                        request.setAttribute("admins", adminService.getAllAdmins());
                        request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(request, response);
                    } else if ("/edit".equals(pathInfo)) {
                        String username = request.getParameter("username");
                        System.out.println("AdminServlet: Attempting to edit admin with username: " + username);
                        if (username != null && !username.trim().isEmpty()) {
                            Admin admin = adminService.getAllAdmins().stream()
                                    .filter(a -> a.getUsername().equalsIgnoreCase(username))
                                    .findFirst()
                                    .orElse(null);
                            if (admin != null) {
                                request.setAttribute("admin", admin);
                            } else {
                                request.setAttribute("error", "Admin not found for username: " + username);
                                request.setAttribute("admin", new Admin(username, ""));
                            }
                            request.getRequestDispatcher("/WEB-INF/views/editAdmin.jsp").forward(request, response);
                        } else {
                            request.setAttribute("error", "Username parameter is required");
                            response.sendRedirect(request.getContextPath() + "/admins");
                        }
                    } else if ("/delete".equals(pathInfo)) {
                        handleDeleteAdmin(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/admins");
                    }
                    break;
                case "/adminDashboard":
                    if (session == null || session.getAttribute("user") == null || !"admin".equals(session.getAttribute("userType"))) {
                        response.sendRedirect(request.getContextPath() + "/adminLogin");
                        return;
                    }
                    List<User> users = userService.getAllUsers();
                    request.setAttribute("users", users);
                    request.setAttribute("admins", adminService.getAllAdmins());
                    request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(request, response);
                    break;
                case "/users":
                    if (session == null || !"admin".equals(session.getAttribute("userType"))) {
                        response.sendRedirect(request.getContextPath() + "/userDashboard");
                        return;
                    }
                    if (pathInfo == null || "/".equals(pathInfo)) {
                        users = userService.getAllUsers();
                        request.setAttribute("users", users);
                        request.getRequestDispatcher("/WEB-INF/views/userList.jsp").forward(request, response);
                    } else if ("/edit".equals(pathInfo)) {
                        String username = request.getParameter("username");
                        if (username == null) {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username is required");
                            return;
                        }
                        User user = userService.getUserByUsername(username);
                        if (user == null) {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                            return;
                        }
                        request.setAttribute("user", user);
                        request.getRequestDispatcher("/WEB-INF/views/editUser.jsp").forward(request, response);
                    } else if ("/delete".equals(pathInfo)) {
                        String username = request.getParameter("username");
                        if (username == null) {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username is required");
                            return;
                        }
                        if (userService.deleteUser(username)) {
                            response.sendRedirect(request.getContextPath() + "/users");
                        } else {
                            request.setAttribute("error", "Failed to delete user");
                            users = userService.getAllUsers();
                            request.setAttribute("users", users);
                            request.getRequestDispatcher("/WEB-INF/views/userList.jsp").forward(request, response);
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid users path");
                    }
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid path");
                    break;
            }
        } catch (Exception e) {
            System.err.println("AdminServlet: Error in doGet: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        String pathInfo = request.getPathInfo();
        System.out.println("AdminServlet: Handling POST request - path: " + path + ", pathInfo: " + pathInfo + ", contextPath: " + request.getContextPath());
        try {
            HttpSession session = request.getSession(false);
            if ("/adminLogin".equals(path)) {
                handleLogin(request, response);
            } else if ("/admin".equals(path) && "/register".equals(pathInfo)) {
                handleRegisterAdmin(request, response);
            } else if ("/admin".equals(path) && "/add".equals(pathInfo)) {
                if (session == null || session.getAttribute("user") == null) {
                    response.sendRedirect(request.getContextPath() + "/adminLogin");
                    return;
                }
                handleAddAdmin(request, response);
            } else if ("/admin".equals(path) || "/admins".equals(path)) {
                if (session == null || session.getAttribute("user") == null) {
                    response.sendRedirect(request.getContextPath() + "/adminLogin");
                    return;
                }
                if ("/edit".equals(pathInfo)) {
                    handleUpdateAdmin(request, response);
                } else if ("/delete".equals(pathInfo)) {
                    handleDeleteAdmin(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid admin path");
                }
            } else if ("/users".equals(path)) {
                if (session == null || !"admin".equals(session.getAttribute("userType"))) {
                    response.sendRedirect(request.getContextPath() + "/userDashboard");
                    return;
                }
                if ("/edit".equals(pathInfo)) {
                    String username = request.getParameter("username");
                    String password = request.getParameter("password");
                    String fullName = request.getParameter("fullName");
                    String email = request.getParameter("email");
                    String phone = request.getParameter("phone");
                    if (username == null || password == null || fullName == null || email == null || phone == null ||
                            username.trim().isEmpty() || password.trim().isEmpty() || fullName.trim().isEmpty() || email.trim().isEmpty() || phone.trim().isEmpty()) {
                        request.setAttribute("error", "All fields are required");
                        User user = userService.getUserByUsername(username);
                        request.setAttribute("user", user);
                        request.getRequestDispatcher("/WEB-INF/views/editUser.jsp").forward(request, response);
                        return;
                    }
                    User updatedUser = new User(username, password, fullName, email, phone);
                    if (userService.updateUser(updatedUser)) {
                        response.sendRedirect(request.getContextPath() + "/users");
                    } else {
                        request.setAttribute("error", "Failed to update user");
                        request.setAttribute("user", updatedUser);
                        request.getRequestDispatcher("/WEB-INF/views/editUser.jsp").forward(request, response);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid users path");
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid path");
            }
        } catch (Exception e) {
            System.err.println("AdminServlet: Error in doPost: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("/WEB-INF/views/adminLogin.jsp").forward(request, response);
            return;
        }
        if (adminService.authenticate(username, password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", username);
            session.setAttribute("userType", "admin");
            response.sendRedirect(request.getContextPath() + "/adminDashboard");
            return;
        }
        request.setAttribute("error", "Invalid username or password");
        request.getRequestDispatcher("/WEB-INF/views/adminLogin.jsp").forward(request, response);
    }

    private void handleAddAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("AdminServlet: Attempting to add admin - username: " + username);
        if (username == null || username.trim().isEmpty() || password == null || password.trim().length() < 6 || password.contains(" ")) {
            System.err.println("AdminServlet: Invalid input for adding admin - username: " + username);
            request.setAttribute("error", "Username and password are required. Password must be at least 6 characters with no spaces.");
            request.getRequestDispatcher("/WEB-INF/views/addAdmin.jsp").forward(request, response);
            return;
        }
        if (adminService.addAdmin(username, password)) {
            response.sendRedirect(request.getContextPath() + "/admins");
        } else {
            request.setAttribute("error", "Failed to add admin. Username may already exist.");
            request.getRequestDispatcher("/WEB-INF/views/addAdmin.jsp").forward(request, response);
        }
    }

    private void handleRegisterAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("AdminServlet: Attempting to register admin - username: " + username);
        if (username == null || username.trim().isEmpty() || password == null || password.trim().length() < 6 || password.contains(" ")) {
            System.err.println("AdminServlet: Invalid input for registering admin - username: " + username);
            request.setAttribute("error", "Username and password are required. Password must be at least 6 characters with no spaces.");
            request.getRequestDispatcher("/WEB-INF/views/registerAdmin.jsp").forward(request, response);
            return;
        }
        if (adminService.addAdmin(username, password)) {
            response.sendRedirect(request.getContextPath() + "/adminLogin");
        } else {
            request.setAttribute("error", "Failed to register admin. Username may already exist.");
            request.getRequestDispatcher("/WEB-INF/views/registerAdmin.jsp").forward(request, response);
        }
    }

    private void handleUpdateAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String newPassword = request.getParameter("password");
        System.out.println("AdminServlet: Attempting to update admin - username: " + username + ", password length: " + (newPassword != null ? newPassword.length() : "null"));
        if (username == null || username.trim().isEmpty()) {
            System.err.println("AdminServlet: Username is missing or empty");
            request.setAttribute("error", "Username is required");
            request.setAttribute("admin", new Admin("", ""));
            request.getRequestDispatcher("/WEB-INF/views/editAdmin.jsp").forward(request, response);
            return;
        }
        if (newPassword == null || newPassword.trim().length() < 6 || newPassword.contains(" ")) {
            System.err.println("AdminServlet: Invalid password for username: " + username);
            request.setAttribute("error", "Password must be at least 6 characters long and contain no spaces");
            request.setAttribute("admin", new Admin(username, ""));
            request.getRequestDispatcher("/WEB-INF/views/editAdmin.jsp").forward(request, response);
            return;
        }
        boolean updated = adminService.updateAdmin(username, newPassword);
        System.out.println("AdminServlet: Update result for username " + username + ": " + updated);
        if (updated) {
            response.sendRedirect(request.getContextPath() + "/admins");
        } else {
            System.err.println("AdminServlet: Failed to update admin for username: " + username);
            request.setAttribute("error", "Failed to update admin. Admin not found or file error.");
            request.setAttribute("admin", new Admin(username, ""));
            request.getRequestDispatcher("/WEB-INF/views/editAdmin.jsp").forward(request, response);
        }
    }

    private void handleDeleteAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("error", "Username is required");
            request.setAttribute("admins", adminService.getAllAdmins());
            request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession(false);
        String currentAdmin = (String) session.getAttribute("user");
        if (currentAdmin.equals(username)) {
            request.setAttribute("error", "You cannot delete your own account");
            request.setAttribute("admins", adminService.getAllAdmins());
            request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(request, response);
            return;
        }
        if (adminService.deleteAdmin(username)) {
            response.sendRedirect(request.getContextPath() + "/adminDashboard");
        } else {
            request.setAttribute("error", "Failed to delete admin");
            request.setAttribute("admins", adminService.getAllAdmins());
            request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(request, response);
        }
    }
}