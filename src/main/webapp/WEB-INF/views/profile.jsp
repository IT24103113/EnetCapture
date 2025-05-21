<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Profile - EnetCapture</title>
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

<!-- Profile Section -->
<section class="gradient-overlay py-16">
    <div class="max-w-md mx-auto px-4 sm:px-6 lg:px-8">
        <div class="bg-white bg-opacity-90 p-8 rounded-lg shadow-md">
            <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">User Profile</h2>
            <% if (request.getAttribute("error") != null) { %>
            <p class="text-red-500 text-center mb-4"><%= request.getAttribute("error") %></p>
            <% } %>
            <div class="space-y-4">
                <p><strong class="text-gray-700">Username:</strong> <%= ((com.enetcapture.model.User) request.getAttribute("user")).getUsername() %></p>
                <p><strong class="text-gray-700">Full Name:</strong> <%= ((com.enetcapture.model.User) request.getAttribute("user")).getFullName() %></p>
                <p><strong class="text-gray-700">Email:</strong> <%= ((com.enetcapture.model.User) request.getAttribute("user")).getEmail() %></p>
                <p><strong class="text-gray-700">Phone:</strong> <%= ((com.enetcapture.model.User) request.getAttribute("user")).getPhone() %></p>
            </div>
            <div class="mt-6 space-y-4">
                <a href="${pageContext.request.contextPath}/profile/edit" class="block text-center bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition">Edit Profile</a>
                <form action="${pageContext.request.contextPath}/profile/delete" method="post">
                    <button type="submit" class="w-full bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition" onclick="return confirm('Are you sure you want to delete your account?')">Delete Account</button>
                </form>
                <a href="${pageContext.request.contextPath}/logout" class="block text-center text-blue-600 hover:underline">Logout</a>
            </div>
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