<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    if (session.getAttribute("ORDER_ID") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>

<%@page import="model.CartDTO"%>
<%@page import="model.ProductDTO"%>
<%@page import="model.OrderDAO"%>
<%@page import="model.OrderDTO"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Đặt hàng thành công</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

        <style>
            body{
                background: linear-gradient(135deg,#eef2ff,#fdfbfb);
                font-family:Segoe UI;
            }

            .wrapper{
                width:900px;
                margin:auto;
                margin-top:60px;
                background:white;
                padding:40px;
                border-radius:16px;
                box-shadow:0 10px 30px rgba(0,0,0,0.1);
                text-align:center;
            }

            .steps{
                display:flex;
                justify-content:space-between;
                background:#fdeaea;
                padding:18px;
                border-radius:10px;
                margin-bottom:30px;
            }

            .step{
                flex:1;
                color:#666;
            }
            .step.active{
                color:#28a745;
                font-weight:bold;
            }

            .success-icon{
                font-size:80px;
                color:#28a745;
            }

            .info-box{
                background:#f8f9fa;
                padding:15px;
                border-radius:10px;
                margin-top:20px;
                text-align:left;
            }

            .table th{
                background:#212529;
                color:white;
            }

            .timeline{
                display:flex;
                justify-content:space-between;
                margin-top:25px;
            }

            .timeline div{
                flex:1;
                text-align:center;
            }
        </style>
    </head>

    <body>

        <div class="wrapper">

            <!-- STEP -->
            <div class="steps">
                <div class="step">🛒<br>Giỏ hàng</div>
                <div class="step">📄<br>Thông tin</div>
                <div class="step">💳<br>Thanh toán</div>
                <div class="step active">✔<br>Hoàn tất</div>
            </div>

            <div class="success-icon">✔</div>

            <h2>Đặt hàng thành công 🎉</h2>
            <p>Đơn hàng đang được xử lý</p>

            <!-- ================= SAFE ORDER ================= -->
            <%
                Object oidObj = session.getAttribute("ORDER_ID");
                int oid = oidObj != null ? (int) oidObj : 0;

                OrderDTO order = null;
                String status = "pending";

                if (oid > 0) {
                    order = new OrderDAO().getById(oid);
                    if (order != null) {
                        status = order.getStatus();
                    }
                }

                String statusText = "Đang xử lý";
                if ("shipping".equals(status)) {
                    statusText = "Đang giao";
                }
                if ("completed".equals(status))
                    statusText = "Hoàn tất";
            %>

            <!-- ================= INFO ================= -->
            <div class="info-box">
                <h5>📦 Thông tin đơn hàng</h5>

                <p><b>Mã đơn:</b> #<%= oid%></p>

                <p><b>Khách hàng:</b> <%= session.getAttribute("FULLNAME")%></p>
                <p><b>SĐT:</b> <%= session.getAttribute("PHONE")%></p>
                <p><b>Địa chỉ:</b> <%= session.getAttribute("ADDRESS")%></p>

                <p><b>Thanh toán:</b> 
                    <%= session.getAttribute("PAYMENT_METHOD")%>
                </p>

                <p><b>Trạng thái:</b> 
                    <span style="
                          color:
                          <%= "pending".equals(status) ? "orange"
                        : "shipping".equals(status) ? "blue"
                        : "completed".equals(status) ? "green" : "black"%>;
                          ">
                        <%= statusText%>
                    </span>
                </p>
            </div>

            <!-- ================= PRODUCT ================= -->
            <%
                CartDTO cart = (CartDTO) session.getAttribute("LAST_CART");
                double total = 0;
            %>

            <h4 class="mt-4">🛍 Sản phẩm đã mua</h4>

            <table class="table table-bordered mt-3">

                <tr>
                    <th>Tên</th>
                    <th>Giá</th>
                    <th>SL</th>
                    <th>Tổng</th>
                </tr>

                <%
                    if (cart != null && cart.getCart() != null) {
                        for (ProductDTO p : cart.getCart().values()) {

                            double itemTotal = p.getFinalPrice() * p.getQuantity();
                            total += itemTotal;
                %>

                <tr>
                    <td><%= p.getName()%></td>
                    <td><%= String.format("%,.0f", p.getFinalPrice())%> ₫</td>
                    <td><%= p.getQuantity()%></td>
                    <td><%= String.format("%,.0f", itemTotal)%> ₫</td>
                </tr>

                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="4" style="color:red;">Không có dữ liệu sản phẩm</td>
                </tr>
                <%
                    }
                %>

            </table>

            <!-- ================= TOTAL ================= -->
            <h4 style="text-align:right;color:red;">
                Tổng tiền: <%= String.format("%,.0f", total)%> ₫
            </h4>

            <!-- ================= BUTTON ================= -->
            <div class="mt-4">

                <a href="index.jsp" class="btn btn-primary">
                    🛍 Tiếp tục mua
                </a>

                <a href="MainController?action=searchOrder" class="btn btn-success">
                    📄 Xem đơn hàng
                </a>

            </div>

        </div>

        <%
            // OPTIONAL cleanup
            session.removeAttribute("LAST_CART");
        %>

    </body>
</html>