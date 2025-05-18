<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ page import="java.io.PrintWriter" %>
<html>
<head>
    <title>Error - EnetCapture</title>
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
                <a href="${pageContext.request.contextPath}/index.jsp" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Home</a>
                <a href="${pageContext.request.contextPath}/register" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Register</a>
                <a href="${pageContext.request.contextPath}/userLogin" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">User Login</a>
                <a href="${pageContext.request.contextPath}/adminLogin" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Admin Login</a>
            </div>
        </div>
    </div>
</nav>

<!-- Error Section -->
<section class="gradient-overlay py-16">
    <div class="max-w-md mx-auto px-4 sm:px-6 lg:px-8">
        <div class="bg-white bg-opacity-90 p-8 rounded-lg shadow-md text-center">
            <h2 class="text-2xl font-bold text-gray-800 mb-6">Error</h2>
            <p class="text-gray-700 mb-4">
                <strong>Status Code:</strong> <%= response.getStatus() %><br>
                <% if (exception != null) { %>
                <strong>Exception:</strong> <%= exception.getMessage() %><br>
                <% exception.printStackTrace(new PrintWriter(out)); %>
                <% } else { %>
                The requested page was not found or an unexpected error occurred.
                <% } %>
            </p>
            <a href="${pageContext.request.contextPath}/home" class="text-blue-600 hover:underline">Return to Home</a>
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