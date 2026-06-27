package controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import model.CategoryDAO;
import model.CategoryDTO;

public class CategoryController extends HttpServlet {

    // ===== CHECK ADMIN =====
    private boolean isAdmin(HttpServletRequest request) {
        return request.getSession().getAttribute("admin") != null;
    }

    // ===== CHECK LOGIN =====
    private boolean isLoggedIn(HttpServletRequest request) {
        return request.getSession().getAttribute("user") != null
                || request.getSession().getAttribute("admin") != null;
    }

    // ===== MAIN PROCESS =====
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!isLoggedIn(request)) {
            response.sendRedirect("index.jsp");
            return;
        }

        String action = request.getParameter("action");

        if (action == null) {
            action = "searchCategory";
        }

        switch (action) {

            case "searchCategory":
                doSearch(request, response);
                break;

            case "viewCategory":
                doView(request, response);
                break;

            case "showAddCategoryForm":
                request.getRequestDispatcher("category-add.jsp").forward(request, response);
                break;

            case "showUpdateCategoryForm":
                showUpdateForm(request, response);
                break;

            case "addCategory":
                if (isAdmin(request)) {
                    doAdd(request, response);
                } else {
                    deny(response);
                }
                break;

            case "updateCategory":
                if (isAdmin(request)) {
                    doUpdate(request, response);
                } else {
                    deny(response);
                }
                break;

            case "deleteCategory":
                if (isAdmin(request)) {
                    doDelete(request, response);
                } else {
                    deny(response);
                }
                break;

            default:
                doSearch(request, response);
        }
    }

    // ===== SEARCH CATEGORY =====
    private void doSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");

        if (keyword == null) {
            keyword = "";
        }

        CategoryDAO dao = new CategoryDAO();
        ArrayList<CategoryDTO> list;

        if (!keyword.trim().isEmpty()) {
            list = dao.searchByName(keyword);
        } else {
            list = dao.getAll();
        }

        request.setAttribute("list", list);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("category-search.jsp").forward(request, response);
    }

    // ===== VIEW CATEGORY DETAIL =====
    private void doView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = parseInt(request.getParameter("id"));

        CategoryDAO dao = new CategoryDAO();
        CategoryDTO category = dao.getById(id);

        if (category == null) {
            response.sendRedirect("MainController?action=searchCategory");
            return;
        }

        request.setAttribute("category", category);

        request.getRequestDispatcher("category-detail.jsp").forward(request, response);
    }

    // ===== SHOW UPDATE FORM =====
    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = parseInt(request.getParameter("id"));

        CategoryDAO dao = new CategoryDAO();
        CategoryDTO category = dao.getById(id);

        if (category == null) {
            response.sendRedirect("MainController?action=searchCategory");
            return;
        }

        request.setAttribute("category", category);

        request.getRequestDispatcher("category-update.jsp").forward(request, response);
    }

    // ===== ADD CATEGORY =====
    private void doAdd(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        CategoryDTO c = extractCategory(request);

        CategoryDAO dao = new CategoryDAO();

        if (dao.insert(c)) {
            response.sendRedirect("MainController?action=searchCategory");
        } else {
            response.sendRedirect("MainController?action=searchCategory&error=addFail");
        }
    }

    // ===== UPDATE CATEGORY =====
    private void doUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        CategoryDTO c = extractCategory(request);

        CategoryDAO dao = new CategoryDAO();

        if (dao.update(c)) {
            response.sendRedirect("MainController?action=searchCategory");
        } else {
            response.sendRedirect("MainController?action=searchCategory&error=updateFail");
        }
    }

    // ===== DELETE CATEGORY (SOFT DELETE) =====
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = parseInt(request.getParameter("id"));

        CategoryDAO dao = new CategoryDAO();

        if (dao.softDelete(id)) {
            response.sendRedirect("MainController?action=searchCategory");
        } else {
            response.sendRedirect("MainController?action=searchCategory&error=deleteFail");
        }
    }

    // ===== EXTRACT CATEGORY FROM REQUEST =====
    private CategoryDTO extractCategory(HttpServletRequest request) {

        int id = parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        return new CategoryDTO(id, name, description, true);
    }

    // ===== PARSE INT SAFE =====
    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    // ===== ACCESS DENIED =====
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
