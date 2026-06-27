<%-- 
    Document   : BestSeller.jsp
    Created on : Mar 8, 2026, 9:42:38 AM
    Author     : truon
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="css/BestSeller.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    </head>
    <body>
        <div class="bestseller-banner">
            <img src="images/BestSeller1.png" alt="banner">
        </div>

        <div class="promo-section">

            <div class="promo-container">

                <div class="promo-item">
                    <img src="images/voucher1.png" alt="Voucher">
                </div>

                <a href="#bestSeller">
                    <div class="promo-item">
                        <img src="images/BestSeller2.png" alt="Best Seller">
                    </div>
                </a>

                <a href="#under25">
                    <div class="promo-item">
                        <img src="images/BestSeller3.png" alt="Duoi 25 Trieu">
                    </div>
                </a>

                <a href="#under30">
                    <div class="promo-item">
                        <img src="images/BestSeller4.png" alt="Duoi 30 Trieu">
                    </div>
                </a>

                <a href="#above30">
                    <div class="promo-item">
                        <img src="images/BestSeller5.png" alt="Tren 30 Trieu">
                    </div>
                </a>

            </div>


        </div>
        <div class="best-title"> 
            <img src="images/BestSeller.png" alt="Best Seller"> 
        </div>

        <div id="bestSeller" class="background-section">

            <div class="product-row">

                <c:forEach items="${list}" var="p" begin="0" end="11">
                    <div class="product-card">

                        <img src="images/${p.image}" alt="${p.name}">

                        <h3>${p.name}</h3>

                        <div class="spec-box">
                            <span><i class="fa-solid fa-microchip"></i> ${p.cpu}</span>
                            <span><i class="fa-solid fa-server"></i> ${p.gpu}</span>
                            <span><i class="fa-solid fa-memory"></i> ${p.ram}</span>
                            <span><i class="fa-solid fa-hard-drive"></i> ${p.ssd}</span>
                            <span><i class="fa-solid fa-tv"></i> ${p.screen}</span>
                            <span><i class="fa-solid fa-gauge-high"></i> ${p.refresh_rate}</span>
                        </div>

                        <div class="price-box">

                            <div class="old-price">
                                <fmt:formatNumber value="${p.old_price}" type="number" groupingUsed="true"/>₫
                            </div>

                            <div class="new-price">
                                <fmt:formatNumber value="${p.new_price}" type="number" groupingUsed="true"/>₫
                                <span class="discount">-5%</span>
                            </div>

                        </div>

                        <form action="MainController" method="post">

                            <input type="hidden" name="action" value="AddCart">
                            <input type="hidden" name="productID" value="${p.id}">
                            <input type="hidden" name="quantity" value="1">

                            <button class="buy-btn">Mua ngay</button>

                        </form>

                    </div>
                </c:forEach>

            </div>

        </div>


        <div id="under25" class="under25-section">

            <div class="price-section-title">
                <img src="images/duoi25trieu.png" alt="Laptop dưới 25 triệu">
            </div>

            <div class="product-row">

                <c:forEach items="${listUnder25}" var="p">
                    <div class="product-card">
                        <img src="images/${p.image}" alt="${p.name}">
                        <h3>${p.name}</h3>

                        <div class="spec-box">
                            <span><i class="fa-solid fa-microchip"></i> ${p.cpu}</span>
                            <span><i class="fa-solid fa-server"></i> ${p.gpu}</span>
                            <span><i class="fa-solid fa-memory"></i> ${p.ram}</span>
                            <span><i class="fa-solid fa-hard-drive"></i> ${p.ssd}</span>
                            <span><i class="fa-solid fa-tv"></i> ${p.screen}</span>
                            <span><i class="fa-solid fa-gauge-high"></i> ${p.refresh_rate}</span>
                        </div>

                        <div class="price-box">

                            <div class="old-price">
                                <fmt:formatNumber value="${p.old_price}" type="number" groupingUsed="true"/>₫
                            </div>

                            <div class="new-price">
                                <fmt:formatNumber value="${p.new_price}" type="number" groupingUsed="true"/>₫
                                <span class="discount">-5%</span>
                            </div>
                        </div>
                        <form action="MainController" method="post">

                            <input type="hidden" name="action" value="AddCart">
                            <input type="hidden" name="productID" value="${p.id}">
                            <input type="hidden" name="quantity" value="1">

                            <button class="buy-btn">Mua ngay</button>

                        </form>
                    </div>
                </c:forEach>

            </div>

        </div>
        <div id="under30" class="under30-section">

            <div class="price-section-title">
                <img src="images/duoi30trieu.png" alt="Laptop dưới 30 triệu">
            </div>

            <div class="product-row">

                <c:forEach items="${listUnder30}" var="p">
                    <div class="product-card">
                        <img src="images/${p.image}" alt="${p.name}">
                        <h3>${p.name}</h3>
                        <div class="spec-box">
                            <span><i class="fa-solid fa-microchip"></i> ${p.cpu}</span>
                            <span><i class="fa-solid fa-server"></i> ${p.gpu}</span>
                            <span><i class="fa-solid fa-memory"></i> ${p.ram}</span>
                            <span><i class="fa-solid fa-hard-drive"></i> ${p.ssd}</span>
                            <span><i class="fa-solid fa-tv"></i> ${p.screen}</span>
                            <span><i class="fa-solid fa-gauge-high"></i> ${p.refresh_rate}</span>
                        </div>

                        <div class="price-box">
                            <div class="old-price">
                                <fmt:formatNumber value="${p.old_price}" type="number" groupingUsed="true"/>₫
                            </div>
                            <div class="new-price">
                                <fmt:formatNumber value="${p.new_price}" type="number" groupingUsed="true"/>₫
                                <span class="discount">-5%</span>
                            </div>
                        </div>
                        <form action="MainController" method="post">

                            <input type="hidden" name="action" value="AddCart">
                            <input type="hidden" name="productID" value="${p.id}">
                            <input type="hidden" name="quantity" value="1">

                            <button class="buy-btn">Mua ngay</button>

                        </form>
                    </div>
                </c:forEach>

            </div>

        </div>

        <div id="above30" class="above30-section">
            <div class="price-section-title">
                <img src="images/tren30trieu.png" alt="Laptop trên 30 triệu">
            </div>
            <div class="product-row">
                <c:forEach items="${listTop30}" var="p">
                    <div class="product-card">
                        <img src="images/${p.image}" alt="${p.name}">
                        <h3>${p.name}</h3>
                        <div class="spec-box">
                            <span><i class="fa-solid fa-microchip"></i> ${p.cpu}</span>
                            <span><i class="fa-solid fa-server"></i> ${p.gpu}</span>
                            <span><i class="fa-solid fa-memory"></i> ${p.ram}</span>
                            <span><i class="fa-solid fa-hard-drive"></i> ${p.ssd}</span>
                            <span><i class="fa-solid fa-tv"></i> ${p.screen}</span>
                            <span><i class="fa-solid fa-gauge-high"></i> ${p.refresh_rate}</span>
                        </div>

                        <div class="price-box">

                            <div class="old-price">
                                <fmt:formatNumber value="${p.old_price}" type="number" groupingUsed="true"/>₫
                            </div>
                            <div class="new-price">
                                <fmt:formatNumber value="${p.new_price}" type="number" groupingUsed="true"/>₫
                                <span class="discount">-5%</span>
                            </div>
                        </div>
                        <form action="MainController" method="post">

                            <input type="hidden" name="action" value="AddCart">
                            <input type="hidden" name="productID" value="${p.id}">
                            <input type="hidden" name="quantity" value="1">

                            <button class="buy-btn">Mua ngay</button>

                        </form>
                    </div>
                </c:forEach>
            </div>
        </div>
    </body>
</html>
