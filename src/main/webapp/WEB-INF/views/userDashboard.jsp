<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Dashboard - EnetCapture</title>
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
                <a href="${pageContext.request.contextPath}/userDashboard" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Dashboard</a>
                <a href="${pageContext.request.contextPath}/bookings/new" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">New Booking</a>
                <a href="${pageContext.request.contextPath}/events" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Events</a>
                <a href="${pageContext.request.contextPath}/reviews" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Add Review</a>
                <a href="${pageContext.request.contextPath}/logout" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Logout</a>
            </div>
        </div>
    </div>
</nav>

<!-- Dashboard Section -->
<section class="gradient-overlay py-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="bg-white bg-opacity-90 p-8 rounded-lg shadow-md">
            <h2 class="text-2xl font-bold text-gray-800 mb-6">User Dashboard</h2>
            <c:if test="${not empty error}">
                <p class="text-red-500 mb-4">${error}</p>
            </c:if>

            <!-- Bookings Section -->
            <h3 class="text-lg font-medium text-gray-700 mb-4">Your Bookings</h3>
            <c:choose>
                <c:when test="${not empty bookings}">
                    <div class="overflow-x-auto mb-8">
                        <table class="min-w-full border border-gray-300">
                            <thead class="bg-gray-100">
                            <tr>
                                <th class="border-b px-4 py-2 text-left text-gray-700">ID</th>
                                <th class="border-b px-4 py-2 text-left text-gray-700">Photographer</th>
                                <th class="border-b px-4 py-2 text-left text-gray-700">Event Date</th>
                                <th class="border-b px-4 py-2 text-left text-gray-700">Event Type</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="booking" items="${bookings}">
                                <tr class="hover:bg-gray-50">
                                    <td class="border-b px-4 py-2">${booking.id}</td>
                                    <td class="border-b px-4 py-2">${booking.photographer}</td>
                                    <td class="border-b px-4 py-2">${booking.eventDate}</td>
                                    <td class="border-b px-4 py-2">${booking.eventType}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-gray-700 mb-8">No bookings found.</p>
                </c:otherwise>
            </c:choose>

            <!-- Add Booking Section -->
            <h3 class="text-lg font-medium text-gray-700 mb-4">Add New Booking</h3>
            <form action="${pageContext.request.contextPath}/userDashboard/addBooking" method="post" class="mb-8">
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <div>
                        <label for="booking_photographer" class="block text-gray-700 font-medium mb-2">Photographer (Sorted by Rating)</label>
                        <select id="booking_photographer" name="photographer" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                            <c:forEach var="photographer" items="${photographers}">
                                <option value="${photographer.name}">${photographer.name} (${photographer.rating})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div>
                        <label for="booking_eventId" class="block text-gray-700 font-medium mb-2">Event</label>
                        <select id="booking_eventId" name="eventId" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                            <c:forEach var="event" items="${events}">
                                <option value="${event.id}">${event.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div>
                        <label for="booking_eventDate" class="block text-gray-700 font-medium mb-2">Event Date</label>
                        <input type="date" id="booking_eventDate" name="eventDate" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                    </div>
                    <div>
                        <label for="booking_eventType" class="block text-gray-700 font-medium mb-2">Event Type</label>
                        <input type="text" id="booking_eventType" name="eventType" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                    </div>
                </div>
                <button type="submit" class="mt-4 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition">Add Booking</button>
            </form>

            <!-- Reviews Section -->
            <h3 class="text-lg font-medium text-gray-700 mb-4">Manage Reviews</h3>
            <c:choose>
                <c:when test="${not empty reviews}">
                    <div class="overflow-x-auto mb-8">
                        <table class="min-w-full border border-gray-300">
                            <thead class="bg-gray-100">
                            <tr>
                                <th class="border-b px-4 py-2 text-left text-gray-700">ID</th>
                                <th class="border-b px-4 py-2 text-left text-gray-700">Photographer</th>
                                <th class="border-b px-4 py-2 text-left text-gray-700">Rating</th>
                                <th class="border-b px-4 py-2 text-left text-gray-700">Comment</th>
                                <th class="border-b px-4 py-2 text-left text-gray-700">Date</th>
                                <th class="border-b px-4 py-2 text-left text-gray-700">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="review" items="${reviews}">
                                <tr class="hover:bg-gray-50">
                                    <td class="border-b px-4 py-2">${review.id}</td>
                                    <td class="border-b px-4 py-2">${review.photographer}</td>
                                    <td class="border-b px-4 py-2">${review.rating}</td>
                                    <td class="border-b px-4 py-2">${review.comment}</td>
                                    <td class="border-b px-4 py-2">${review.date}</td>
                                    <td class="border-b px-4 py-2">
                                        <a href="${pageContext.request.contextPath}/reviews/edit?id=${review.id}" class="bg-blue-600 text-white px-3 py-1 rounded-lg hover:bg-blue-700 transition">Edit</a>
                                        <a href="${pageContext.request.contextPath}/reviews/delete?id=${review.id}" class="bg-red-600 text-white px-3 py-1 rounded-lg hover:bg-red-700 transition" onclick="return confirm('Are you sure?')">Delete</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-gray-700 mb-8">No reviews found.</p>
                </c:otherwise>
            </c:choose>
            <a href="${pageContext.request.contextPath}/reviews/add" class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition">Add New Review</a>
        </div>
    </div>
</section>

<!-- Footer -->
<footer class="bg-gray-800 text-white py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <p>© 2025 EnetCapture. All rights reserved.</p>
    </div>
</footer>
</body>
</html>