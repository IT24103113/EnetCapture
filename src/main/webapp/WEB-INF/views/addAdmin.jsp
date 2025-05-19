<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Add Admin - EnetCapture</title>
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
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Dashboard</a>
                <a href="${pageContext.request.contextPath}/logout" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Logout</a>
            </div>
        </div>
    </div>
</nav>

<!-- Form Section -->
<section class="gradient-overlay py-16">
    <div class="max-w-md mx-auto px-4 sm:px-6 lg:px-8">
        <div class="bg-white bg-opacity-90 p-8 rounded-lg shadow-md">
            <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Add Admin</h2>
            <c:if test="${not empty error}">
                <p class="text-red-500 text-center mb-4">${error}</p>
            </c:if>
            <form action="${pageContext.request.contextPath}/admin/add" method="post">
                <div class="mb-4">
                    <label for="username" class="block text-gray-700 font-medium mb-2">Username</label>
                    <input type="text" id="username" name="username" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <div class="mb-6">
                    <label for="password" class="block text-gray-700 font-medium mb-2">Password</label>
                    <input type="password" id="password" name="password" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <button type="submit" class="w-full bg-blue-600 text-white p-3 rounded-lg hover:bg-blue-700 transition">Add Admin</button>
            </form>
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="block text-center mt-4 text-blue-600 hover:underline">Back to Dashboard</a>
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
