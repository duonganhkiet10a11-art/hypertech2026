<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<br><br><br>

<h2>QUẢN LÝ USER</h2>

<!-- ===== SEARCH ===== -->
<div style="display:flex; justify-content:space-between; margin-bottom:15px;">

    <form action="MainController" method="get">

        <input type="hidden" name="action" value="searchUserByAd"/>

        <input type="text" name="keyword"
               value="${param.keyword}"
               placeholder="Nhập email để tìm...">

        <button type="submit">Tìm</button>
    </form>
</div>

<!-- ===== TABLE ===== -->
<c:if test="${not empty list}">

    <table border="1" width="100%" cellpadding="10">
        <tr>
            <th>Email</th>
            <th>Username</th>
            <th>Password</th>
            <th>Phone</th>
            <th>Address</th>
            <th>Status</th>
            <th>Hành động</th>
        </tr>

        <c:forEach var="u" items="${list}">
            <tr>
                <td>${u.email}</td>
                <td>${u.username}</td>
                <td>${u.password}</td>
                <td>${u.phone}</td>
                <td>${u.address}</td>
                <td>${u.status}</td>

                <td>
                    <!-- DELETE -->
                    <form action="MainController" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="deleteUser" />
                        <input type="hidden" name="email" value="${u.email}" />
                        <button type="submit" onclick="return confirm('Delete user này?')">
                            Disable
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>

    </table>

</c:if>

<c:if test="${empty list}">
    <p>Không có user nào</p>
</c:if>