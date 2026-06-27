       <%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Dashboard</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin.css">

</head>

<body>
    <div class="admin-container">
        <div class="sidebar">
            <ul>
                <li><a href="Admin.jsp">Trang Chủ</a></li>
                <li><a href="product.jsp">Sản Phẩm</a></li>
                <li><a href="complain.jsp">Phản Hồi</a></li>
                <li><a href="order.jsp">Đơn Hàng</a></li>
                <li><a href="user.jsp">Người Dùng</a></li>
                <li><a href="index.jsp">Thoát</a></li>
            </ul>
        </div>
    </div>
</body>
</html>