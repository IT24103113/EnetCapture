<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Photographers - EnetCapture</title>
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
        .admin-only { display: ${sessionScope.userType == 'admin' ? 'block' : 'none'}; }
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
                    <c:when test="${sessionScope.userType == 'admin'}">
                        <a href="${pageContext.request.contextPath}/adminDashboard" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Dashboard</a>
                        <a href="${pageContext.request.contextPath}/users" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Manage Users</a>
                        <a href="${pageContext.request.contextPath}/bookings" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Manage Bookings</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/userDashboard" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Dashboard</a>
                        <a href="${pageContext.request.contextPath}/bookings/new" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">New Booking</a>
                        <a href="${pageContext.request.contextPath}/events" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Events</a>
                        <a href="${pageContext.request.contextPath}/reviews" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Add Review</a>
                    </c:otherwise>
                </c:choose>
                <a href="${pageContext.request.contextPath}/photographers" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Photographers</a>
                <a href="${pageContext.request.contextPath}/logout" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md">Logout</a>
            </div>
        </div>
    </div>
</nav>

<!-- Photographers Section -->
<section class="gradient-overlay py-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="bg-white bg-opacity-90 p-8 rounded-lg shadow-md">
            <h2 class="text-2xl font-bold text-gray-800 mb-6">Photographers (Sorted by Rating)</h2>
            <c:if test="${not empty error}">
                <p class="text-red-500 mb-4">${error}</p>
            </c:if>
            <div class="overflow-x-auto">
                <table class="min-w-full border border-gray-300">
                    <thead class="bg-gray-100">
                    <tr>
                        <th class="border-b px-4 py-2 text-left text-gray-700">Name</th>
                        <th class="border-b px-4 py-2 text-left text-gray-700">Rating</th>
                        <th class="border-b px-4 py-2 text-left text-gray-700 admin-only">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="photographer" items="${photographers}">
                        <tr class="hover:bg-gray-50">
                            <td class="border-b px-4 py-2">${photographer.name}</td>
                            <td class="border-b px-4 py-2">${photographer.rating}</td>
                            <td class="border-b px-4 py-2 admin-only">
                                <a href="${pageContext.request.contextPath}/photographers/edit?name=${photographer.name}" class="bg-blue-600 text-white px-3 py-1 rounded-lg hover:bg-blue-700 transition">Edit</a>
                                <a href="${pageContext.request.contextPath}/photographers/delete?name=${photographer.name}" class="bg-red-600 text-white px-3 py-1 rounded-lg hover:bg-red-700 transition" onclick="return confirm('Are you sure?')">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="mt-6 admin-only">
                <a href="${pageContext.request.contextPath}/photographers/add" class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition">Add Photographer</a>
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