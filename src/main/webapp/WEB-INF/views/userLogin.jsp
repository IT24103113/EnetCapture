<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>User Login - EnetCapture</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <script src="https://cdn.tailwindcss.com"></script>

    <!-- AOS CSS -->
    <link href="https://cdn.jsdelivr.net/npm/aos@2.3.4/dist/aos.css" rel="stylesheet" />

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

        /* Pop scale + fade animation */
        @keyframes popScale {
            0% {
                opacity: 0;
                transform: scale(0.8);
            }
            60% {
                opacity: 1;
                transform: scale(1.05);
            }
            100% {
                transform: scale(1);
            }
        }
        .pop-animation {
            animation: popScale 0.6s ease forwards;
        }
    </style>
</head>
<body class="font-sans text-gray-800">

<!-- Navigation -->
<nav class="bg-white shadow-lg sticky top-0 z-50" data-aos="fade-down">
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

<!-- Login Section -->
<section class="gradient-overlay py-16" data-aos="fade-up">
    <div class="max-w-md mx-auto px-4 sm:px-6 lg:px-8">
        <div class="bg-white bg-opacity-90 p-8 rounded-lg shadow-md pop-animation">
            <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">User Login</h2>
            <% if (request.getAttribute("error") != null) { %>
            <p class="text-red-500 text-center mb-4"><%= request.getAttribute("error") %></p>
            <% } %>
            <form action="${pageContext.request.contextPath}/userLogin" method="post">
                <div class="mb-4">
                    <label for="username" class="block text-gray-700 font-medium mb-2">Username</label>
                    <input type="text" id="username" name="username" placeholder="Username" required
                           class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600" />
                </div>
                <div class="mb-4">
                    <label for="password" class="block text-gray-700 font-medium mb-2">Password</label>
                    <input type="password" id="password" name="password" placeholder="Password" required
                           class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600" />
                </div>
                <button type="submit" class="w-full bg-blue-600 text-white p-3 rounded-lg hover:bg-blue-700 transition">Log In</button>
            </form>
            <a href="${pageContext.request.contextPath}/register" class="block text-center mt-4 text-blue-600 hover:underline">Register</a>
        </div>
    </div>
</section>

<!-- Footer -->
<footer class="bg-gray-800 text-white py-8" data-aos="fade-up" data-aos-delay="300">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <p>© 2025 EnetCapture. All rights reserved.</p>
    </div>
</footer>

<!-- AOS JS -->
<script src="https://cdn.jsdelivr.net/npm/aos@2.3.4/dist/aos.js"></script>
<script>
    AOS.init({
        duration: 700,
        easing: 'ease-out-back',
        once: true,
    });
</script>

</body>
</html>
