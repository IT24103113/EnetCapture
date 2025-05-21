<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Sign Up - EnetCapture</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- AOS CSS -->
    <link href="https://cdn.jsdelivr.net/npm/aos@2.3.4/dist/aos.css" rel="stylesheet" />
</head>
<body class="font-sans text-gray-800" >

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

<!-- Signup Section -->
<section class="gradient-overlay py-16" data-aos="fade-up">
    <div class="max-w-md mx-auto px-4 sm:px-6 lg:px-8">
        <div class="bg-white bg-opacity-90 p-8 rounded-lg shadow-md" id="form-container">
            <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center" data-aos="zoom-in">Sign Up</h2>
            <% if (request.getAttribute("error") != null) { %>
            <p class="text-red-500 text-center mb-4"><%= request.getAttribute("error") %></p>
            <% } %>
            <form action="${pageContext.request.contextPath}/register" method="post">
                <div class="mb-4">
                    <label for="username" class="block text-gray-700 font-medium mb-2">Username</label>
                    <input type="text" id="username" name="username" placeholder="Username" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <div class="mb-4">
                    <label for="password" class="block text-gray-700 font-medium mb-2">Password</label>
                    <input type="password" id="password" name="password" placeholder="Password" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <div class="mb-4">
                    <label for="confirmPassword" class="block text-gray-700 font-medium mb-2">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm Password" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <div class="mb-4">
                    <label for="fullName" class="block text-gray-700 font-medium mb-2">Full Name</label>
                    <input type="text" id="fullName" name="fullName" placeholder="Full Name" required class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <div class="mb-4">
                    <label for="email" class="block text-gray-700 font-medium mb-2">Email</label>
                    <input type="email" id="email" name="email" placeholder="Email" class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <div class="mb-4">
                    <label for="phone" class="block text-gray-700 font-medium mb-2">Phone</label>
                    <input type="text" id="phone" name="phone" placeholder="Phone" class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
                </div>
                <button type="submit" class="w-full bg-blue-600 text-white p-3 rounded-lg hover:bg-blue-700 transition" id="submit-btn">Register</button>
            </form>
            <a href="${pageContext.request.contextPath}/" class="block text-center mt-4 text-blue-600 hover:underline">Back</a>
        </div>
    </div>
</section>

<!-- Footer -->
<footer class="bg-gray-800 text-white py-8" data-aos="fade-up">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <p>© 2025 EnetCapture. All rights reserved.</p>
    </div>
</footer>

<!-- AOS JS -->
<script src="https://cdn.jsdelivr.net/npm/aos@2.3.4/dist/aos.js"></script>
<!-- GSAP -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.12.2/gsap.min.js"></script>

<script>
    // Initialize AOS
    AOS.init({
        duration: 800,
        easing: 'ease-in-out',
        once: true,
    });

    // GSAP entrance animation for the form container
    gsap.from("#form-container", {
        opacity: 0,
        y: 50,
        duration: 1,
        ease: "power3.out"
    });

    // GSAP hover scale effect on the Register button
    const submitBtn = document.getElementById("submit-btn");
    submitBtn.addEventListener("mouseenter", () => {
        gsap.to(submitBtn, { scale: 1.05, duration: 0.3, ease: "power1.out" });
    });
    submitBtn.addEventListener("mouseleave", () => {
        gsap.to(submitBtn, { scale: 1, duration: 0.3, ease: "power1.out" });
    });
</script>

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
</body>
</html>