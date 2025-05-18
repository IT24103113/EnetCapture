<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit Booking - EnetCapture</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body {
            background-image: url('https://images.unsplash.com/photo-1501139083538-0139583c060f');
            background-size: cover;
            background-position: center;
            background-attachment: fixed;
        }
        .gradient-overlay {
            background: linear-gradient(to bottom, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.3));
        }
        .error-message {
            background-color: #fee2e2;
            border: 1px solid #ef4444;
            padding: 10px;
            border-radius: 5px;
        }
    </style>
</head>
<body class="font-sans text-gray-800">
<!-- Navigation -->
<nav class="bg-white shadow-lg sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
            <div class="flex items-center">
                <a href="${pageContext.request.contextPath}/index.jsp" class="text-2xl font-bold text-blue-600">EnetCapture</a>
            </div>
            <div class="flex items-center space-x-4">
                <a href="${pageContext.request.contextPath}/adminDashboard" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Dashboard</a>
                <a href="${pageContext.request.contextPath}/users" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Manage Users</a>
                <a href="${pageContext.request.contextPath}/admins" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Manage Admins</a>
                <a href="${pageContext.request.contextPath}/bookings" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Manage Bookings</a>
                <a href="${pageContext.request.contextPath}/photographers" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Manage Photographers</a>
                <a href="${pageContext.request.contextPath}/events" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Manage Events</a>
                <a href="${pageContext.request.contextPath}/admin/reviews" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Manage Reviews</a>
                <a href="${pageContext.request.contextPath}/logout" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Logout</a>
            </div>
        </div>
    </div>
</nav>

<!-- Form Section -->
<section class="gradient-overlay py-16">
    <div class="max-w-md mx-auto px-4 sm:px-6 lg:px-8">
        <div class="bg-white bg-opacity-90 p-8 rounded-lg shadow-md">
            <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Edit Booking</h2>
            <c:if test="${not empty error}">
                <p class="text-red-500 text-center mb-4 error-message">${error}</p>
            </c:if>
            <form action="${pageContext.request.contextPath}/bookings/edit" method="post" onsubmit="return validateForm()">
                <input type="hidden" name="id" value="${booking.id}">
                <div class="mb-4">
                    <label for="username" class="block text-gray-700 font-medium mb-2">Username</label>
                    <input type="text" id="username" name="username" value="${booking.username}" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600" readonly>
                </div>
                <div class="mb-4">
                    <label for="photographer" class="block text-gray-700 font-medium mb-2">Photographer</label>
                    <select id="photographer" name="photographer" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                        <option value="">Select a Photographer</option>
                        <c:forEach var="photographer" items="${photographers}">
                            <option value="${photographer.name}" <c:if test="${photographer.name eq booking.photographer}">selected</c:if>>${photographer.name} (Rating: ${photographer.rating})</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-4">
                    <label for="eventId" class="block text-gray-700 font-medium mb-2">Event</label>
                    <select id="eventId" name="eventId" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                        <option value="">Select an Event</option>
                        <c:forEach var="event" items="${events}">
                            <option value="${event.id}" <c:if test="${event.id eq booking.eventId}">selected</c:if>>${event.name} (ID: ${event.id})</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-4">
                    <label for="eventDate" class="block text-gray-700 font-medium mb-2">Event Date</label>
                    <input type="date" id="eventDate" name="eventDate" value="${booking.eventDate}" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <div class="mb-4">
                    <label for="eventType" class="block text-gray-700 font-medium mb-2">Event Type</label>
                    <input type="text" id="eventType" name="eventType" value="${booking.eventType}" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <button type="submit" class="w-full bg-blue-600 text-white p-3 rounded-lg hover:bg-blue-700 transition">Update Booking</button>
            </form>
            <a href="${pageContext.request.contextPath}/bookings" class="block text-center mt-4 text-blue-600 hover:underline">Back to Bookings</a>
        </div>
    </div>
</section>

<!-- Footer -->
<footer class="bg-gray-800 text-white py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <p>© 2025 EnetCapture. All rights reserved.</p>
    </div>
</footer>

<script>
    function validateForm() {
        const photographer = document.getElementById('photographer').value;
        const eventId = document.getElementById('eventId').value;
        const eventDate = document.getElementById('eventDate').value;
        const eventType = document.getElementById('eventType').value;

        if (!photographer) {
            alert('Please select a photographer.');
            return false;
        }
        if (!eventId) {
            alert('Please select an event.');
            return false;
        }
        if (!eventDate) {
            alert('Please select an event date.');
            return false;
        }
        if (!eventType) {
            alert('Please enter an event type.');
            return false;
        }
        try {
            const date = new Date(eventDate);
            if (isNaN(date.getTime())) {
                alert('Please enter a valid event date.');
                return false;
            }
        } catch (e) {
            alert('Invalid date format.');
            return false;
        }
        return true;
    }
</script>
</body>
</html>