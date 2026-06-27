<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Best Seller Keyboard</title>

        <link rel="stylesheet" href="css/BestSeller3.css">

    </head>

    <body>

        <div class="banner">
            <img src="images/keyboardgaming.png" alt="banner">
        </div>
        
        <div class="separator"></div>
        
        <div class="product-section">

            <div class="product-container">

                <c:forEach var="p" items="${keyboardList}">

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