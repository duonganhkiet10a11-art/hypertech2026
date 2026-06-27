package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.UserDTO;

public class MainController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String url = "index.jsp";
        System.out.println(action);

        if (action == null) {
            url = "index.jsp";

            // ================= LOGIN =================
        } else if (action.equals("login") || action.equals("Adminlogout")) {
            url = "AdminController";

        } else if (action.equals("Userlogout")) {
            url = "UserController";
        } else if (action.equals("addUser")) {
            url = "UserController";
        } else if (action.equals("forgotPassword")) {
            url = "UserController";
            //======== PRODUCT  ===================
        } else if (action.contains("Product")) {

            url = "ProductController";
        } else if (action.contains("Complain")) {

            url = "ComplainController";
} else if (action.equals("searchUserByAd")) {

            url = "UserController";
            // ================= CART =================
        } else if (action.equals("Cart")
                || action.equals("AddCart")
                || action.equals("UpdateCart")
                || action.equals("RemoveCart")
                || action.equals("clearCart")) {

            url = "CartController";

            // ================= ORDER =================
        } else if (action.equals("createOrder")
                || action.equals("searchOrder")
                || action.equals("cancelOrder")
                || action.equals("updateOrder")
                || action.equals("saveUpdateOrder")
                || action.equals("statisticsOrder")
                || action.equals("searchOrderByAd")) {

            url = "OrderController";

            // ================= SAVE INFO =================
        } else if (action.equals("saveInfo")) {

            HttpSession session = request.getSession();

            String fullname = request.getParameter("fullname");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            // ✅ lưu session
            session.setAttribute("FULLNAME", fullname);
            session.setAttribute("EMAIL", email);
            session.setAttribute("PHONE", phone);
            session.setAttribute("ADDRESS", address);

            // 🔥 FIX: xác nhận đã nhập thông tin
            session.setAttribute("INFO_CONFIRMED", true);

            response.sendRedirect("MainController?action=checkout");
            return;

            // ================= CHECKOUT =================
        } else if (action.equals("checkout")) {

            HttpSession session = request.getSession();
            UserDTO user = (UserDTO) session.getAttribute("user");

            // ❌ chưa login
            if (user == null) {

                session.setAttribute("showLoginModal", true);
                url = "cart.jsp";

            } else {

                String phone = (String) session.getAttribute("PHONE");
                String address = (String) session.getAttribute("ADDRESS");

                // 👉 giữ info user
                session.setAttribute("FULLNAME", user.getUsername());
                session.setAttribute("EMAIL", user.getEmail());

                if (phone != null) {
                    session.setAttribute("PHONE", phone);
                }
                if (address != null) {
                    session.setAttribute("ADDRESS", address);
                }

                // 👉 update DB
                if (phone != null && !phone.isEmpty()
                        && address != null && !address.isEmpty()) {

                    user.setPhone(phone);
                    user.setAddress(address);

                    new model.UserDAO().updateContact(user.getEmail(), phone, address);

                    session.setAttribute("user", user);
                }

                // 🔥 FIX LOOP
                Boolean confirmed = (Boolean) session.getAttribute("INFO_CONFIRMED");

                if (confirmed == null || !confirmed) {

                    url = "information.jsp";

                } else {

                    session.removeAttribute("INFO_CONFIRMED");
                    url = "payment.jsp";
                }
            }

            // ================= PAYMENT =================
        } else if (action.startsWith("viewPayment")
                || action.startsWith("viewUserPayment")
                || action.startsWith("searchPayment")
                || action.startsWith("addPayment")
                || action.startsWith("updatePayment")
                || action.startsWith("deletePayment")
                || action.equals("paymentStatistic")) {

            url = "PaymentController";
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

    private int safeParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Main Controller";
    }
}
