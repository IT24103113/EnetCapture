package com.enetcapture.controller;

import com.enetcapture.model.Review;
import com.enetcapture.model.User;
import com.enetcapture.service.PhotographerService;
import com.enetcapture.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = {"/reviews", "/reviews/*", "/admin/reviews", "/admin/reviews/*"})
public class ReviewServlet extends HttpServlet {
    private final ReviewService reviewService = ReviewService.getInstance();
    private final PhotographerService photographerService = PhotographerService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/userLogin");
            return;
        }

        if (path.startsWith("/admin")) {
            if (!"admin".equals(session.getAttribute("userType"))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }
            if ("/admin/reviews".equals(path) && (pathInfo == null || "/".equals(pathInfo))) {
                request.setAttribute("reviews", reviewService.getAllReviews());
                request.getRequestDispatcher("/WEB-INF/views/adminReviews.jsp").forward(request, response);
            } else if ("/admin/reviews/edit".equals(path + (pathInfo != null ? pathInfo : ""))) {
                String idStr = request.getParameter("id");
                if (idStr != null) {
                    int id = Integer.parseInt(idStr);
                    Review review = reviewService.getReviewById(id);
                    if (review != null) {
                        request.setAttribute("review", review);
                        request.setAttribute("photographers", photographerService.getAllPhotographers());
                        request.getRequestDispatcher("/WEB-INF/views/editReview.jsp").forward(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Review not found");
                    }
                }
            } else if ("/admin/reviews/delete".equals(path + (pathInfo != null ? pathInfo : ""))) {
                String idStr = request.getParameter("id");
                if (idStr != null && reviewService.deleteReview(Integer.parseInt(idStr))) {
                    response.sendRedirect(request.getContextPath() + "/admin/reviews");
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to delete review");
                }
            }
        } else {
            if ("/reviews".equals(path) && ("/".equals(pathInfo) || pathInfo == null)) {
                request.setAttribute("reviews", reviewService.getAllReviews());
                request.getRequestDispatcher("/WEB-INF/views/userDashboard.jsp").forward(request, response);
            } else if ("/reviews/add".equals(path + (pathInfo != null ? pathInfo : ""))) {
                request.setAttribute("photographers", photographerService.getAllPhotographers());
                request.getRequestDispatcher("/WEB-INF/views/addReview.jsp").forward(request, response);
            } else if ("/reviews/edit".equals(path + (pathInfo != null ? pathInfo : ""))) {
                String idStr = request.getParameter("id");
                if (idStr != null) {
                    int id = Integer.parseInt(idStr);
                    Review review = reviewService.getReviewById(id);
                    if (review != null) {
                        request.setAttribute("review", review);
                        request.setAttribute("photographers", photographerService.getAllPhotographers());
                        request.getRequestDispatcher("/WEB-INF/views/editReview.jsp").forward(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Review not found");
                    }
                }
            } else if ("/reviews/delete".equals(path + (pathInfo != null ? pathInfo : ""))) {
                String idStr = request.getParameter("id");
                if (idStr != null && reviewService.deleteReview(Integer.parseInt(idStr))) {
                    response.sendRedirect(request.getContextPath() + "/reviews");
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to delete review");
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/userLogin");
            return;
        }
        if (path.startsWith("/admin") && !"admin".equals(session.getAttribute("userType"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }
        if ("/reviews".equals(path) && ("/".equals(pathInfo) || pathInfo == null)) {
            handleAddReview(request, response);
        } else if ("/reviews/edit".equals(path + (pathInfo != null ? pathInfo : "")) || "/admin/reviews/edit".equals(path + (pathInfo != null ? pathInfo : ""))) {
            handleUpdateReview(request, response);
        }
    }

    private void handleAddReview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleReviewOperation(request, response, true);
    }

    private void handleUpdateReview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Review ID is required");
            return;
        }
        handleReviewOperation(request, response, false);
    }

    private void handleReviewOperation(HttpServletRequest request, HttpServletResponse response, boolean isAdd) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String username;
        Object userObj = session.getAttribute("user");
        if (userObj instanceof User) {
            username = ((User) userObj).getUsername();
        } else if (userObj instanceof String) {
            username = (String) userObj;
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid user session attribute");
            return;
        }

        String photographer = request.getParameter("photographer");
        String ratingStr = request.getParameter("rating");
        String comment = request.getParameter("comment");
        String eventIdStr = request.getParameter("eventId");
        String idStr = isAdd ? null : request.getParameter("id");

        if (photographer == null || ratingStr == null || comment == null || eventIdStr == null ||
                photographer.trim().isEmpty() || comment.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required");
            if (isAdd) {
                request.setAttribute("photographers", photographerService.getAllPhotographers());
                request.getRequestDispatcher("/WEB-INF/views/addReview.jsp").forward(request, response);
            } else {
                String path = request.getServletPath() + (request.getPathInfo() != null ? request.getPathInfo() : "");
                if (path.startsWith("/admin")) {
                    request.setAttribute("reviews", reviewService.getAllReviews());
                    request.getRequestDispatcher("/WEB-INF/views/adminReviews.jsp").forward(request, response);
                } else {
                    request.setAttribute("reviews", reviewService.getAllReviews());
                    request.getRequestDispatcher("/WEB-INF/views/userDashboard.jsp").forward(request, response);
                }
            }
            return;
        }

        double rating;
        int eventId;
        int id = -1;
        try {
            rating = Double.parseDouble(ratingStr);
            eventId = Integer.parseInt(eventIdStr);
            if (!isAdd) id = Integer.parseInt(idStr);
            if (rating < 0 || rating > 5 || eventId <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Rating must be between 0 and 5, and event ID must be positive");
            if (isAdd) {
                request.setAttribute("photographers", photographerService.getAllPhotographers());
                request.getRequestDispatcher("/WEB-INF/views/addReview.jsp").forward(request, response);
            } else {
                String path = request.getServletPath() + (request.getPathInfo() != null ? request.getPathInfo() : "");
                if (path.startsWith("/admin")) {
                    request.setAttribute("reviews", reviewService.getAllReviews());
                    request.getRequestDispatcher("/WEB-INF/views/adminReviews.jsp").forward(request, response);
                } else {
                    request.setAttribute("reviews", reviewService.getAllReviews());
                    request.getRequestDispatcher("/WEB-INF/views/userDashboard.jsp").forward(request, response);
                }
            }
            return;
        }

        Review review = new Review();
        review.setUsername(username);
        review.setPhotographer(photographer);
        review.setRating(rating);
        review.setComment(comment);
        review.setEventId(eventId);
        if (!isAdd) review.setId(id);

        boolean success = isAdd ? reviewService.addReview(review) : reviewService.updateReview(review);
        if (success) {
            String path = request.getServletPath() + (request.getPathInfo() != null ? request.getPathInfo() : "");
            if (path.startsWith("/admin")) {
                response.sendRedirect(request.getContextPath() + "/admin/reviews");
            } else {
                response.sendRedirect(request.getContextPath() + "/reviews");
            }
        } else {
            request.setAttribute("error", "Failed to " + (isAdd ? "add" : "update") + " review");
            if (isAdd) {
                request.setAttribute("photographers", photographerService.getAllPhotographers());
                request.getRequestDispatcher("/WEB-INF/views/addReview.jsp").forward(request, response);
            } else {
                String path = request.getServletPath() + (request.getPathInfo() != null ? request.getPathInfo() : "");
                if (path.startsWith("/admin")) {
                    request.setAttribute("reviews", reviewService.getAllReviews());
                    request.getRequestDispatcher("/WEB-INF/views/adminReviews.jsp").forward(request, response);
                } else {
                    request.setAttribute("reviews", reviewService.getAllReviews());
                    request.getRequestDispatcher("/WEB-INF/views/userDashboard.jsp").forward(request, response);
                }
            }
        }
    }
}