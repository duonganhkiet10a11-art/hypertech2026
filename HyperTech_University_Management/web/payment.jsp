<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    if (session.getAttribute("CART") == null) {
        response.sendRedirect("cart.jsp");
        return;
    }
%>

<%@page import="model.CartDTO"%>
<%@page import="model.ProductDTO"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Thanh toán</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

        <style>
            body{
                background: linear-gradient(135deg, #eef2ff, #fdfbfb);
                font-family: Segoe UI;
            }

            .wrapper{
                width:900px;
                margin:auto;
                margin-top:40px;
                background:white;
                padding:30px;
                border-radius:16px;
                box-shadow:0 10px 30px rgba(0,0,0,0.1);
            }

            .steps{
                display:flex;
                justify-content:space-between;
                background:#fdeaea;
                padding:18px;
                border-radius:10px;
                margin-bottom:25px;
            }

            .step{
                flex:1;
                text-align:center;
                color:#666;
            }

            .step.active{
                color:#e53935;
                font-weight:bold;
            }

            .payment-box{
                background:#fafafa;
                padding:20px;
                border-radius:10px;
                border:1px solid #eee;
                margin-bottom:20px;
            }

            .form-check{
                padding:12px;
                border-radius:10px;
                transition:0.2s;
            }

            .form-check:hover{
                background:#f1f3ff;
            }

            .total-box{
                text-align:right;
                font-size:22px;
                font-weight:bold;
                color:#e53935;
            }

            button.btn-success{
                padding:10px 25px;
                border-radius:10px;
                font-weight:600;
                transition:0.3s;
            }

            button.btn-success:hover{
                transform:scale(1.05);
                box-shadow:0 5px 15px rgba(0,0,0,0.2);
            }

            /* LOADING */
            #loadingOverlay{
                position:fixed;
                top:0;
                left:0;
                width:100%;
                height:100%;
                background:rgba(255,255,255,0.9);
                display:none;
                justify-content:center;
                align-items:center;
                z-index:9999;
            }

            .loading-box{
                text-align:center;
            }

            .loading-box img{
                width:120px;
            }

            .loading-box p{
                margin-top:10px;
                font-size:18px;
                color:#555;
                font-weight:500;
            }
        </style>
    </head>

    <body>

        <%
            CartDTO cart = (CartDTO) session.getAttribute("CART");
            double total = 0;
        %>

        <div class="wrapper">

            <div class="steps">
                <div class="step">🛒<br>Giỏ hàng</div>
                <div class="step">📄<br>Thông tin</div>
                <div class="step active">💳<br>Thanh toán</div>
                <div class="step">✔<br>Hoàn tất</div>
            </div>

            <h4>💳 Phương thức thanh toán</h4>

            <form action="MainController" method="post" id="checkoutForm">
                <input type="hidden" name="action" value="addPayment">

                <div class="payment-box">

                    <!-- COD -->
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="payment_method" value="COD" checked>
                        <label>Thanh toán khi nhận hàng</label>
                    </div>

                    <!-- PAYPAL -->
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="payment_method" value="PayPal">

                        <label class="d-flex align-items-center gap-2">
                            <img src="images/Paypal.png" style="height:30px;">
                            Thanh toán bằng PayPal
                        </label>

                        <!-- 👇 HIỂN THỊ NGAY DƯỚI -->
                        <div id="paypal-button-container" style="margin-top:10px; display:none;"></div>
                    </div>

                </div>

                <h4>📦 Sản phẩm</h4>

                <table class="table table-bordered">
                    <tr>
                        <th>Tên</th>
                        <th>Giá</th>
                        <th>SL</th>
                        <th>Tổng</th>
                    </tr>

                    <%
                        for (ProductDTO p : cart.getCart().values()) {
                            double itemTotal = p.getFinalPrice() * p.getQuantity();
                            total += itemTotal;
                    %>

                    <tr>
                        <td><%=p.getName()%></td>
                        <td><%= String.format("%,.0f", p.getFinalPrice())%> ₫</td>
                        <td><%=p.getQuantity()%></td>
                        <td><%= String.format("%,.0f", itemTotal)%> ₫</td>
                    </tr>

                    <% }%>
                </table>

                <div class="total-box">
                    Tổng: <%= String.format("%,.0f", total)%> ₫
                </div>

                <!-- SUBMIT -->
                <div style="text-align:right;margin-top:20px">
                    <button type="submit" class="btn btn-success" id="submitBtn">
                        Xác nhận thanh toán
                    </button>
                </div>

            </form>
        </div>

        <!-- LOADING -->
        <div id="loadingOverlay">
            <div class="loading-box">
                <img src="https://media.tenor.com/On7kvXhzml4AAAAj/loading-gif.gif">
                <p>Hãy đợi tí đang check thanh toán ạ...</p>
            </div>
        </div>

        <!-- PAYPAL SDK -->
        <script src="https://www.paypal.com/sdk/js?client-id=AbVBL_Mtch3neBrKJ40sbC9OaZAVcdB2fGs__oJeAxHU5O09DE139Qx72yPOP2ITSE9WnVLOLidx9nBh&currency=USD"></script>

        <script>
            var totalVND = <%= total%>;
            var totalUSD = (totalVND / 25000).toFixed(2);
            const form = document.getElementById("checkoutForm");
            const loading = document.getElementById("loadingOverlay");
            // SUBMIT
            form.addEventListener("submit", function (e) {
                let method = document.querySelector('input[name="payment_method"]:checked').value;
                if (method === "PayPal") {
                    e.preventDefault();
                    alert("Vui lòng thanh toán bằng PayPal bên dưới!");
                } else {
                    loading.style.display = "flex";
                }
            });
            // TOGGLE
            document.querySelectorAll('input[name="payment_method"]').forEach(radio => {
                radio.addEventListener('change', function () {
                    if (this.value === 'PayPal') {
                        document.getElementById('submitBtn').style.display = 'none';
                        document.getElementById('paypal-button-container').style.display = 'block';
                    } else {
                        document.getElementById('submitBtn').style.display = 'inline-block';
                        document.getElementById('paypal-button-container').style.display = 'none';
                    }
                });
            });
            // PAYPAL
            let paid = false; // ✅ PHẢI ĐỂ NGOÀI
            paypal.Buttons({

                createOrder: function (data, actions) {
                    return actions.order.create({
                        purchase_units: [{
                                amount: {
                                    value: totalUSD
                                }
                            }]
                    });
                },
                onApprove: function (data, actions) {

                    if (paid)
                        return; // 👈 chặn submit lần 2
                    paid = true;
                    loading.style.display = "flex";
                    return actions.order.capture().then(function (details) {
                        setTimeout(() => {
                            form.submit();
                        }, 1000);
                    });
                },

                onError: function (err) {
                    alert("Lỗi PayPal!");
                    console.log(err);
                }

            }).render('#paypal-button-container');

        </script>

    </body>
</html>