<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title>Banner</title>

        <link rel="stylesheet" href="css/BestSeller2.css">

    </head>

    <body>

        <div class="banner">
            <img src="images/Banner1.png" alt="banner">
        </div>

        <div class="category-bar">

            <div class="category-container">
                <a href="#deal" class="menu-btn">DEAL HỜI</a>
                <a href="#gaming" class="menu-btn">CHUỘT GAMING</a>
                <a href="#office" class="menu-btn">CHUỘT VĂN PHÒNG</a>
            </div>

        </div>

        <!-- Banner thứ 2 -->
        <div id="deal" class="banner2">
            <img src="images/Banner2.png" alt="banner2">
        </div>

        <div class="product-section">

            <div class="product-container">

                <c:forEach var="p" items="${dealMouse}" >

                    <div class="product-card">

                        <div class="product-img">
                            <img src="images/${p.image}">
                        </div>

                        <div class="product-name">
                            ${p.name}
                        </div>

                        <div class="product-price">

                            <span class="old-price">
                                <fmt:formatNumber value="${p.old_price}" type="number" groupingUsed="true"/>
                            </span>

                            <div class="price-row">
                                <span class="new-price">
                                    <fmt:formatNumber value="${p.new_price}" type="number" groupingUsed="true"/>
                                </span>
                                <span class="discount">-20%</span>
                            </div>

                        </div>


                        <form action="MainController" method="post">

                            <input type="hidden" name="action" value="AddCart">
                            <input type="hidden" name="productID" value="${p.id}">
                            <input type="hidden" name="quantity" value="1">

                            <button class="buy-btn">Mua ngay</button>

                        </form>
                    </div>   <!-- THÊM CÁI NÀY -->

                </c:forEach>

            </div>

        </div>

        <!-- Banner3 -->
        <div id="gaming" class="banner3">
            <img src="images/Banner3.png">
        </div>

        <!-- Sản phẩm dưới Banner3 -->
        <div class="product-section">

            <div class="product-container">

                <c:forEach var="p" items="${gamingMouse}">
                    <div class="product-card">

                        <div class="product-img">
                            <img src="images/${p.image}">
                        </div>

                        <div class="product-name">
                            ${p.name}
                        </div>
                        <div class="product-price">
                            <span class="old-price">
                                <fmt:formatNumber value="${p.old_price}" type="number" groupingUsed="true"/>
                            </span>

                            <div class="price-row">
                                <span class="new-price">
                                    <fmt:formatNumber value="${p.new_price}" type="number" groupingUsed="true"/>
                                </span>
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

        <!-- Banner4 -->
        <div id="office" class="banner4">
            <img src="images/Banner4.png">
        </div>

        <!-- Sản phẩm dưới Banner4 -->
        <div class="product-section">

            <div class="product-container">

                <c:forEach var="p" items="${officeMouse}">
                    <div class="product-card">

                        <div class="product-img">
                            <img src="images/${p.image}">
                        </div>

                        <div class="product-name">
                            ${p.name}
                        </div>

                        <div class="product-price">
                            <span class="old-price">
                                <fmt:formatNumber value="${p.old_price}" type="number" groupingUsed="true"/>
                            </span>

                            <div class="price-row">
                                <span class="new-price">
                                    <fmt:formatNumber value="${p.new_price}" type="number" groupingUsed="true"/>
                                </span>
                                <span class="discount">-2%</span>
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