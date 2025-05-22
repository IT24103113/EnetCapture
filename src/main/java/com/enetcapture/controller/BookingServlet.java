package com.enetcapture.controller;

import com.enetcapture.model.Booking;
import com.enetcapture.model.Event;
import com.enetcapture.model.Photographer;
import com.enetcapture.model.Review;
import com.enetcapture.model.User;
import com.enetcapture.service.BookingService;
import com.enetcapture.service.EventService;
import com.enetcapture.service.PhotographerService;
import com.enetcapture.service.ReviewService;
import com.enetcapture.service.CustomQueue;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet(urlPatterns = {"/bookings", "/bookings/*", "/userDashboard", "/userDashboard/*"})
public class BookingServlet extends HttpServlet {
    private final BookingService bookingService = BookingService.getInstance();
    private final PhotographerService photographerService = PhotographerService.getInstance();
    private final EventService eventService = EventService.getInstance();
    private final ReviewService reviewService = ReviewService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        String pathInfo = request.getPathInfo();
        System.out.println("BookingServlet: Handling GET request - path: " + path + ", pathInfo: " + pathInfo + ", contextPath: " + request.getContextPath());
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect(request.getContextPath() + "/userLogin");
                return;
            }
            switch (path) {
                case "/userDashboard":
                    if (pathInfo == null || "/".equals(pathInfo)) {
                        User user = (User) session.getAttribute("user");
                        CustomQueue<Booking> userBookings = bookingService.getBookingsByUsername(user.getUsername());
                        request.setAttribute("bookings", userBookings.toArray(new Booking[0]));
                        request.setAttribute("photographers", photographerService.getAllPhotographers().toArray(new Photographer[0]));
                        request.setAttribute("events", eventService.getAllEvents().toArray(new Event[0]));
                        request.setAttribute("reviews", reviewService.getAllReviews().toArray(new Review[0]));
                        request.getRequestDispatcher("/WEB-INF/views/userDashboard.jsp").forward(request, response);
                    } else if ("/addBooking".equals(pathInfo)) {
                        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Use POST for adding bookings");
                        return;
                    }
                    break;
                case "/bookings":
                    if (session == null || !"admin".equals(session.getAttribute("userType"))) {
                        response.sendRedirect(request.getContextPath() + "/userLogin");
                        return;
                    }
                    if (pathInfo == null || "/".equals(pathInfo)) {
                        CustomQueue<Booking> bookings = bookingService.getAllBookings();
                        request.setAttribute("bookings", bookings.toArray(new Booking[0]));
                        request.getRequestDispatcher("/WEB-INF/views/bookingList.jsp").forward(request, response);
                    } else if ("/edit".equals(pathInfo)) {
                        String bookingId = request.getParameter("id");
                        if (bookingId == null) {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Booking ID is required");
                            return;
                        }
                        Booking booking = bookingService.getBookingById(Integer.parseInt(bookingId));
                        if (booking == null) {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Booking not found");
                            return;
                        }
                        request.setAttribute("booking", booking);
                        request.setAttribute("photographers", photographerService.getAllPhotographers().toArray(new Photographer[0]));
                        request.setAttribute("events", eventService.getAllEvents().toArray(new Event[0]));
                        request.getRequestDispatcher("/WEB-INF/views/editBooking.jsp").forward(request, response);
                    } else if ("/delete".equals(pathInfo)) {
                        String bookingId = request.getParameter("id");
                        if (bookingId == null) {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Booking ID is required");
                            return;
                        }
                        Booking booking = bookingService.getBookingById(Integer.parseInt(bookingId));
                        if (booking == null) {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Booking not found");
                            return;
                        }
                        if (bookingService.deleteBooking(Integer.parseInt(bookingId))) {
                            response.sendRedirect(request.getContextPath() + "/bookings");
                        } else {
                            request.setAttribute("error", "Failed to delete booking");
                            CustomQueue<Booking> bookings = bookingService.getAllBookings();
                            request.setAttribute("bookings", bookings.toArray(new Booking[0]));
                            request.getRequestDispatcher("/WEB-INF/views/bookingList.jsp").forward(request, response);
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid bookings path");
                    }
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid path");
                    break;
            }
        } catch (Exception e) {
            System.err.println("BookingServlet: Error in doGet: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        String pathInfo = request.getPathInfo();
        System.out.println("BookingServlet: Handling POST request - path: " + path + ", pathInfo: " + pathInfo + ", contextPath: " + request.getContextPath());
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect(request.getContextPath() + "/userLogin");
                return;
            }
            if ("/userDashboard".equals(path) && "/addBooking".equals(pathInfo)) {
                handleAddBooking(request, response);
            } else if ("/bookings".equals(path)) {
                if (!"admin".equals(session.getAttribute("userType"))) {
                    response.sendRedirect(request.getContextPath() + "/userLogin");
                    return;
                }
                if ("/edit".equals(pathInfo)) {
                    handleUpdateBooking(request, response);
                } else if ("/delete".equals(pathInfo)) {
                    handleDeleteBooking(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid bookings path");
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid path");
            }
        } catch (Exception e) {
            System.err.println("BookingServlet: Error in doPost: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void handleAddBooking(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        String photographer = request.getParameter("photographer");
        String eventIdStr = request.getParameter("eventId");
        String eventDate = request.getParameter("eventDate");
        String eventType = request.getParameter("eventType");
        if (photographer == null || eventIdStr == null || eventDate == null || eventType == null ||
                photographer.trim().isEmpty() || eventIdStr.trim().isEmpty() || eventDate.trim().isEmpty() || eventType.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required");
            CustomQueue<Booking> userBookings = bookingService.getBookingsByUsername(user.getUsername());
            request.setAttribute("bookings", userBookings.toArray(new Booking[0]));
            request.setAttribute("photographers", photographerService.getAllPhotographers().toArray(new Photographer[0]));
            request.setAttribute("events", eventService.getAllEvents().toArray(new Event[0]));
            request.setAttribute("reviews", reviewService.getAllReviews().toArray(new Review[0]));
            request.getRequestDispatcher("/WEB-INF/views/userDashboard.jsp").forward(request, response);
            return;
        }
        int eventId;
        try {
            eventId = Integer.parseInt(eventIdStr);
            if (eventId <= 0) {
                throw new NumberFormatException("Event ID must be positive");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid event ID");
            CustomQueue<Booking> userBookings = bookingService.getBookingsByUsername(user.getUsername());
            request.setAttribute("bookings", userBookings.toArray(new Booking[0]));
            request.setAttribute("photographers", photographerService.getAllPhotographers().toArray(new Photographer[0]));
            request.setAttribute("events", eventService.getAllEvents().toArray(new Event[0]));
            request.setAttribute("reviews", reviewService.getAllReviews().toArray(new Review[0]));
            request.getRequestDispatcher("/WEB-INF/views/userDashboard.jsp").forward(request, response);
            return;
        }
        Booking booking = new Booking();
        booking.setUsername(user.getUsername());
        booking.setPhotographer(photographer);
        try {
            booking.setEventDate(LocalDate.parse(eventDate));
        } catch (Exception e) {
            request.setAttribute("error", "Invalid event date format. Use YYYY-MM-DD.");
            CustomQueue<Booking> userBookings = bookingService.getBookingsByUsername(user.getUsername());
            request.setAttribute("bookings", userBookings.toArray(new Booking[0]));
            request.setAttribute("photographers", photographerService.getAllPhotographers().toArray(new Photographer[0]));
            request.setAttribute("events", eventService.getAllEvents().toArray(new Event[0]));
            request.setAttribute("reviews", reviewService.getAllReviews().toArray(new Review[0]));
            request.getRequestDispatcher("/WEB-INF/views/userDashboard.jsp").forward(request, response);
            return;
        }
        booking.setEventType(eventType);
        booking.setEventId(eventId);
        if (bookingService.addBooking(booking)) {
            request.setAttribute("booking", booking);
            request.getRequestDispatcher("/WEB-INF/views/bookingConfirmation.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Failed to add booking. Please try again.");
            CustomQueue<Booking> userBookings = bookingService.getBookingsByUsername(user.getUsername());
            request.setAttribute("bookings", userBookings.toArray(new Booking[0]));
            request.setAttribute("photographers", photographerService.getAllPhotographers().toArray(new Photographer[0]));
            request.setAttribute("events", eventService.getAllEvents().toArray(new Event[0]));
            request.setAttribute("reviews", reviewService.getAllReviews().toArray(new Review[0]));
            request.getRequestDispatcher("/WEB-INF/views/userDashboard.jsp").forward(request, response);
        }
    }

    private void handleUpdateBooking(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String username = request.getParameter("username");
        String photographer = request.getParameter("photographer");
        String eventIdStr = request.getParameter("eventId");
        String eventDate = request.getParameter("eventDate");
        String eventType = request.getParameter("eventType");
        if (id == null || username == null || photographer == null || eventIdStr == null || eventDate == null || eventType == null ||
                id.trim().isEmpty() || username.trim().isEmpty() || photographer.trim().isEmpty() || eventIdStr.trim().isEmpty() || eventDate.trim().isEmpty() || eventType.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required");
            Booking booking = bookingService.getBookingById(Integer.parseInt(id));
            request.setAttribute("booking", booking);
            request.setAttribute("photographers", photographerService.getAllPhotographers().toArray(new Photographer[0]));
            request.setAttribute("events", eventService.getAllEvents().toArray(new Event[0]));
            request.getRequestDispatcher("/WEB-INF/views/editBooking.jsp").forward(request, response);
            return;
        }
        int eventId;
        try {
            eventId = Integer.parseInt(eventIdStr);
            if (eventId <= 0) {
                throw new NumberFormatException("Event ID must be positive");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid event ID");
            Booking booking = bookingService.getBookingById(Integer.parseInt(id));
            request.setAttribute("booking", booking);
            request.setAttribute("photographers", photographerService.getAllPhotographers().toArray(new Photographer[0]));
            request.setAttribute("events", eventService.getAllEvents().toArray(new Event[0]));
            request.getRequestDispatcher("/WEB-INF/views/editBooking.jsp").forward(request, response);
            return;
        }
        LocalDate date;
        try {
            date = LocalDate.parse(eventDate);
        } catch (Exception e) {
            request.setAttribute("error", "Invalid date format. Use YYYY-MM-DD.");
            Booking booking = bookingService.getBookingById(Integer.parseInt(id));
            request.setAttribute("booking", booking);
            request.setAttribute("photographers", photographerService.getAllPhotographers().toArray(new Photographer[0]));
            request.setAttribute("events", eventService.getAllEvents().toArray(new Event[0]));
            request.getRequestDispatcher("/WEB-INF/views/editBooking.jsp").forward(request, response);
            return;
        }
        Booking updatedBooking = new Booking(Integer.parseInt(id), username, photographer, date, eventType, eventId);
        if (bookingService.updateBooking(updatedBooking)) {
            response.sendRedirect(request.getContextPath() + "/bookings");
        } else {
            request.setAttribute("error", "Failed to update booking");
            request.setAttribute("booking", updatedBooking);
            request.setAttribute("photographers", photographerService.getAllPhotographers().toArray(new Photographer[0]));
            request.setAttribute("events", eventService.getAllEvents().toArray(new Event[0]));
            request.getRequestDispatcher("/WEB-INF/views/editBooking.jsp").forward(request, response);
        }
    }

    private void handleDeleteBooking(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        if (id == null || id.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Booking ID is required");
            return;
        }
        if (bookingService.deleteBooking(Integer.parseInt(id))) {
            response.sendRedirect(request.getContextPath() + "/bookings");
        } else {
            request.setAttribute("error", "Failed to delete booking");
            CustomQueue<Booking> bookings = bookingService.getAllBookings();
            request.setAttribute("bookings", bookings.toArray(new Booking[0]));
            request.getRequestDispatcher("/WEB-INF/views/bookingList.jsp").forward(request, response);
        }
    }
}