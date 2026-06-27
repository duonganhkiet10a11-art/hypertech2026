package controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import model.DiscountDAO;
import model.DiscountDTO;

public class DiscountController extends HttpServlet {

    // ================= CHECK ROLE =================
    private boolean isAdmin(HttpServletRequest request) {
        return request.getSession().getAttribute("admin") != null;
    }

    private boolean isUser(HttpServletRequest request) {
        return request.getSession().getAttribute("user") != null;
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        return isAdmin(request) || isUser(request);
    }

    // ================= MAIN PROCESS =================
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (!isLoggedIn(request)) {
            response.sendRedirect("index.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "searchDiscount";

        switch (action) {

            // ===== USER + ADMIN =====
            case "searchDiscount":
                doSearch(request, response);
                break;

            case "viewDiscount":
                doView(request, response);
                break;

            case "applyDiscount":
                applyDiscount(request, response);
                break;

            // ===== ADMIN =====
            case "showAddDiscountForm":
                request.getRequestDispatcher("discount-add.jsp")
                        .forward(request, response);
                break;

            case "addDiscount":
                if (isAdmin(request)) doAdd(request, response);
                else deny(response);
                break;

            case "showUpdateDiscountForm":
                showUpdateForm(request, response);
                break;

            case "updateDiscount":
                if (isAdmin(request)) doUpdate(request, response);
                else deny(response);
                break;

            case "deleteDiscount":
                if (isAdmin(request)) doDelete(request, response);
                else deny(response);
                break;

            case "discountStatistic":
                if (isAdmin(request)) doStatistic(request, response);
                else deny(response);
                break;

            default:
                doSearch(request, response);
        }
    }

    // ================= SEARCH =================
    private void doSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DiscountDAO dao = new DiscountDAO();
        ArrayList<DiscountDTO> list = dao.getAll();

        request.setAttribute("list", list);

        request.getRequestDispatcher("discount-search.jsp")
                .forward(request, response);
    }

    // ================= VIEW =================
    private void doView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = parseInt(request.getParameter("id"));

        DiscountDAO dao = new DiscountDAO();
        DiscountDTO discount = dao.getById(id);

        if (discount == null) {
            response.sendRedirect("DiscountController?action=searchDiscount");
            return;
        }

        request.setAttribute("discount", discount);

        request.getRequestDispatcher("discount-detail.jsp")
                .forward(request, response);
    }

    // ================= APPLY DISCOUNT =================
    private void applyDiscount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");

        DiscountDAO dao = new DiscountDAO();
        DiscountDTO discount = dao.getValidDiscount(name);

        if (discount != null) {

            request.getSession().setAttribute("discount", discount);

            request.setAttribute("message", "Discount applied successfully!");

        } else {

            request.setAttribute("error", "Invalid or expired discount code!");

        }

        request.getRequestDispatcher("cart.jsp")
                .forward(request, response);
    }

    // ================= ADD =================
    private void doAdd(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        DiscountDTO discount = extractDiscount(request);

        DiscountDAO dao = new DiscountDAO();

        if (dao.insert(discount)) {
            response.sendRedirect("DiscountController?action=searchDiscount");
        } else {
            response.sendRedirect("DiscountController?action=searchDiscount&error=addFail");
        }
    }

    // ================= SHOW UPDATE FORM =================
    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = parseInt(request.getParameter("id"));

        DiscountDAO dao = new DiscountDAO();
        DiscountDTO discount = dao.getById(id);

        request.setAttribute("discount", discount);

        request.getRequestDispatcher("discount-update.jsp")
                .forward(request, response);
    }

    // ================= UPDATE =================
    private void doUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        DiscountDTO discount = extractDiscount(request);

        DiscountDAO dao = new DiscountDAO();

        if (dao.update(discount)) {
            response.sendRedirect("DiscountController?action=searchDiscount");
        } else {
            response.sendRedirect("DiscountController?action=searchDiscount&error=updateFail");
        }
    }

    // ================= DELETE =================
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = parseInt(request.getParameter("id"));

        DiscountDAO dao = new DiscountDAO();

        if (dao.delete(id)) {
            response.sendRedirect("DiscountController?action=searchDiscount");
        } else {
            response.sendRedirect("DiscountController?action=searchDiscount&error=deleteFail");
        }
    }

    // ================= STATISTIC =================
    private void doStatistic(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DiscountDAO dao = new DiscountDAO();

        int total = dao.countDiscounts();

        request.setAttribute("totalDiscounts", total);

        request.getRequestDispatcher("admin-dashboard.jsp")
                .forward(request, response);
    }

    // ================= EXTRACT DISCOUNT =================
    private DiscountDTO extractDiscount(HttpServletRequest request) {

        int id = parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        int percent = parseInt(request.getParameter("discount_percent"));

        Date start = Date.valueOf(request.getParameter("start_date"));
        Date end = Date.valueOf(request.getParameter("end_date"));

        return new DiscountDTO(id, name, percent, start, end);
    }

    // ================= PARSE =================
    private int parseInt(String value) {
        try { return Integer.parseInt(value); }
        catch (Exception e) { return 0; }
    }

    // ================= ACCESS DENY =================
    private void deny(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied!");
    }

    // ================= GET =================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // ================= POST =================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}