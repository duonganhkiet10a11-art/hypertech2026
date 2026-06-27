package controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.AdminDAO;
import model.AdminDTO;

public class AdminController extends HttpServlet {

    protected void doLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String url = "header.jsp";
        String txtUsername = request.getParameter("txtUsername");
        String txtPassword = request.getParameter("txtPassword");
        AdminDAO adao = new AdminDAO();
        AdminDTO admin = adao.adLogin(txtUsername, txtPassword);
        if (admin != null) {

            session.setAttribute("admin", admin);
            url = "Admin.jsp";

        } else {

            // ================= CHECK USER =================
            // ===== KHÔNG PHẢI ADMIN → CHUYỂN QUA USERCONTROLLER =====
            url = "UserController?action=login&txtUsername=" + txtUsername + "&txtPassword=" + txtPassword;
            response.sendRedirect(url);
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }

    protected void doLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        if (session.getAttribute("user") != null) {
            session.invalidate();
        }

        response.sendRedirect("login.jsp");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action == null || action.isEmpty()) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {

            switch (action) {

                case "login":
                    doLogin(request, response);
                    break;

                case "logout":
                    doLogout(request, response);
                    break;

                default:
                    request.setAttribute("error", "Hành động không hợp lệ: " + action);
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    break;
            }

        } catch (Exception e) {

            log("Error at AdminController: " + e.toString());

            request.setAttribute("error", "Hệ thống đang gặp sự cố, vui lòng thử lại sau.");
            request.getRequestDispatcher("error.jsp").forward(request, response);

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

}
