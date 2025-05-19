<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit Review - EnetCapture</title>
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
                <c:choose>
                    <c:when test="${sessionScope.userType eq 'admin'}">
                        <a href="${pageContext.request.contextPath}/adminDashboard" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Dashboard</a>
                        <a href="${pageContext.request.contextPath}/users" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Manage Users</a>
                        <a href="${pageContext.request.contextPath}/admins" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Manage Admins</a>
                        <a href="${pageContext.request.contextPath}/bookings" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Manage Bookings</a>
                        <a href="${pageContext.request.contextPath}/photographers" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Manage Photographers</a>
                        <a href="${pageContext.request.contextPath}/events" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Manage Events</a>
                        <a href="${pageContext.request.contextPath}/admin/reviews" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Manage Reviews</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/userDashboard" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Dashboard</a>
                        <a href="${pageContext.request.contextPath}/bookings/new" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">New Booking</a>
                        <a href="${pageContext.request.contextPath}/events" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Events</a>
                        <a href="${pageContext.request.contextPath}/reviews" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Add Review</a>
                    </c:otherwise>
                </c:choose>
                <a href="${pageContext.request.contextPath}/logout" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Logout</a>
            </div>
        </div>
    </div>
</nav>

<!-- Form Section -->
<section class="gradient-overlay py-16">
    <div class="max-w-md mx-auto px-4 sm:px-6 lg:px-8">
        <div class="bg-white bg-opacity-90 p-8 rounded-lg shadow-md">
            <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Edit Review</h2>
            <c:if test="${not empty error}">
                <p class="text-red-500 text-center mb-4">${error}</p>
            </c:if>
            <form action="${pageContext.request.contextPath}${sessionScope.userType eq 'admin' ? '/admin/reviews/edit' : '/reviews/edit'}" method="post">
                <input type="hidden" name="id" value="${review.id}">
                <div class="mb-4">
                    <label for="photographer" class="block text-gray-700 font-medium mb-2">Photographer</label>
                    <select id="photographer" name="photographer" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                        <c:forEach var="photographer" items="${photographers}">
                            <option value="${photographer.name}" ${photographer.name eq review.photographer ? 'selected' : ''}>${photographer.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-4">
                    <label for="rating" class="block text-gray-700 font-medium mb-2">Rating (0-5)</label>
                    <input type="number" id="rating" name="rating" step="0.1" min="0" max="5" value="${review.rating}" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <div class="mb-4">
                    <label for="comment" class="block text-gray-700 font-medium mb-2">Comment</label>
                    <textarea id="comment" name="comment" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">${review.comment}</textarea>
                </div>
                <div class="mb-4">
                    <label for="eventId" class="block text-gray-700 font-medium mb-2">Event ID</label>
                    <input type="number" id="eventId" name="eventId" value="${review.eventId}" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <button type="submit" class="w-full bg-blue-600 text-white p-3 rounded-lg hover:bg-blue-700 transition">Update Review</button>
            </form>
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