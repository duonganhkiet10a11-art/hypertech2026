package controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import model.ProductDiscountDAO;
import model.ProductDiscountDTO;

public class ProductDiscount extends HttpServlet {

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

        if (action == null) {
            action = "searchProductDiscount";
        }

        switch (action) {

            // ===== USER + ADMIN =====
            case "searchProductDiscount":
                doSearch(request, response);
                break;

            case "viewProductDiscount":
                doView(request, response);
                break;

            case "filterProductDiscountByProduct":
                filterByProduct(request, response);
                break;

            // ===== ADMIN ONLY =====
            case "showAddProductDiscountForm":
                if (isAdmin(request)) {
                    request.getRequestDispatcher("productDiscount-add.jsp")
                            .forward(request, response);
                } else {
                    deny(response);
                }
                break;

            case "addProductDiscount":
                if (isAdmin(request)) {
                    doAdd(request, response);
                } else {
                    deny(response);
                }
                break;

            case "deleteProductDiscount":
                if (isAdmin(request)) {
                    doDelete(request, response);
                } else {
                    deny(response);
                }
                break;

            case "productDiscountStatistic":
                if (isAdmin(request)) {
                    doStatistic(request, response);
                } else {
                    deny(response);
                }
                break;

            default:
                doSearch(request, response);
        }
    }

    // ================= SEARCH =================
    private void doSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDiscountDAO dao = new ProductDiscountDAO();
        ArrayList<ProductDiscountDTO> list = dao.getAll();

        request.setAttribute("list", list);

        request.getRequestDispatcher("productDiscount-search.jsp")
                .forward(request, response);
    }

    // ================= VIEW DETAIL =================
    private void doView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int productID = parseInt(request.getParameter("product_id"));

        ProductDiscountDAO dao = new ProductDiscountDAO();
        ArrayList<ProductDiscountDTO> list = dao.searchByProductID(productID);

        request.setAttribute("list", list);

        request.getRequestDispatcher("productDiscount-detail.jsp")
                .forward(request, response);
    }

    // ================= FILTER =================
    private void filterByProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int productID = parseInt(request.getParameter("product_id"));

        ProductDiscountDAO dao = new ProductDiscountDAO();
        ArrayList<ProductDiscountDTO> list = dao.searchByProductID(productID);

        request.setAttribute("list", list);

        request.getRequestDispatcher("productDiscount-search.jsp")
                .forward(request, response);
    }

    // ================= ADD =================
    private void doAdd(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int orderID = parseInt(request.getParameter("order_id"));
        int productID = parseInt(request.getParameter("product_id"));
        float price = parseFloat(request.getParameter("price"));
        int quantity = parseInt(request.getParameter("quantity"));

        ProductDiscountDTO pd = new ProductDiscountDTO(
                0,
                orderID,
                productID,
                price,
                quantity
        );

        ProductDiscountDAO dao = new ProductDiscountDAO();

        boolean success = dao.add(pd);

        if (success) {
            response.sendRedirect("ProductDiscount?action=searchProductDiscount");
        } else {
            response.sendRedirect("ProductDiscount?action=searchProductDiscount&error=addFail");
        }
    }

    // ================= DELETE =================
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = parseInt(request.getParameter("id"));

        ProductDiscountDAO dao = new ProductDiscountDAO();

        boolean success = dao.delete(id);

        if (success) {
            response.sendRedirect("ProductDiscount?action=searchProductDiscount");
        } else {
            response.sendRedirect("ProductDiscount?action=searchProductDiscount&error=deleteFail");
        }
    }

    // ================= STATISTIC =================
    private void doStatistic(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDiscountDAO dao = new ProductDiscountDAO();
        ArrayList<ProductDiscountDTO> list = dao.getAll();

        int total = list.size();

        request.setAttribute("totalProductDiscount", total);

        request.getRequestDispatcher("admin-dashboard.jsp")
                .forward(request, response);
    }

    // ================= PARSE INT =================
    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    // ================= PARSE FLOAT =================
    private float parseFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return 0;
        }
    }

    // ================= ACCESS DENY =================
    private void deny(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin only!");
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