<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Quản lý đơn hàng</title>
</head>
<body>

<br><br><br>

<h2>XEM ĐƠN HÀNG</h2>

<div style="display:flex; justify-content:space-between; margin-bottom:15px;">

    <form action="MainController" method="get">

        <input type="hidden" name="action" value="searchOrderByAd"/>

        <!-- search theo email hoặc id -->               
        <input type="text" name="keywords"
               value="${param.keywords}"
               placeholder="Tìm theo email ">
        <!-- filter theo status -->


        <button type="submit">Tìm</button>
    </form>
</div>

<c:if test="${not empty list}">
    <table border="1" width="100%" cellpadding="10">
        <tr>
            <th>ID</th>
            <th>Email</th>
            <th>Tổng tiền</th>
            <th>Trạng thái</th>
            <th>Ngày tạo</th>
        </tr>

        <c:forEach var="o" items="${list}">
            <tr>
                <td>${o.id}</td>
                <td>${o.email}</td>
                <td>${o.totalPrice}</td>
                <td>${o.status}</td>
                <td>${o.createdAt}</td>
            </tr>
        </c:forEach>

    </table>
</c:if>

</body>
</html>