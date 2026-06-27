<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="user" value="${sessionScope.user}" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Thông tin khách hàng</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

        <style>
            body{
                background:#f4f6fb;
                font-family:Segoe UI;
            }

            .info-wrapper{
                width:700px;
                margin:auto;
                margin-top:50px;
                background:white;
                padding:35px;
                border-radius:16px;
                box-shadow:0 10px 25px rgba(0,0,0,0.08);
                animation:fadeIn 0.5s ease;
            }

            .title{
                font-weight:600;
                margin-bottom:25px;
            }

            .form-control{
                border-radius:10px;
                padding:10px;
            }

            .btn-next{
                background:#28a745;
                color:white;
                padding:10px 20px;
                border-radius:10px;
                font-weight:500;
            }

            .btn-next:hover{
                background:#218838;
            }

            .step-bar{
                display:flex;
                justify-content:space-between;
                margin-bottom:25px;
                background:#fdecec;
                padding:15px;
                border-radius:10px;
            }

            .step{
                flex:1;
                text-align:center;
                color:#888;
            }

            .step.active{
                color:#e53935;
                font-weight:bold;
            }

            @keyframes fadeIn{
                from{
                    opacity:0;
                    transform:translateY(10px);
                }
                to{
                    opacity:1;
                    transform:translateY(0);
                }
            }
        </style>
    </head>

    <body>
        <c:if test="${empty user}">
            <c:redirect url="index.jsp"/>
        </c:if>
        <div class="info-wrapper">

            <h3 class="title">📄 Thông tin khách hàng</h3>

            <div class="step-bar">
                <div class="step">🛒<br>Giỏ hàng</div>
                <div class="step active">📄<br>Thông tin</div>
                <div class="step">💳<br>Thanh toán</div>
                <div class="step">✔<br>Hoàn tất</div>
            </div>

            <!-- FORM -->
            <form action="MainController" method="post" onsubmit="return validateForm()">

                <input type="hidden" name="action" value="saveInfo"/>

                <div class="mb-3">
                    <label>Họ tên</label>
                    <input type="text" name="fullname" class="form-control"
                           value="${user.username}" required>
                </div>

                <div class="mb-3">
                    <label>Email</label>
                    <input type="email" name="email" class="form-control"
                           value="${not empty sessionScope.EMAIL ? sessionScope.EMAIL : user.email}" required>
                </div>

                <!-- 🔥 FIX PHONE -->
                <div class="mb-3">
                    <label>Số điện thoại</label>
                    <input type="text" name="phone" class="form-control"
                           value="${not empty sessionScope.PHONE ? sessionScope.PHONE : user.phone}" required>
                </div>

                <!-- 🔥 FIX ADDRESS -->
                <div class="mb-3">
                    <label>Địa chỉ giao hàng</label>
                    <input type="text" name="address" class="form-control"
                           value="${not empty sessionScope.ADDRESS ? sessionScope.ADDRESS : (not empty user.address ? user.address : '')}"required>
                </div>

                <div class="text-end">
                    <button type="submit" class="btn btn-next" onclick="this.disabled = true; this.form.submit();">
                        Tiếp tục thanh toán →
                    </button>
                </div>

            </form>

        </div>

        <script>
            function validateForm() {
                let phone = document.querySelector("input[name='phone']").value;
                let address = document.querySelector("input[name='address']").value;

                let regex = /^[0-9]{9,11}$/;

                if (!regex.test(phone)) {
                    alert("Số điện thoại không hợp lệ!");
                    return false;
                }

                if (address.trim().length < 5) {
                    alert("Địa chỉ quá ngắn!");
                    return false;
                }

                return true;
            }
        </script>

    </body>
</html>