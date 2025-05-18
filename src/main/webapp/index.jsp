<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>EnetCapture - Home</title>

    <!-- Tailwind CSS -->
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

        /* Loading Overlay */
        #loading-overlay {
            position: fixed;
            top: 0; left: 0;
            width: 100vw;
            height: 100vh;
            background: rgba(0,0,0,0.75);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 9999;
            transition: opacity 0.3s ease;
        }
        #loading-overlay.hidden {
            opacity: 0;
            pointer-events: none;
        }
        .spinner {
            border: 6px solid #ccc;
            border-top: 6px solid #3b82f6; /* Tailwind blue-500 */
            border-radius: 50%;
            width: 50px;
            height: 50px;
            animation: spin 1s linear infinite;
        }
        @keyframes spin {
            0% { transform: rotate(0deg);}
            100% { transform: rotate(360deg);}
        }

        /* Button Hover Effects */
        a.bg-blue-600:hover,
        a.bg-gray-600:hover,
        a.bg-green-600:hover {
            transform: scale(1.05);
            transition: transform 0.3s ease, background-color 0.3s ease;
        }

        /* Modal Styles */
        #modal {
            position: fixed;
            top: 0; left: 0; right: 0; bottom: 0;
            background: rgba(0,0,0,0.7);
            display: none;
            justify-content: center;
            align-items: center;
            z-index: 10000;
            animation: fadeInModal 0.3s ease forwards;
        }
        #modal.active {
            display: flex;
        }
        #modal-content {
            background: white;
            padding: 2rem;
            border-radius: 0.5rem;
            max-width: 400px;
            width: 90%;
            position: relative;
            box-shadow: 0 0 20px rgba(0,0,0,0.25);
            animation: slideDown 0.4s ease forwards;
        }
        #modal-close {
            position: absolute;
            top: 0.5rem;
            right: 0.75rem;
            font-weight: bold;
            font-size: 1.5rem;
            cursor: pointer;
            color: #ef4444; /* Tailwind red-500 */
        }
        @keyframes fadeInModal {
            from {opacity: 0;}
            to {opacity: 1;}
        }
        @keyframes slideDown {
            from {transform: translateY(-20px); opacity: 0;}
            to {transform: translateY(0); opacity: 1;}
        }
    </style>
</head>
<body class="font-sans text-gray-800">

<!-- Loading Overlay -->
<div id="loading-overlay">
    <div class="spinner"></div>
</div>

<!-- Navigation -->
<nav class="bg-white shadow-lg sticky top-0 z-50" data-aos="fade-down">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
            <div class="flex items-center">
                <a href="<%= request.getContextPath() %>/index.jsp" class="text-2xl font-bold text-blue-600">EnetCapture</a>
            </div>
            <div class="flex items-center space-x-4">
                <a href="<%= request.getContextPath() %>/index.jsp" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md transition">Home</a>
                <a href="<%= request.getContextPath() %>/register" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md transition">Register</a>
                <a href="<%= request.getContextPath() %>/userLogin" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md transition">User Login</a>
                <a href="<%= request.getContextPath() %>/adminLogin" class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md transition">Admin Login</a>
            </div>
        </div>
    </div>
</nav>

<!-- Hero Section -->
<section class="gradient-overlay text-white py-20" data-aos="fade-up" data-aos-duration="1000">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <h1 class="text-4xl md:text-5xl font-bold mb-4">Welcome to EnetCapture</h1>
        <p class="text-lg md:text-xl mb-8">Manage your data with ease and security.</p>
        <div class="flex justify-center space-x-4">
            <a href="<%= request.getContextPath() %>/userLogin" class="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition">Log In as User</a>
            <a href="<%= request.getContextPath() %>/adminLogin" class="bg-gray-600 text-white px-6 py-3 rounded-lg hover:bg-gray-700 transition">Log In as Admin</a>
        </div>
        <div class="mt-6">
            <button id="open-modal-btn" class="bg-green-600 text-white px-6 py-3 rounded-lg hover:bg-green-700 transition">Book Now</button>
        </div>
    </div>
</section>

<!-- Sample Projects Section -->
<section id="projects" class="py-16 bg-white" data-aos="fade-up" data-aos-duration="1000">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl font-bold text-center mb-12">Sample Projects</h2>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div class="bg-gray-100 p-6 rounded-lg shadow-md" data-aos="zoom-in" data-aos-delay="100">
                <h3 class="text-xl font-semibold mb-2">Data Migration Tool</h3>
                <p class="text-gray-600">Streamlined data transfer for 500+ users.</p>
            </div>
            <div class="bg-gray-100 p-6 rounded-lg shadow-md" data-aos="zoom-in" data-aos-delay="200">
                <h3 class="text-xl font-semibold mb-2">Security Audit System</h3>
                <p class="text-gray-600">Enhanced security for 10+ enterprises.</p>
            </div>
            <div class="bg-gray-100 p-6 rounded-lg shadow-md" data-aos="zoom-in" data-aos-delay="300">
                <h3 class="text-xl font-semibold mb-2">User Management App</h3>
                <p class="text-gray-600">Simplified user access control.</p>
            </div>
        </div>
    </div>
</section>

<!-- Customer Reviews Section -->
<section class="py-16 bg-gray-100" data-aos="fade-up" data-aos-duration="1000">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl font-bold text-center mb-12">Customer Reviews</h2>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div class="bg-white p-6 rounded-lg shadow-md" data-aos="fade-right" data-aos-delay="100">
                <p class="text-gray-600 italic">"Great service! The data migration was seamless."</p>
                <p class="mt-4 font-semibold text-blue-600">- John D.</p>
            </div>
            <div class="bg-white p-6 rounded-lg shadow-md" data-aos="fade-up" data-aos-delay="200">
                <p class="text-gray-600 italic">"Highly recommend for security solutions."</p>
                <p class="mt-4 font-semibold text-blue-600">- Sarah K.</p>
            </div>
            <div class="bg-white p-6 rounded-lg shadow-md" data-aos="fade-left" data-aos-delay="300">
                <p class="text-gray-600 italic">"User-friendly and efficient tool!"</p>
                <p class="mt-4 font-semibold text-blue-600">- Mike R.</p>
            </div>
        </div>
        <div class="text-center mt-8">
            <a href="#reviews" class="text-blue-600 hover:underline">View All Reviews</a>
        </div>
    </div>
</section>

<!-- Modal -->
<div id="modal" role="dialog" aria-modal="true" aria-labelledby="modal-title" class="fixed inset-0 bg-black bg-opacity-70 hidden justify-center items-center z-50">
    <div id="modal-content" class="bg-white rounded-lg p-8 max-w-md w-full relative">
        <button id="modal-close" aria-label="Close modal" class="absolute top-4 right-4 text-red-600 font-bold text-2xl">&times;</button>
        <h2 id="modal-title" class="text-2xl font-bold mb-4">Book Now</h2>
        <p class="mb-6">Thank you for your interest! Please contact us at <a href="mailto:contact@enetcapture.com" class="text-blue-600 underline">contact@enetcapture.com</a>.</p>
        <button id="modal-submit-btn" class="bg-green-600 text-white px-6 py-3 rounded-lg hover:bg-green-700 transition">Close</button>
    </div>
</div>

<!-- Footer -->
<footer class="bg-gray-800 text-white py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <p>© 2025 EnetCapture. All rights reserved.</p>
    </div>
</footer>

<!-- AOS JS -->
<script src="https://cdn.jsdelivr.net/npm/aos@2.3.4/dist/aos.js"></script>

<script>
    AOS.init();

    // Hide loading overlay after page load
    window.addEventListener('load', () => {
        document.getElementById('loading-overlay').classList.add('hidden');
    });

    // Modal controls
    const modal = document.getElementById('modal');
    const openModalBtn = document.getElementById('open-modal-btn');
    const closeModalBtn = document.getElementById('modal-close');
    const modalSubmitBtn = document.getElementById('modal-submit-btn');

    openModalBtn.addEventListener('click', e => {
        e.preventDefault();
        modal.classList.remove('hidden');
        modal.classList.add('flex');
    });

    function closeModal() {
        modal.classList.add('hidden');
        modal.classList.remove('flex');
    }

    closeModalBtn.addEventListener('click', closeModal);
    modalSubmitBtn.addEventListener('click', closeModal);

    window.addEventListener('click', e => {
        if (e.target === modal) {
            closeModal();
        }
    });
</script>

</body>
</html>