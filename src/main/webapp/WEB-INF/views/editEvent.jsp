<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit Event - EnetCapture</title>
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
            <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Edit Event</h2>
            <c:if test="${not empty error}">
                <p class="text-red-500 text-center mb-4">${error}</p>
            </c:if>
            <form action="${pageContext.request.contextPath}/events/edit" method="post">
                <input type="hidden" name="id" value="${event.id}">
                <div class="mb-4">
                    <label for="name" class="block text-gray-700 font-medium mb-2">Name</label>
                    <input type="text" id="name" name="name" value="${event.name}" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <div class="mb-4">
                    <label for="date" class="block text-gray-700 font-medium mb-2">Date</label>
                    <input type="date" id="date" name="date" value="${event.date}" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <div class="mb-4">
                    <label for="location" class="block text-gray-700 font-medium mb-2">Location</label>
                    <input type="text" id="location" name="location" value="${event.location}" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <div class="mb-4">
                    <label for="description" class="block text-gray-700 font-medium mb-2">Description</label>
                    <textarea id="description" name="description" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">${event.description}</textarea>
                </div>
                <div class="mb-4">
                    <label for="budget" class="block text-gray-700 font-medium mb-2">Budget</label>
                    <input type="number" step="0.01" id="budget" name="budget" value="${event.budget}" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <div class="mb-4">
                    <label for="specialInstructions" class="block text-gray-700 font-medium mb-2">Special Instructions</label>
                    <textarea id="specialInstructions" name="specialInstructions" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">${event.specialInstructions}</textarea>
                </div>
                <div class="mb-4">
                    <label for="status" class="block text-gray-700 font-medium mb-2">Status</label>
                    <select id="status" name="status" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                        <option value="Pending" ${event.status == 'Pending' ? 'selected' : ''}>Pending</option>
                        <option value="Confirmed" ${event.status == 'Confirmed' ? 'selected' : ''}>Confirmed</option>
                        <option value="In Progress" ${event.status == 'In Progress' ? 'selected' : ''}>In Progress</option>
                        <option value="Completed" ${event.status == 'Completed' ? 'selected' : ''}>Completed</option>
                        <option value="Cancelled" ${event.status == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                    </select>
                </div>
                <div class="mb-4">
                    <label for="paymentStatus" class="block text-gray-700 font-medium mb-2">Payment Status</label>
                    <select id="paymentStatus" name="paymentStatus" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                        <option value="Pending" ${event.paymentStatus == 'Pending' ? 'selected' : ''}>Pending</option>
                        <option value="Partial" ${event.paymentStatus == 'Partial' ? 'selected' : ''}>Partial</option>
                        <option value="Paid" ${event.paymentStatus == 'Paid' ? 'selected' : ''}>Paid</option>
                        <option value="Refunded" ${event.paymentStatus == 'Refunded' ? 'selected' : ''}>Refunded</option>
                    </select>
                </div>
                <button type="submit" class="w-full bg-blue-600 text-white p-3 rounded-lg hover:bg-blue-700 transition">Update Event</button>
            </form>
            <a href="${pageContext.request.contextPath}/events" class="block text-center mt-4 text-blue-600 hover:underline">Back to Events</a>
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
</html> editevent.jsp