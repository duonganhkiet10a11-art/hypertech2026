package controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import model.CartDTO;
import model.ProductDTO;
import model.UserDTO;
import model.OrderDAO;
import model.OrderDTO;
import model.OrderItemDAO;
import model.OrderItemDTO;

public class OrderController extends HttpServlet {

    // ================= CHECK LOGIN =================
    private boolean isAdmin(HttpServletRequest request) {
        return request.getSession().getAttribute("admin") != null;
    }

    private boolean isUser(HttpServletRequest request) {
        return request.getSession().getAttribute("user") != null;
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        return isAdmin(request) || isUser(request);
    }

    // ================= MAIN =================
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!isLoggedIn(request)) {
            response.sendRedirect("index.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "searchOrder";
        }

        try {
            switch (action) {

                case "createOrder":
                    handleCreateOrder(request, response);
                    break;

                case "searchOrder":
                    handleSearch(request, response);
                    break;

                case "cancelOrder":

                    handleDelete(request, response);

                    break;
                case "searchOrderByAd":

                    searchOrderbyad(request, response);

                    break;
                case "updateOrder":
                    if (isAdmin(request)) {
                        handleUpdate(request, response);
                    } else {
                        deny(response);
                    }
                    break;

                case "saveUpdateOrder":
                    if (isAdmin(request)) {
                        handleSaveUpdate(request, response);
                    } else {
                        deny(response);
                    }
                    break;

                case "statisticsOrder":
                    if (isAdmin(request)) {
                        handleStatistics(request, response);
                    } else {
                        deny(response);
                    }
                    break;

                default:
                    handleSearch(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "System error!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // ================= CREATE ORDER =================
    private void handleCreateOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        CartDTO cart = (CartDTO) session.getAttribute("CART");

        if (cart == null || cart.getCart().isEmpty()) {
            response.sendRedirect("cart.jsp");
            return;
        }

        UserDTO user = (UserDTO) session.getAttribute("user");

        // ===== GET INFO =====
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        if (email == null || email.isEmpty()) {
            email = (String) session.getAttribute("EMAIL");
        }

        if (email == null && user != null) {
            email = user.getEmail();
        }

        // ===== SAVE SESSION =====
        session.setAttribute("FULLNAME", fullname);
        session.setAttribute("EMAIL", email);
        session.setAttribute("PHONE", phone);
        session.setAttribute("ADDRESS", address);
// 🔥 THÊM ĐOẠN NÀY (LƯU DB)
        if (user != null) {
            user.setPhone(phone);
            user.setAddress(address);

            new model.UserDAO().updateContact(user.getEmail(), phone, address);

            session.setAttribute("user", user);
        }
        // ===== CALCULATE TOTAL =====
        double total = 0;
        for (ProductDTO p : cart.getCart().values()) {
            total += p.getNew_price() * p.getQuantity();
        }

        // ===== INSERT ORDER =====
        OrderDAO orderDAO = new OrderDAO();
        OrderDTO order = new OrderDTO(0, email, total, "pending", null);

        int orderId = orderDAO.insert(order);

        if (orderId > 0) {

            OrderItemDAO itemDAO = new OrderItemDAO();

            for (ProductDTO p : cart.getCart().values()) {
                OrderItemDTO item = new OrderItemDTO(
                        0,
                        orderId,
                        p.getId(),
                        p.getNew_price(),
                        p.getQuantity()
                );
                itemDAO.insert(item);
            }

            // ================= 🔥 FIX QUAN TRỌNG NHẤT =================
            CartDTO lastCart = new CartDTO();

            for (ProductDTO p : cart.getCart().values()) {

                ProductDTO clone = new ProductDTO();

                clone.setId(p.getId());
                clone.setName(p.getName());
                clone.setNew_price(p.getNew_price());
                clone.setQuantity(p.getQuantity());

                lastCart.add(clone);
            }

            session.setAttribute("LAST_CART", lastCart);

            // ===== RESET CART (TRÁNH DÍNH DỮ LIỆU CŨ) =====
            session.removeAttribute("CART");
            session.setAttribute("CART", new CartDTO());

            // ===== SAVE ORDER ID =====
            session.setAttribute("ORDER_ID", orderId);

            // 👉 chuyển sang success
            response.sendRedirect("success.jsp");

        } else {
            response.sendRedirect("cart.jsp?error=orderFail");
        }
    }

    protected void searchOrderbyad(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String keywords = request.getParameter("keywords");
            System.out.println("control" + keywords);
            OrderDAO dao = new OrderDAO();
            ArrayList<OrderDTO> list = dao.searchOrders(keywords);

            request.setAttribute("list", list);

            request.getRequestDispatcher("order.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("order.jsp");
        }
    }

    // ================= SEARCH =================
    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        OrderDAO dao = new OrderDAO();
        ArrayList<OrderDTO> list;

        // 🔥 FIX: user chỉ thấy đơn của mình
        if (user != null) {
            list = dao.getOrdersByEmail(user.getEmail());
        } else {
            list = dao.getAll(); // admin
        }

        request.setAttribute("list", list);

        request.getRequestDispatcher("order-lookup.jsp").forward(request, response);
    }

    // ================= DELETE =================
    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = parseInt(request.getParameter("id"));
        OrderDAO dao = new OrderDAO();

        if (dao.delete(id)) {
            response.sendRedirect("MainController?action=searchOrder");
        } else {
            response.sendRedirect("MainController?action=searchOrder&error=deleteFail");
        }
    }

    // ================= LOAD UPDATE =================
    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = parseInt(request.getParameter("id"));
        OrderDAO dao = new OrderDAO();

        OrderDTO order = dao.getById(id);

        if (order != null) {
            request.setAttribute("order", order);
            request.setAttribute("mode", "update");
            request.getRequestDispatcher("order-form.jsp").forward(request, response);
        } else {
            response.sendRedirect("MainController?action=searchOrder");
        }
    }

    // ================= SAVE UPDATE =================
    private void handleSaveUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = parseInt(request.getParameter("id"));
        String email = request.getParameter("email");
        double totalPrice = parseDouble(request.getParameter("totalPrice"));
        String status = request.getParameter("status");

        OrderDTO order = new OrderDTO(id, email, totalPrice, status, null);
        OrderDAO dao = new OrderDAO();

        if (dao.update(order)) {
            response.sendRedirect("MainController?action=searchOrder");
        } else {
            response.sendRedirect("MainController?action=searchOrder&error=updateFail");
        }
    }

    // ================= STATISTICS =================
    private void handleStatistics(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        OrderDAO orderDAO = new OrderDAO();
        OrderItemDAO itemDAO = new OrderItemDAO();

        request.setAttribute("totalRevenue", orderDAO.getTotalRevenue());
        request.setAttribute("totalOrders", orderDAO.getTotalOrders());
        request.setAttribute("totalSold", itemDAO.getTotalSoldQuantity());

        request.getRequestDispatcher("statistics-order.jsp").forward(request, response);
    }

    // ================= UTILS =================
    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private void deny(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin only!");
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
}
