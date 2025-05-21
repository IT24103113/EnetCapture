package com.enetcapture.controller;

import com.enetcapture.model.User;
import com.enetcapture.service.BookingService;
import com.enetcapture.service.EventService;
import com.enetcapture.service.PhotographerService;
import com.enetcapture.service.ReviewService;
import com.enetcapture.service.UserService;
import com.enetcapture.service.CustomArray;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = {"/", "/userLogin", "/logout", "/register", "/profile", "/profile/*", "/auth/check", "/home"})
public class UserServlet extends HttpServlet {
    private final UserService userService = UserService.getInstance();
    private final BookingService bookingService = BookingService.getInstance();
    private final PhotographerService photographerService = PhotographerService.getInstance();
    private final EventService eventService = EventService.getInstance();
    private final ReviewService reviewService = ReviewService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        String pathInfo = request.getPathInfo();
        System.out.println("UserServlet: Handling GET request - path: " + path + ", pathInfo: " + pathInfo + ", contextPath: " + request.getContextPath());
        try {
            HttpSession session = request.getSession(false);
            switch (path) {
                case "/":
                    request.getRequestDispatcher("/index.html").forward(request, response);
                    break;
                case "/userLogin":
                    if (session != null && session.getAttribute("user") != null) {
                        response.sendRedirect(request.getContextPath() + "/userDashboard");
                        return;
                    }
                    request.getRequestDispatcher("/WEB-INF/views/userLogin.jsp").forward(request, response);
                    break;
                case "/logout":
                    if (session != null) {
                        session.invalidate();
                    }
                    response.sendRedirect(request.getContextPath() + "/");
                    break;
                case "/register":
                    request.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(request, response);
                    break;
                case "/profile":
                    if (session == null || session.getAttribute("user") == null) {
                        response.sendRedirect(request.getContextPath() + "/userLogin");
                        return;
                    }
                    pathInfo = request.getPathInfo();
                    if (pathInfo == null || pathInfo.equals("/")) {
                        User user = (User) session.getAttribute("user");
                        request.setAttribute("user", user);
                        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
                    } else if (pathInfo.equals("/edit")) {
                        String username = request.getParameter("username");
                        User userToEdit = (User) session.getAttribute("user");
                        if (username != null && !"admin".equals(session.getAttribute("userType"))) {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                            return;
                        }
                        userToEdit = username != null && "admin".equals(session.getAttribute("userType")) ? userService.getUserByUsername(username) : userToEdit;
                        request.setAttribute("user", userToEdit);
                        request.getRequestDispatcher("/WEB-INF/views/editProfile.jsp").forward(request, response);
                    } else if (pathInfo.equals("/delete")) {
                        handleProfileDelete(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid profile path");
                    }
                    break;
                case "/auth/check":
                    response.setContentType("application/json");
                    boolean isAuthenticated = session != null && session.getAttribute("user") != null;
                    response.getWriter().write("{\"isAuthenticated\":" + isAuthenticated + "}");
                    break;
                case "/home":
                    request.getRequestDispatcher("/index.html").forward(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid path");
                    break;
            }
        } catch (Exception e) {
            System.err.println("UserServlet: Error in doGet: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        String pathInfo = request.getPathInfo();
        System.out.println("UserServlet: Handling POST request - path: " + path + ", pathInfo: " + pathInfo + ", contextPath: " + request.getContextPath());
        try {
            HttpSession session = request.getSession(false);
            if ("/userLogin".equals(path)) {
                handleLogin(request, response);
            } else if ("/register".equals(path)) {
                handleRegistration(request, response);
            } else if ("/profile".equals(path)) {
                if (session == null || session.getAttribute("user") == null) {
                    response.sendRedirect(request.getContextPath() + "/userLogin");
                    return;
                }
                if (pathInfo != null && pathInfo.equals("/edit")) {
                    handleProfileUpdate(request, response);
                } else if (pathInfo != null && pathInfo.equals("/delete")) {
                    handleProfileDelete(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid profile path");
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid path");
            }
        } catch (Exception e) {
            System.err.println("UserServlet: Error in doPost: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("/WEB-INF/views/userLogin.jsp").forward(request, response);
            return;
        }
        User user = userService.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("userType", "user");
            response.sendRedirect(request.getContextPath() + "/userDashboard");
            return;
        }
        request.setAttribute("error", "Invalid username or password");
        request.getRequestDispatcher("/WEB-INF/views/userLogin.jsp").forward(request, response);
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        if (username == null || password == null || confirmPassword == null || fullName == null ||
                username.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty() || fullName.trim().isEmpty()) {
            request.setAttribute("error", "All required fields must be filled");
            request.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(request, response);
            return;
        }
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            request.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(request, response);
            return;
        }
        User newUser = new User(username, password, fullName, request.getParameter("email") != null ? request.getParameter("email").trim() : "", request.getParameter("phone") != null ? request.getParameter("phone").trim() : "");
        if (userService.addUser(newUser)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", newUser);
            session.setAttribute("userType", "user");
            response.sendRedirect(request.getContextPath() + "/userDashboard");
        } else {
            request.setAttribute("error", "Username already exists");
            request.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(request, response);
        }
    }

    private void handleProfileUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/userLogin");
            return;
        }
        String username = request.getParameter("username");
        User userToUpdate = (User) session.getAttribute("user");
        if (username != null && "admin".equals(session.getAttribute("userType"))) {
            userToUpdate = userService.getUserByUsername(username);
        }
        if (userToUpdate == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        if (fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("error", "Full name is required");
            request.setAttribute("user", userToUpdate);
            request.getRequestDispatcher("/WEB-INF/views/editProfile.jsp").forward(request, response);
            return;
        }
        String newPassword = password != null && !password.trim().isEmpty() ? password.trim() : userToUpdate.getPassword();
        User updatedUser = new User(userToUpdate.getUsername(), newPassword, fullName, request.getParameter("email") != null ? request.getParameter("email").trim() : userToUpdate.getEmail(), request.getParameter("phone") != null ? request.getParameter("phone").trim() : userToUpdate.getPhone());
        if (userService.updateUser(updatedUser)) {
            if (userToUpdate.getUsername().equals(((User) session.getAttribute("user")).getUsername())) {
                session.setAttribute("user", updatedUser);
            }
            response.sendRedirect(request.getContextPath() + (session.getAttribute("userType").equals("admin") ? "/adminDashboard" : "/userDashboard"));
        } else {
            request.setAttribute("error", "Failed to update profile");
            request.setAttribute("user", userToUpdate);
            request.getRequestDispatcher("/WEB-INF/views/editProfile.jsp").forward(request, response);
        }
    }

    private void handleProfileDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/userLogin");
            return;
        }
        String username = request.getParameter("username");
        String userToDelete = ((User) session.getAttribute("user")).getUsername();
        if (username != null && "admin".equals(session.getAttribute("userType"))) {
            userToDelete = username;
        }
        if (userService.deleteUser(userToDelete)) {
            if (userToDelete.equals(((User) session.getAttribute("user")).getUsername())) {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/");
            } else {
                response.sendRedirect(request.getContextPath() + "/adminDashboard");
            }
        } else {
            request.setAttribute("error", "Failed to delete account");
            request.getRequestDispatcher("/WEB-INF/views/" + (session.getAttribute("userType").equals("admin") ? "adminDashboard.jsp" : "profile.jsp")).forward(request, response);
        }
    }
}