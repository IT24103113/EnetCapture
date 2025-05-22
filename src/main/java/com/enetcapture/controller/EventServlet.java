package com.enetcapture.controller;

import com.enetcapture.model.Event;
import com.enetcapture.service.EventService;
import com.enetcapture.service.CustomArray;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet(urlPatterns = {"/events", "/events/*"})
public class EventServlet extends HttpServlet {
    private final EventService eventService = EventService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String path = req.getServletPath(), pathInfo = req.getPathInfo();
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/adminLogin"); return;
        }
        if ("/events".equals(path)) {
            if (pathInfo == null || "/".equals(pathInfo)) {
                CustomArray<Event> events = eventService.getAllEvents();
                req.setAttribute("events", events.toArray(new Event[0]));
                req.getRequestDispatcher("/WEB-INF/views/eventList.jsp").forward(req, res);
            } else if ("/add".equals(pathInfo)) {
                req.getRequestDispatcher("/WEB-INF/views/addEvent.jsp").forward(req, res);
            } else if ("/edit".equals(pathInfo)) {
                String idStr = req.getParameter("id");
                if (idStr == null) { res.sendError(HttpServletResponse.SC_BAD_REQUEST); return; }
                Event event = eventService.getEventById(Integer.parseInt(idStr));
                if (event == null) { res.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
                req.setAttribute("event", event);
                req.getRequestDispatcher("/WEB-INF/views/editEvent.jsp").forward(req, res);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String path = req.getServletPath();
        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/adminLogin");
            return;
        }

        if ("/events".equals(path)) {
            if ("/add".equals(pathInfo)) {
                handleAddEvent(req, res);
            } else if ("/edit".equals(pathInfo)) {
                handleUpdateEvent(req, res);
            } else if ("/delete".equals(pathInfo)) {
                handleDeleteEvent(req, res);
            } else {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleAddEvent(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String name = req.getParameter("name");
        String dateStr = req.getParameter("date");
        String location = req.getParameter("location");
        String description = req.getParameter("description");
        String budgetStr = req.getParameter("budget");
        String specialInstructions = req.getParameter("specialInstructions");
        String status = req.getParameter("status");
        String paymentStatus = req.getParameter("paymentStatus");

        if (name == null || dateStr == null || location == null || description == null ||
                budgetStr == null || specialInstructions == null || status == null || paymentStatus == null ||
                name.isEmpty() || dateStr.isEmpty() || location.isEmpty() || description.isEmpty() ||
                budgetStr.isEmpty() || specialInstructions.isEmpty() || status.isEmpty() || paymentStatus.isEmpty()) {
            req.setAttribute("error", "All fields required");
            req.getRequestDispatcher("/WEB-INF/views/addEvent.jsp").forward(req, res);
            return;
        }

        try {
            LocalDate date = LocalDate.parse(dateStr);
            double budget = Double.parseDouble(budgetStr);
            eventService.addEvent(name, date, location, description, budget, specialInstructions, status, paymentStatus);
            res.sendRedirect(req.getContextPath() + "/events");
        } catch (Exception e) {
            req.setAttribute("error", "Invalid input");
            req.getRequestDispatcher("/WEB-INF/views/addEvent.jsp").forward(req, res);
        }
    }

    private void handleUpdateEvent(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String name = req.getParameter("name");
        String dateStr = req.getParameter("date");
        String location = req.getParameter("location");
        String description = req.getParameter("description");
        String budgetStr = req.getParameter("budget");
        String specialInstructions = req.getParameter("specialInstructions");
        String status = req.getParameter("status");
        String paymentStatus = req.getParameter("paymentStatus");

        if (idStr == null || name == null || dateStr == null || location == null || description == null ||
                budgetStr == null || specialInstructions == null || status == null || paymentStatus == null ||
                idStr.isEmpty() || name.isEmpty() || dateStr.isEmpty() || location.isEmpty() ||
                description.isEmpty() || budgetStr.isEmpty() || specialInstructions.isEmpty() ||
                status.isEmpty() || paymentStatus.isEmpty()) {
            req.setAttribute("error", "All fields required");
            req.setAttribute("event", eventService.getEventById(idStr != null && !idStr.isEmpty() ? Integer.parseInt(idStr) : -1));
            req.getRequestDispatcher("/WEB-INF/views/editEvent.jsp").forward(req, res);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            LocalDate date = LocalDate.parse(dateStr);
            double budget = Double.parseDouble(budgetStr);
            eventService.updateEvent(id, name, date, location, description, budget, specialInstructions, status, paymentStatus);
            res.sendRedirect(req.getContextPath() + "/events");
        } catch (Exception e) {
            req.setAttribute("error", "Invalid input");
            req.setAttribute("event", eventService.getEventById(Integer.parseInt(idStr)));
            req.getRequestDispatcher("/WEB-INF/views/editEvent.jsp").forward(req, res);
        }
    }

    private void handleDeleteEvent(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Event ID is required");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            boolean deleted = eventService.deleteEvent(id);
            HttpSession session = req.getSession();
            if (deleted) {
                session.setAttribute("message", "Event successfully deleted");
            } else {
                session.setAttribute("error", "Event with ID " + id + " not found or could not be deleted");
            }
            res.sendRedirect(req.getContextPath() + "/events");
        } catch (NumberFormatException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid event ID format");
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Error deleting event: " + e.getMessage());
            res.sendRedirect(req.getContextPath() + "/events");
        }
    }
}