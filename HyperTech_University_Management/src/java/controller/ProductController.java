package controller;

import java.io.IOException;

import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import model.DiscountDAO;
import model.DiscountDTO;

import model.ProductDAO;
import model.ProductDTO;

public class ProductController extends HttpServlet {

    // ================= CHECK ROLE =================
    private boolean isAdmin(HttpServletRequest request) {
        return request.getSession().getAttribute("admin") != null;
    }


    // ================= MAIN PROCESS =================
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            request.getRequestDispatcher("product.jsp").forward(request, response);
            return;
        }
        switch (action) {

            // ================= USER + ADMIN =================
            case "searchProductByAd":
                doSearchAdmin(request, response);
                break;
            case "viewProduct":
                doView(request, response);
                break;

            // ================= ADMIN ONLY =================
            case "addProduct":
                if (isAdmin(request)) {
                    doAdd(request, response);
                } else {
                    deny(response);
                }
                break;

            case "updateProduct":
                if (isAdmin(request)) {
                    runUpdate(request, response);
                } else {
                    deny(response);
                }
                break;
            case "editProduct":
                if (isAdmin(request)) {
                    doUpdate(request, response);
                } else {
                    deny(response);
                }
                break;
            case "deleteProduct":
                if (isAdmin(request)) {
                    doSoftDelete(request, response);
                } else {
                    deny(response);
                }
                break;

            case "ProductStatistic":
                if (isAdmin(request)) {
                    doStatistic(request, response);
                } else {
                    deny(response);
                }
                break;
            case "searchProductMouse":
                doshowMouse(request, response);
                break;
            case "searchProductLaptop":
                doShowLap(request, response);
                break;
            case "searchProductKeyboard":
                doShowKeyBoard(request, response);
                break;
            case "searchProductMonitor":
                doShowMonitor(request, response);
                break;
            default:
                doSearch(request, response);
        }
    }

// ================= SEARCH =================
    private void doSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keywords = request.getParameter("keywords");

        ProductDAO dao = new ProductDAO();
        ArrayList<ProductDTO> list = dao.searchByName(keywords);

        request.setAttribute("list", list);

        request.getRequestDispatcher("")
                .forward(request, response);
    }

    private void doSearchAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keywords = request.getParameter("keywords");
        String category = request.getParameter("category");

        ProductDAO dao = new ProductDAO();
        ArrayList<ProductDTO> list;

        // chuẩn hóa
        if (keywords == null) {
            keywords = "";
        }
        keywords = keywords.trim();

        // ===== SEARCH LOGIC =====
        if (keywords.isEmpty() && (category == null || category.isEmpty())) {
            list = dao.getAll();
        } else if (category != null && !category.isEmpty() && !keywords.isEmpty()) {
            int category_id = Integer.parseInt(category);
            list = dao.searchByNamepro(keywords, category_id);
        } else if (category != null && !category.isEmpty()) {
            int category_id = Integer.parseInt(category);
            list = dao.getByCategory(category_id);
        } else {
            list = dao.searchByName(keywords);
        }

        // ===== LOGIC HIỂN THỊ CỘT =====
        boolean isLaptop = false;

        if (category != null && !category.isEmpty()) {
            isLaptop = category.equals("1");
        } else if (list != null && !list.isEmpty()) {
            // auto detect nếu không chọn category
            isLaptop = list.get(0).getCpu() != null;
        }

        request.setAttribute("list", list);
        request.setAttribute("isLaptop", isLaptop);

        request.getRequestDispatcher("product.jsp").forward(request, response);
    }

    private void doshowMouse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keywords = request.getParameter("keywords");
        String category = request.getParameter("category");
        System.out.println("search" + category);
        if (keywords == null) {
            keywords = "";
        }

        ProductDAO dao = new ProductDAO();

        ArrayList<ProductDTO> list;
        ArrayList<ProductDTO> dealMouse = dao.getDealMouse();
        ArrayList<ProductDTO> gamingMouse = dao.getGamingMouse();
        ArrayList<ProductDTO> officeMouse = dao.getOfficeMouse();

        if (category != null) {
            int category_id = Integer.parseInt(category);
            list = dao.getByCategory(category_id);
        } else if (!keywords.trim().isEmpty()) {
            list = dao.searchByName(keywords);
        } else {
            list = dao.getAll();
        }

        request.setAttribute("productList", list);

        // ===== thêm 2 list chuột =====
        request.setAttribute("dealMouse", dealMouse);
        request.setAttribute("gamingMouse", gamingMouse);
        request.setAttribute("officeMouse", officeMouse);

        request.setAttribute("keywords", keywords);

        request.getRequestDispatcher("BestSeller2.jsp").forward(request, response);
    }

    // ================= VIEW DETAIL =================
    private void doView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = parseInt(request.getParameter("id"));
        ProductDAO dao = new ProductDAO();
        ProductDTO product = dao.getById(id);

        request.setAttribute("product", product);
        request.getRequestDispatcher("product-detail.jsp").forward(request, response);
    }

    // ================= ADD =================
    private ProductDTO extractProductForAdd(HttpServletRequest request) {

        return new ProductDTO(
                0,
                safeParseInt(request.getParameter("category_id")),
                request.getParameter("name"),
                request.getParameter("cpu"),
                request.getParameter("gpu"),
                request.getParameter("ram"),
                request.getParameter("ssd"),
                request.getParameter("screen"),
                request.getParameter("refresh_rate"),
                safeParseFloat(request.getParameter("old_price")),
                safeParseFloat(request.getParameter("new_price")),
                safeParseInt(request.getParameter("stock")),
                request.getParameter("description"),
                request.getParameter("image"),
                true
        );
    }

    private void doAdd(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            ProductDTO p = extractProductForAdd(request);
            ProductDAO dao = new ProductDAO();

            if (dao.add(p)) {
                response.sendRedirect("ProductController?action=searchProductByAd");
            } else {
                response.sendRedirect("ProductController?action=searchProductByAd&error=addFail");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ProductController?action=searchProductByAd&error=invalidData");
        }
    }

    private void runUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {

            int id = Integer.parseInt(request.getParameter("id"));
            ProductDAO dao = new ProductDAO();
            ProductDTO p = dao.getById(id);

            request.setAttribute("product", p);
            request.setAttribute("keyword", request.getParameter("keyword"));

            request.getRequestDispatcher("updateProduct.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ProductController?action=searchProdzByAd&error=sailuong");
        }

    }

    // ================= UPDATE =================
    private void doUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            ProductDTO p = extractProductSafe(request);
            ProductDAO dao = new ProductDAO();

            String keyword = request.getParameter("keyword");

            System.out.println(">>> Updating product ID = " + p.getId());

            if (dao.update(p)) {

                System.out.println(">>> UPDATE SUCCESS");

                response.sendRedirect("ProductController?action=searchProductByAd"
                        + "&keyword=" + (keyword != null ? keyword : ""));

            } else {

                System.out.println(">>> UPDATE FAIL");

                response.sendRedirect("ProductController?action=searchProductByAd"
                        + "&error=updateFail"
                        + "&keyword=" + (keyword != null ? keyword : ""));
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ProductController?action=searchProductByAd&error=invalidData");
        }
    }

    // ================= SOFT DELETE =================
    private void doSoftDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        ProductDAO dao = new ProductDAO();

        if (dao.delete(id)) {
            response.sendRedirect("ProductController?action=searchProductByAd");
        } else {
            response.sendRedirect("ProductController?action=searchProductByAd&error=deleteFail");
        }
    }

    // ================= COUNT PRODUCT =================
    private void doStatistic(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDAO dao = new ProductDAO();
        int total = dao.countProducts();

        request.setAttribute("totalProduct", total);
        request.getRequestDispatcher("admin-dashboard.jsp").forward(request, response);
    }

    // ================= EXTRACT =================
    private int safeParseInt(String s) {
        if (s == null || s.trim().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(s);
    }

    private float safeParseFloat(String s) {
        if (s == null || s.trim().isEmpty()) {
            return 0;
        }
        return Float.parseFloat(s);
    }

    private ProductDTO extractProductSafe(HttpServletRequest request) {

        int id = safeParseInt(request.getParameter("id"));
        int category_id = safeParseInt(request.getParameter("category_id"));

        return new ProductDTO(
                id,
                category_id,
                request.getParameter("name"),
                request.getParameter("cpu"),
                request.getParameter("gpu"),
                request.getParameter("ram"),
                request.getParameter("ssd"),
                request.getParameter("screen"),
                request.getParameter("refresh_rate"),
                safeParseFloat(request.getParameter("old_price")),
                safeParseFloat(request.getParameter("new_price")),
                safeParseInt(request.getParameter("stock")),
                request.getParameter("description"),
                request.getParameter("image"),
                true
        );
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private float parseFloat(String value) {
        try {
            return Float.parseFloat(value);
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

    protected void doShowLap(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "searchProductLaptop";
        }

        if (action.equals("searchProductLaptop")) {

            ProductDAO dao = new ProductDAO();

            ArrayList<ProductDTO> list = dao.getAllLaptop();
            ArrayList<ProductDTO> listUnder25 = dao.getLaptopUnder25();
            ArrayList<ProductDTO> listUnder30 = dao.getLaptopUnder30();
            ArrayList<ProductDTO> listHigher30 = dao.getLaptopHigher30();

            request.setAttribute("list", list);
            request.setAttribute("listUnder25", listUnder25);
            request.setAttribute("listUnder30", listUnder30);
            request.setAttribute("listTop30", listHigher30);

            request.getRequestDispatcher("BestSeller.jsp").forward(request, response);
        }
    }

    protected void doShowKeyBoard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDAO dao = new ProductDAO();

        ArrayList<ProductDTO> keyboardList = dao.getKeyboard();

        request.setAttribute("keyboardList", keyboardList);

        request.getRequestDispatcher("BestSeller3.jsp").forward(request, response);
    }

    protected void doShowMonitor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDAO dao = new ProductDAO();

        ArrayList<ProductDTO> monitorList = dao.getMonitor();
        ArrayList<ProductDTO> list24inch = dao.getMonitor24Inch();
        ArrayList<ProductDTO> list27inch = dao.getMonitor27Inch();
        ArrayList<ProductDTO> listOLED = dao.getMonitorOLED();
        System.out.println("list: " +list27inch);
        // set tất cả trước
        request.setAttribute("listProduct", monitorList);
        request.setAttribute("list24inch", list24inch);
        request.setAttribute("list27inch", list27inch);
        request.setAttribute("listOLED", listOLED);

        request.getRequestDispatcher("BestSeller4.jsp").forward(request, response);
    }

}
