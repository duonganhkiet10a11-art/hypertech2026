package controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import model.*;

public class PaymentController extends HttpServlet {

    private boolean isAdmin(HttpServletRequest request) {
        return request.getSession().getAttribute("admin") != null;
    }

    private boolean isUser(HttpServletRequest request) {
        return request.getSession().getAttribute("user") != null;
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        return isAdmin(request) || isUser(request);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (!isLoggedIn(request)) {
            response.sendRedirect("index.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "searchPayment";
        }

        switch (action) {

            case "checkout":
                request.getRequestDispatcher("payment.jsp").forward(request, response);
                break;

            case "searchPayment":
                doSearch(request, response);
                break;

            case "viewPayment":
                doView(request, response);
                break;

            case "viewUserPayments":
                doViewUserPayments(request, response);
                break;

            case "addPayment":
                if (isUser(request)) {
                    doAdd(request, response);
                } else {
                    deny(response);
                }
                break;

            default:
                doSearch(request, response);
        }
    }

    // ================= ADD PAYMENT =================
    private void doAdd(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        CartDTO cart = (CartDTO) session.getAttribute("CART");
        UserDTO user = (UserDTO) session.getAttribute("user");

        // ❌ chưa login
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // ❌ cart rỗng
        if (cart == null || cart.getCart() == null || cart.getCart().isEmpty()) {
            response.sendRedirect("cart.jsp?error=empty");
            return;
        }

        // ===== TÍNH TỔNG =====
        float total = 0;
        for (ProductDTO p : cart.getCart().values()) {
            total += p.getFinalPrice() * p.getQuantity();
        }

        // ===== METHOD =====
        String method = request.getParameter("payment_method");
        if (method == null) {
            method = "COD";
        }

        // ===== ORDER =====
        OrderDAO orderDAO = new OrderDAO();
        OrderDTO order = new OrderDTO();

        order.setEmail(user.getEmail());
        order.setTotalPrice(total);
        order.setStatus("pending");

        int orderId = orderDAO.insert(order);

        if (orderId <= 0) {
            response.sendRedirect("payment.jsp?error=orderFail");
            return;
        }

        // ===== ORDER ITEMS =====
        OrderItemDAO itemDAO = new OrderItemDAO();

        for (ProductDTO p : cart.getCart().values()) {
            OrderItemDTO item = new OrderItemDTO();
            item.setOrderID(orderId);
            item.setProductID(p.getId());
            item.setPrice(p.getFinalPrice());
            item.setQuantity(p.getQuantity());
            itemDAO.insert(item);
        }

        // ===== PAYMENT =====
        PaymentDTO payment = new PaymentDTO();

        payment.setOrderId(orderId);
        payment.setEmail(user.getEmail());
        payment.setPaymentMethod(method);
        payment.setAmount(total);

        payment.setTransactionCode("TXN" + System.currentTimeMillis());
        payment.setPaid_at(new java.sql.Date(System.currentTimeMillis()));

        if ("COD".equals(method)) {
            payment.setStatus("pending");
        } else {
            payment.setStatus("paid");
        }

        boolean success = new PaymentDAO().insert(payment);

        // ================= SUCCESS =================
        if (success) {

            // 👉 lưu thông tin cần thiết
            session.setAttribute("ORDER_ID", orderId);
            session.setAttribute("PAYMENT_METHOD", method);
            // 🔥 THÊM ĐOẠN NÀY
            StringBuilder productList = new StringBuilder();

            for (ProductDTO p : cart.getCart().values()) {
                productList.append("<li>")
                        .append(p.getName())
                        .append(" - SL: ").append(p.getQuantity())
                        .append("</li>");
            }
            // ===== SEND MAIL =====
            String content = "<h2>Xác nhận đơn hàngg!!!</h2>"
                    + "<p>Mã đơn: <b>#" + orderId + "</b></p>"
                    + "<ul>" + productList.toString() + "</ul>"
                    + "<p>Tổng tiền: <b>" + String.format("%,.0f", total) + " VND</b></p>"
                    + "<p>Phương thức: <b>" + method + "</b></p>"
                    + "<hr>"
                    + "<p>Cảm ơn bạn đã mua hàng tại TKT 💖</p>";
            System.out.println("👉 EMAIL USER: " + user.getEmail());
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                util.MailUtil.sendEmail(user.getEmail(), "Xác nhận đơn hàng", content);
            } else {
                System.out.println("❌ Email user null → không gửi mail");
            }

            // 🔥 FIX QUAN TRỌNG NHẤT (THÊM ĐOẠN NÀY)
            CartDAO cartDAO = new CartDAO();
            cartDAO.clearCart(user.getEmail()); // ❗ XÓA DB

            // 👉 giữ để hiển thị success
            session.setAttribute("LAST_CART", cart);

            // 👉 xóa session
            session.removeAttribute("CART");

            // 👉 chuyển trang
            response.sendRedirect("success.jsp");

        } else {
            response.sendRedirect("payment.jsp?error=fail");
        }
    }

    // ================= SEARCH =================
    private void doSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<PaymentDTO> list = new PaymentDAO().getAll();
        request.setAttribute("list", list);
        request.getRequestDispatcher("payment-search.jsp").forward(request, response);
    }

    // ================= VIEW =================
    private void doView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        PaymentDTO payment = new PaymentDAO().getById(id);

        request.setAttribute("payment", payment);
        request.getRequestDispatcher("payment-detail.jsp").forward(request, response);
    }

    // ================= USER PAYMENT =================
    private void doViewUserPayments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserDTO user = (UserDTO) request.getSession().getAttribute("user");

        ArrayList<PaymentDTO> list = new PaymentDAO().getByUserId(user.getEmail());

        request.setAttribute("list", list);
        request.getRequestDispatcher("payment-user.jsp").forward(request, response);
    }

    private void deny(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied!");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        processRequest(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        processRequest(req, res);
    }
}
