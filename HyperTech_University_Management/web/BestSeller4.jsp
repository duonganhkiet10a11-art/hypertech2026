<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Best Seller Màn Hình</title>

        <link rel="stylesheet" href="css/BestSeller4.css">

    </head>
    <body>
        <div class="banner">
            <img src="images/bannermh2.jpg" alt="banner">
            <div class="banner-overlay"></div>
        </div>

        <div class="category-wrapper">

            <div class="category-container">

                <a href="#bestSeller">
                    <img src="images/gearvn-man-hinh-t9-button-best-seller.png" class="category-item">
                </a>

                <a href="#inch24">
                    <img src="images/gearvn-man-hinh-t9-button-gaming-24-inch.png" class="category-item">
                </a>

                <a href="#inch27">
                    <img src="images/gearvn-man-hinh-t9-button-gaming-27-inch.png" class="category-item">
                </a>

                <a href="#oled">
                    <img src="images/gearvn-man-hinh-t9-button-oled-reup.png" class="category-item">
                </a>


            </div>

        </div>

        <div id="bestSeller" class="best-seller-section">

            <!-- tiêu đề -->
            <div class="best-seller-container">
                <img src="images/gearvn-man-hinh-t9-section-best-seller.png" alt="">
            </div>

            <!-- background + sản phẩm -->
            <div class="best-seller-content">

                <div class="product-container">

                    <c:forEach var="p" items="${listProduct}">

                        <div class="product-card">

                            <div class="product-img">
                                <img src="images/${p.image}">
                            </div>

                            <div class="product-name">
                                ${p.name}
                            </div>

                            <div class="product-price">

                                <span class="old-price">
                                    <fmt:formatNumber value="${p.old_price}" type="number" groupingUsed="true"/>đ
                                </span>

                                <div class="price-row">
                                    <span class="new-price">
                                        <fmt:formatNumber value="${p.new_price}" type="number" groupingUsed="true"/>đ
                                    </span>

                                    <span class="discount">-30%</span>
                                </div>

                            </div>

                            <button class="buy-btn">Mua ngay</button>

                        </div>

                    </c:forEach>

                </div>

            </div>

        </div>

        <!-- ================= 24 INCH ================= -->
        <div id="inch24" class="inch24-section">

            <!-- tiêu đề -->
            <div class="inch24-container">
                <img src="images/24-innch.png" alt="24 inch">
            </div>

            <!-- background + sản phẩm -->
            <div class="inch24-content">

                <div class="product-container">

                    <c:forEach var="p" items="${list24inch}">

                        <div class="product-card">

                            <div class="product-img">
                                <img src="images/${p.image}">
                            </div>

                            <div class="product-name">
                                ${p.name}
                            </div>

                            <div class="product-price">

                                <span class="old-price">
                                    <fmt:formatNumber value="${p.old_price}" type="number" groupingUsed="true"/>đ
                                </span>

                                <div class="price-row">
                                    <span class="new-price">
                                        <fmt:formatNumber value="${p.new_price}" type="number" groupingUsed="true"/>đ
                                    </span>

                                    <span class="discount">-10%</span>
                                </div>

                            </div>

                            <button class="buy-btn">Mua ngay</button>

                        </div>

                    </c:forEach>

                </div>

            </div>

        </div>

        <!-- ================= 27 INCH ================= -->
        <div id="inch27" class="inch27-section">

            <!-- tiêu đề -->
            <div class="inch27-container">
                <img src="images/27-inch.png" alt="27 inch">
            </div>

            <!-- background + sản phẩm -->
            <div class="inch27-content">

                <div class="product-container">

                    <c:forEach var="p" items="${list27inch}">

                        <div class="product-card">

                            <div class="product-img">
                                <img src="images/${p.image}">
                            </div>

                            <div class="product-name">
                                ${p.name}
                            </div>

                            <div class="product-price">

                                <span class="old-price">
                                    <fmt:formatNumber value="${p.old_price}" type="number" groupingUsed="true"/>đ
                                </span>

                                <div class="price-row">
                                    <span class="new-price">
                                        <fmt:formatNumber value="${p.new_price}" type="number" groupingUsed="true"/>đ
                                    </span>

                                    <span class="discount">-10%</span>
                                </div>

                            </div>

                            <button class="buy-btn">Mua ngay</button>

                        </div>

                    </c:forEach>

                </div>

            </div>

        </div>

        <!-- ================= OLED ================= -->
        <div id="oled" class="oled-section">

            <!-- tiêu đề -->
            <div class="oled-container">
                <img src="images/gearvn-man-hinh-t9-section-oled.png" alt="OLED">
            </div>

            <!-- background + sản phẩm -->
            <div class="oled-content">

                <div class="product-container">

                    <c:forEach var="p" items="${listOLED}">

                        <div class="product-card">

                            <div class="product-img">
                                <img src="images/${p.image}">
                            </div>

                            <div class="product-name">
                                ${p.name}
                            </div>

                            <div class="product-price">

                                <span class="old-price">
                                    <fmt:formatNumber value="${p.old_price}" type="number" groupingUsed="true"/>đ
                                </span>

                                <div class="price-row">
                                    <span class="new-price">
                                        <fmt:formatNumber value="${p.new_price}" type="number" groupingUsed="true"/>đ
                                    </span>

                                    <span class="discount">-10%</span>
                                </div>

                            </div>

                            <button class="buy-btn">Mua ngay</button>

                        </div>

                    </c:forEach>

                </div>

            </div>

        </div>

        <script>
            const menu = document.querySelector(".category-wrapper");
            const trigger = document.getElementById("bestSeller");

            const menuOffset = menu.offsetTop;

            window.addEventListener("scroll", () => {
                if (window.scrollY >= menuOffset) {
                    menu.classList.add("fixed");
                } else {
                    menu.classList.remove("fixed");
                }
            });
        </script>
    </body>
</html>
