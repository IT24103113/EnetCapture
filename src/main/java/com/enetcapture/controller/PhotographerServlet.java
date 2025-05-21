package com.enetcapture.controller;

import com.enetcapture.model.Photographer;
import com.enetcapture.service.PhotographerService;
import com.enetcapture.service.CustomArray;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = {"/photographers", "/photographers/*"})
public class PhotographerServlet extends HttpServlet {
    private final PhotographerService photographerService = PhotographerService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/userLogin");
            return;
        }
        if ("/".equals(pathInfo) || pathInfo == null) {
            CustomArray<Photographer> photographers = photographerService.getAllPhotographers();
            request.setAttribute("photographers", photographers.toArray(new Photographer[0]));
            if ("admin".equals(session.getAttribute("userType"))) {
                request.getRequestDispatcher("/WEB-INF/views/photographerList.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/WEB-INF/views/photographers.jsp").forward(request, response);
            }
        } else if ("/add".equals(pathInfo)) {
            if (!"admin".equals(session.getAttribute("userType"))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }
            request.getRequestDispatcher("/WEB-INF/views/addPhotographer.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/edit")) {
            if (!"admin".equals(session.getAttribute("userType"))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }
            String name = request.getParameter("name");
            if (name != null) {
                Photographer photographer = null;
                CustomArray<Photographer> photographers = photographerService.getAllPhotographers();
                for (int i = 0; i < photographers.size(); i++) {
                    if (photographers.get(i).getName().equals(name)) {
                        photographer = photographers.get(i);
                        break;
                    }
                }
                if (photographer != null) {
                    request.setAttribute("photographer", photographer);
                    request.getRequestDispatcher("/WEB-INF/views/editPhotographer.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Photographer not found");
                }
            }
        } else if (pathInfo.startsWith("/delete")) {
            if (!"admin".equals(session.getAttribute("userType"))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }
            String name = request.getParameter("name");
            if (name != null && photographerService.deletePhotographer(name)) {
                response.sendRedirect(request.getContextPath() + "/photographers");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to delete photographer");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || !"admin".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/userLogin");
            return;
        }
        if ("/".equals(pathInfo) || pathInfo == null) {
            handleAddPhotographer(request, response);
        } else if ("/add".equals(pathInfo)) {
            handleAddPhotographer(request, response);
        } else if (pathInfo.startsWith("/edit")) {
            handleUpdatePhotographer(request, response);
        }
    }

    private void handleAddPhotographer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String ratingStr = request.getParameter("rating");
        if (name == null || ratingStr == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Name and rating are required");
            CustomArray<Photographer> photographers = photographerService.getAllPhotographers();
            request.setAttribute("photographers", photographers.toArray(new Photographer[0]));
            request.getRequestDispatcher("/WEB-INF/views/addPhotographer.jsp").forward(request, response);
            return;
        }
        double rating;
        try {
            rating = Double.parseDouble(ratingStr);
            if (rating < 0 || rating > 5) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Rating must be between 0 and 5");
            CustomArray<Photographer> photographers = photographerService.getAllPhotographers();
            request.setAttribute("photographers", photographers.toArray(new Photographer[0]));
            request.getRequestDispatcher("/WEB-INF/views/addPhotographer.jsp").forward(request, response);
            return;
        }
        if (photographerService.addPhotographer(name, rating)) {
            response.sendRedirect(request.getContextPath() + "/photographers");
        } else {
            request.setAttribute("error", "Photographer already exists");
            CustomArray<Photographer> photographers = photographerService.getAllPhotographers();
            request.setAttribute("photographers", photographers.toArray(new Photographer[0]));
            request.getRequestDispatcher("/WEB-INF/views/addPhotographer.jsp").forward(request, response);
        }
    }

    private void handleUpdatePhotographer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String ratingStr = request.getParameter("rating");
        if (name == null || ratingStr == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Name and rating are required");
            CustomArray<Photographer> photographers = photographerService.getAllPhotographers();
            request.setAttribute("photographers", photographers.toArray(new Photographer[0]));
            request.getRequestDispatcher("/WEB-INF/views/editPhotographer.jsp").forward(request, response);
            return;
        }
        double rating;
        try {
            rating = Double.parseDouble(ratingStr);
            if (rating < 0 || rating > 5) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Rating must be between 0 and 5");
            CustomArray<Photographer> photographers = photographerService.getAllPhotographers();
            request.setAttribute("photographers", photographers.toArray(new Photographer[0]));
            request.getRequestDispatcher("/WEB-INF/views/editPhotographer.jsp").forward(request, response);
            return;
        }
        if (photographerService.updatePhotographer(name, rating)) {
            response.sendRedirect(request.getContextPath() + "/photographers");
        } else {
            request.setAttribute("error", "Photographer not found");
            CustomArray<Photographer> photographers = photographerService.getAllPhotographers();
            request.setAttribute("photographers", photographers.toArray(new Photographer[0]));
            request.getRequestDispatcher("/WEB-INF/views/editPhotographer.jsp").forward(request, response);
        }
    }
}