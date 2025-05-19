<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Manage Admins - EnetCapture</title>
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

<!-- Main Content -->
<section class="gradient-overlay py-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="bg-white bg-opacity-90 p-8 rounded-lg shadow-md">
            <h2 class="text-3xl font-bold text-gray-800 mb-6">Manage Admins</h2>
            <c:if test="${not empty error}">
                <p class="text-red-500 mb-4">${error}</p>
            </c:if>
            <a href="${pageContext.request.contextPath}/admins/add" class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition mb-6 inline-block">Add Admin</a>
            <c:choose>
                <c:when test="${not empty admins}">
                    <div class="overflow-x-auto">
                        <table class="w-full border-collapse">
                            <thead>
                            <tr class="bg-gray-100">
                                <th class="border border-gray-300 p-3 text-left">Username</th>
                                <th class="border border-gray-300 p-3 text-left">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="admin" items="${admins}">
                                <tr>
                                    <td class="border border-gray-300 p-3">${admin.username}</td>
                                    <td class="border border-gray-300 p-3">
                                        <a href="${pageContext.request.contextPath}/admins/edit?username=${admin.username}" class="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700">Edit</a>
                                        <a href="${pageContext.request.contextPath}/admins/delete?username=${admin.username}" class="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700" onclick="return confirm('Are you sure?')">Delete</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-gray-600">No admins found.</p>
                </c:otherwise>
            </c:choose>
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