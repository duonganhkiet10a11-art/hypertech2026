<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="css/header.css">
<link rel="stylesheet" href="css/home.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"><!-- comment -->
<style>
    /* 🔴 lỗi text */
    #loginError {
        color: red;
        font-size: 14px;
        margin-top: 5px;
        display: block;
    }

    /* 🔴 input lỗi */
    .input-error {
        border: 2px solid red !important;
        background-color: #fff5f5;
    }

    /* 💥 rung */
    .shake {
        animation: shake 0.3s;
    }

    @keyframes shake {
        0% {
            transform: translateX(0);
        }
        25% {
            transform: translateX(-6px);
        }
        50% {
            transform: translateX(6px);
        }
        75% {
            transform: translateX(-6px);
        }
        100% {
            transform: translateX(0);
        }
    }

    /* ⏳ loading */
    .loading {
        opacity: 0.6;
        pointer-events: none;
    }
</style>

<!-- Banner -->
<div class="top-banner"></div>

<!-- Header -->
<header class="main-header">
    <a href="home.jsp" class="logo">TKT</a>

    <button class="menu-btn">
        <i class="fa-solid fa-bars"></i> Danh mục
    </button>

    <div class="search-box">
        <input type="text" placeholder="Bạn cần tìm gì?">
        <button><i class="fa-solid fa-magnifying-glass"></i></button>
    </div>
    <div class="header-right">
        <a href="#" class="item">
            <i class="fa-solid fa-headset"></i>
            <div>Hotline<br><b>1900.5301</b></div>
        </a>

        <a href="#" class="item">
            <i class="fa-solid fa-location-dot"></i>
            <div>Hệ Thống<br>Showroom</div>
        </a>

        <a href="cart.jsp" class="item">
            <i class="fa-solid fa-cart-shopping"></i>

            <%
                model.CartDTO cart = (model.CartDTO) session.getAttribute("CART");

                int count = 0;

                if (cart != null && cart.getCart() != null) {
                    count = cart.getCart().size();
                }
            %>

            <div>Giỏ hàng (<%=count%>)</div>

        </a>

        <a href="order-lookup.jsp" class="item">
            <i class="fa-solid fa-magnifying-glass"></i>
            <div>Tra cứu<br>đơn hàng</div>
        </a>

        <div class="item account">
            <i class="fa-solid fa-user"></i>

            <%
                Object user = session.getAttribute("user");
                if (user != null) {
            %>

            <div id="userMenu" style="cursor:pointer;">
                <%= ((model.UserDTO) user).getUsername()%>
            </div>
            <div id="dropdownMenu" class="account-dropdown">

                <!-- Greeting -->
                <div class="account-top">
                    <span class="wave">👋</span>
                    <span>Xin chào, <strong><%= ((model.UserDTO) user).getUsername()%></strong></span>
                </div>

                <div class="account-divider"></div>

                <!-- Logout -->
                <form action="MainController" method="post">
                    <input type="hidden" name="action" value="Userlogout">
                    <button type="submit" class="logout-btn">
                        <i class="fa-solid fa-right-from-bracket"></i>
                        Đăng xuất
                    </button>
                </form>

            </div>

            <%
            } else {
            %>
            <div id="openLoginBtn" style="cursor:pointer;">
                Đăng nhập
            </div>

            <%
                }
            %>

            <div class="account-divider"></div>



        </div>
    </div>

</header>

<div class="sub-menu">
    <a href="#">Mua PC tặng màn 240Hz</a>
    <a href="#">Hot Deal</a>
    <a href="#">Tin tức</a>
    <a href="#">Dịch vụ kỹ thuật</a>
    <a href="#">Thu cũ đổi mới</a>
    <a href="#">Tra cứu bảo hành</a>
</div>

<!-- ================= MAIN LAYOUT ================= -->

<div class="main-layout">

    <!-- SIDEBAR -->
    <div class="sidebar">
        <ul class="category-list">
            <li><a href="MainController?action=searchProductLaptop"><span>Laptop</span><span class="arrow">›</span></a></li>
            <li><a href="MainController?action=searchProductMonitor"></span>Màn hình<span class="arrow">›</span></a></li>
            <li><a href="MainController?action=searchProductKeyboard"></span>Bàn phím<span class="arrow">›</span></a></li>
            <li><a href="MainController?action=searchProductMouse"></span>Chuột<span class="arrow">›</span></a></li>
        </ul>
    </div>

    <!-- CONTENT -->
    <div class="content">

        <!-- Banner khu vực -->
        <div class="home-wrapper">

            <div class="home-left">
                <div class="banner-slider">

                    <a href="li-xi-tet-2026.jsp">
                        <img class="banner-main slide active" src="images/madaothanhcong.png">
                    </a>

                    <a href="ProductController?action=monitor">
                        <img class="banner-main slide" src="images/bannermh1.jpg">
                    </a>

                    <!-- thanh ngang -->
                    <div class="slider-nav">
                        <span class="nav-dot active" data-index="0"></span>
                        <span class="nav-dot" data-index="1"></span>
                    </div>

                </div>

                <div class="banner-row">
                    <a href="ProductController?action=list">
                        <img class="banner-small" src="images/laptopgaming.jpg">
                    </a>
                    <a href="MainController?action=searchProductMouse">
                        <img class="banner-small" src="images/chuotgaming.jpg">
                    </a>
                </div>
            </div>

            <div class="home-middle">
                <a href="ProductController?action=keyboard">
                    <img class="banner-keyboard" src="images/keyboard_new.jpg">
                </a>
            </div>

            <!-- Fixed RTX -->
            <div class="rtx-side">
                <a href="LaptopRTX.jsp">
                    <img src="images/laptoprtx.jpg" alt="Laptop RTX">
                </a>
            </div>

        </div>

        <!-- ================= SẢN PHẨM ĐÃ XEM ================= -->
        <section class="section-viewed">
            <div id="section_viewed">

                <div class="section-heading">
                    <h2>Sản phẩm đã xem</h2>
                </div>

                <div id="viewed-products">
                </div>

            </div>
        </section>

    </div>
</div>



<script>
    window.addEventListener("load", function () {

        const banner = document.querySelector(".fixed-rtx");
        const topBanner = document.querySelector(".top-banner");
        const mainHeader = document.querySelector(".main-header");
        const subMenu = document.querySelector(".sub-menu");
        const spacing = 60;
        // Tính vị trí ban đầu
        const baseTop =
                topBanner.offsetHeight +
                mainHeader.offsetHeight +
                subMenu.offsetHeight +
                spacing;
        // Set vị trí ban đầu
        banner.style.top = baseTop + "px";
        // Scroll effect
        window.addEventListener("scroll", function () {
            const offset = window.scrollY * 0.2; // tốc độ trôi
            banner.style.top = (baseTop + offset) + "px";
        });
    });</script>

<div class="login-modal" id="loginModal">
    <div class="login-box">

        <!-- HEADER -->
        <div class="login-header">
            <h2>ĐĂNG NHẬP HOẶC TẠO TÀI KHOẢN</h2>
            <span class="close-btn" id="closeModal" style="cursor:pointer;">&times;</span>
        </div>

        <!-- FORM -->
        <form id="loginForm">
            <input type="hidden" name="action" value="login">

            <div class="login-body">
                <input id="emailInput" type="text" placeholder="Email" name="txtUsername">
                <input id="passInput" type="password" placeholder="Mật khẩu" name="txtPassword">

                <!-- 🔴 ERROR HIỆN NGAY -->
                <span id="loginError" style="color:red"></span>

                <div class="forgot">
                    <a href="#" id="openForgot">Quên mật khẩu?</a>
                </div>

                <button id="loginBtn" class="login-submit">ĐĂNG NHẬP</button>
            </div>
        </form>
        <!-- DIVIDER -->
        <div class="divider">
            <span>Hoặc đăng nhập bằng</span>
        </div>

        <!-- SOCIAL LOGIN -->
        <div class="social-login">
            <button class="google-btn">
                <i class="fa-brands fa-google"></i>
                <span>Google</span>
            </button>

            <button class="facebook-btn">
                <i class="fa-brands fa-facebook-f"></i>
                <span>Facebook</span>
            </button>
        </div>
        <!-- FOOTER -->
        <div class="login-footer">
            Bạn chưa có tài khoản?
            <a href="#" id="openRegister">Đăng ký ngay!</a>
        </div>
    </div> <!-- login-box -->
</div>
<div class="login-modal" id="forgotModal">
    <div class="login-box">

        <div class="login-header">
            <h2>QUÊN MẬT KHẨU</h2>
            <span class="close-btn" id="closeForgot">&times;</span>
        </div>

        <form action="MainController" method="post">
            <input type="hidden" name="action" value="forgotPassword">

            <div class="login-body">
                <input type="email" name="email" placeholder="Nhập email của bạn" required>
                <button class="login-submit">GỬI MẬT KHẨU MỚI</button>
            </div>
        </form>

    </div>
</div>             
<div class="login-modal" id="registerModal">
    <div class="login-box">

        <!-- HEADER -->
        <div class="login-header">
            <h2>ĐĂNG KÝ TÀI KHOẢN</h2>
            <span class="close-btn" id="closeRegister">&times;</span>
        </div>

        <!-- LINK SĐT -->
        <div style="text-align:right; margin-bottom:15px;">
            <a href="#" style="font-size:14px; color:#666; text-decoration:underline;">
                Đăng ký bằng số điện thoại
            </a>
        </div>

        <!-- FORM -->
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="addUser">
            <div class="login-body">
                <input type="text" name="username" placeholder="Họ và Tên" required>
                <input type="email" name="email" placeholder="Email" required>
                <input type="password" name="password" placeholder="Mật khẩu" required>
                <input type="password" name="confirm_password" placeholder="Nhập lại mật khẩu" required>
                <button type="submit" class="login-submit">ĐĂNG KÝ</button>
            </div>

        </form>

        <!-- DIVIDER -->
        <div class="divider">
            <span>hoặc đăng ký bằng</span>
        </div>

        <!-- SOCIAL LOGIN -->
        <div class="social-login">
            <button class="google-btn">
                <i class="fa-brands fa-google"></i>
                <span>Google</span>
            </button>

            <button class="facebook-btn">
                <i class="fa-brands fa-facebook-f"></i>
                <span>Facebook</span>
            </button>
        </div>

        <!-- FOOTER -->
        <div style="text-align:center; margin-top:20px; font-size:14px;">
            Bạn đã có tài khoản?
            <a href="#" id="switchToLogin" style="color:#0d6efd; text-decoration:none;">
                Đăng nhập!
            </a>
        </div>

    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {

        // ===== LOGIN / REGISTER =====
        const loginModal = document.getElementById("loginModal");
        const registerModal = document.getElementById("registerModal");
        const openLoginBtn = document.getElementById("openLoginBtn");
        const closeModal = document.getElementById("closeModal");
        const openRegister = document.getElementById("openRegister");
        const switchToLogin = document.getElementById("switchToLogin");
        const closeRegister = document.getElementById("closeRegister");
        if (openLoginBtn) {
            openLoginBtn.addEventListener("click", function (e) {
                e.preventDefault();
                loginModal.classList.add("show");
            });
        }

        if (closeModal) {
            closeModal.addEventListener("click", function () {
                loginModal.classList.remove("show");
            });
        }

        if (openRegister) {
            openRegister.addEventListener("click", function (e) {
                e.preventDefault();
                loginModal.classList.remove("show");
                registerModal.classList.add("show");
            });
        }

        if (switchToLogin) {
            switchToLogin.addEventListener("click", function (e) {
                e.preventDefault();
                registerModal.classList.remove("show");
                loginModal.classList.add("show");
            });
        }

        if (closeRegister) {
            closeRegister.addEventListener("click", function () {
                registerModal.classList.remove("show");
            });
        }
// ===== FORGOT PASSWORD =====
        const openForgot = document.getElementById("openForgot");
        const forgotModal = document.getElementById("forgotModal");
        const closeForgot = document.getElementById("closeForgot");
        if (openForgot) {
            openForgot.addEventListener("click", function (e) {
                e.preventDefault();
                loginModal.classList.remove("show");
                forgotModal.classList.add("show");
            });
        }

        if (closeForgot) {
            closeForgot.addEventListener("click", function () {
                forgotModal.classList.remove("show");
            });
        }
    });</script>

<script>

    const slides = document.querySelectorAll(".slide");
    const dots = document.querySelectorAll(".nav-dot");
    let currentIndex = 0;
    let autoSlide;
    function showSlide(index) {

        slides.forEach(slide => slide.classList.remove("active"));
        dots.forEach(dot => dot.classList.remove("active"));
        slides[index].classList.add("active");
        dots[index].classList.add("active");
        currentIndex = index;
    }

    dots.forEach((dot, index) => {
        dot.addEventListener("click", () => {

            showSlide(index);
            resetAutoSlide();
        });
    });
    function nextSlide() {
        currentIndex++;
        if (currentIndex >= slides.length) {
            currentIndex = 0;
        }

        showSlide(currentIndex);
    }

    function startAutoSlide() {
        autoSlide = setInterval(nextSlide, 4000);
    }

    function resetAutoSlide() {
        clearInterval(autoSlide);
        startAutoSlide();
    }

    startAutoSlide();</script>
<script>
    // ===== USER DROPDOWN =====
    const userMenu = document.getElementById("userMenu");
    const dropdown = document.getElementById("dropdownMenu");
    if (userMenu && dropdown) {
        userMenu.addEventListener("click", function (e) {
            e.stopPropagation();
            dropdown.style.display =
                    dropdown.style.display === "block" ? "none" : "block";
        });
        document.addEventListener("click", function (e) {
            if (!userMenu.contains(e.target) && !dropdown.contains(e.target)) {
                dropdown.style.display = "none";
            }
        });
    }

</script>

<c:if test="${sessionScope.showLoginModal}">
    <script>

        document.addEventListener("DOMContentLoaded", function () {

            let toastEl = document.getElementById("loginToast");
            let toast = new bootstrap.Toast(toastEl);
            toast.show();
        });
    </script>
</c:if>
<script>
    document.addEventListener("DOMContentLoaded", function () {

        document.getElementById("loginForm").addEventListener("submit", function (e) {
            e.preventDefault();
            const emailInput = document.getElementById("emailInput");
            const passInput = document.getElementById("passInput");
            const errorEl = document.getElementById("loginError");
            const btn = document.getElementById("loginBtn");
            const modal = document.querySelector(".login-box");
            const email = emailInput.value.trim();
            const password = passInput.value.trim();
            // reset
            errorEl.innerText = "";
            emailInput.classList.remove("input-error");
            passInput.classList.remove("input-error");
            const emailRegex = /^[A-Za-z0-9+_.-]+@(.+)$/;
            // ❌ email sai
            if (!emailRegex.test(email)) {
                errorEl.innerText = "Email không hợp lệ!";
                emailInput.classList.add("input-error");
                shake(modal);
                return;
            }

            // ❌ thiếu input
            if (email === "" || password === "") {
                errorEl.innerText = "Vui lòng nhập đầy đủ thông tin!";
                if (email === "")
                    emailInput.classList.add("input-error");
                if (password === "")
                    passInput.classList.add("input-error");
                shake(modal);
                return;
            }

            // ⏳ loading
            btn.innerText = "Đang xử lý...";
            btn.classList.add("loading");
            fetch("MainController", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: "action=login&txtUsername=" + encodeURIComponent(email)
                        + "&txtPassword=" + encodeURIComponent(password)
            })
                    .then(res => res.text())
                    .then(data => {
                        if (data.trim() === "success") {
                            location.reload();
                        } else {
                            errorEl.innerText = "Invalid email or password!!!!";
                            passInput.classList.add("input-error");
                            shake(modal);
                        }

                        btn.innerText = "ĐĂNG NHẬP";
                        btn.classList.remove("loading");
                    });
        });
        // 💥 rung
        function shake(el) {
            el.classList.add("shake");
            setTimeout(() => el.classList.remove("shake"), 300);
        }

    });
</script>