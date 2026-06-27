package controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import model.UserDAO;
import model.UserDTO;

public class UserController extends HttpServlet {

    // ================= LOGIN =================
    protected void doLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain"); // 🔥 QUAN TRỌNG

        String email = request.getParameter("txtUsername");
        String password = request.getParameter("txtPassword");

        UserDAO dao = new UserDAO();
        UserDTO user = dao.login(email, password);
        HttpSession session = request.getSession();

        if (user != null) {
            session.setAttribute("user", user);

            // ✅ trả về cho JS
            response.getWriter().write("success");

        } else {
            // ❌ KHÔNG redirect nữa
            response.getWriter().write("error");
        }
    }

    // ================= LOGOUT =================
    protected void doLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect("index.jsp");
    }

    // ================= SEARCH USER =================
    protected void doSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");

        if (keyword == null) {
            keyword = "";
        }

        UserDAO dao = new UserDAO();
        ArrayList<UserDTO> list;

        if (keyword.trim().isEmpty()) {
            list = new ArrayList<>(dao.getAllUsers());
        } else {
            list = dao.filterByColumn2("email", keyword);
        }

        request.setAttribute("list", list);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("user.jsp").forward(request, response);
    }

    // ================= LOAD USER TO UPDATE =================
    protected void doUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        UserDAO dao = new UserDAO();

        UserDTO u = dao.searchByEmail(email);

        if (u != null) {

            request.setAttribute("u", u);
            request.setAttribute("mode", "update");

            request.getRequestDispatcher("university-form.jsp").forward(request, response);

        } else {

            request.setAttribute("error", "Không tìm thấy user!");
            request.getRequestDispatcher("search.jsp").forward(request, response);
        }
    }

    // ================= EXTRACT USER =================
    private UserDTO extractUserFromRequest(HttpServletRequest request) {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        boolean status = true;

        return new UserDTO(email, username, password, phone, address, status);
    }

    private boolean checkpass(HttpServletRequest request) {
        String password = request.getParameter("password");
        String passcom = request.getParameter("confirm_password");

        return password.equalsIgnoreCase(passcom);
    }

    // ================= VALIDATE =================
    private String validateUser(UserDTO u, boolean isUpdate) {

        StringBuilder error = new StringBuilder();

        if (u.getUsername() == null || u.getUsername().trim().isEmpty()) {
            error.append("Chưa nhập username <br>");
        }

        if (u.getEmail() == null || u.getEmail().trim().isEmpty()) {
            error.append("Chưa nhập email <br>");
        }

        if (!isUpdate) {

            UserDAO dao = new UserDAO();

            if (dao.searchByEmail(u.getEmail()) != null) {
                error.append("Email đã tồn tại <br>");
            }
        }

        return error.toString();
    }

    // ================= ADD USER =================
    protected void doAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserDTO u = extractUserFromRequest(request);
        String error = validateUser(u, false);
        String msg = "";

        // 🔥 THÊM VALIDATE EMAIL Ở ĐÂY
        String email = request.getParameter("email");
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            error += "Email không hợp lệ <br>";
        }

        if (checkpass(request)) {

            if (error.isEmpty()) {

                UserDAO dao = new UserDAO();

                if (dao.add(u)) {
                    msg = "Thêm user thành công!";

                    // 🔥 GỬI MAIL CHÀO MỪNG (THÊM NGAY ĐÂY)
                    String subject = "🎉 Chào mừng bạn đến với TKT Shop";

                    String content = "<h2>Xin chào " + u.getUsername() + " 👋</h2>"
                            + "<p>Cảm ơn bạn đã đăng ký tài khoản tại <b>TKT Shop</b> 💖</p>"
                            + "<p>Chúng tôi rất vui khi được phục vụ bạn!</p>"
                            + "<hr>"
                            + "<p>Chúc bạn mua sắm vui vẻ 🛒</p>";

                    util.MailUtil.sendEmail(u.getEmail(), subject, content);

                } else {
                    error = "Không thể thêm user!";
                }
            }

        } else {
            error = "Password is incorrect!";
        }

        request.setAttribute("u", u);
        request.setAttribute("msg", msg);
        request.setAttribute("error", error);

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
// ================= FORGOT PASSWORD =================

    protected void doForgotPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        // 🔥 VALIDATE Ở CONTROLLER
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            request.getSession().setAttribute("message", "Email không hợp lệ!");
            response.sendRedirect("index.jsp");
            return;
        }
        UserDAO dao = new UserDAO();
        UserDTO user = dao.searchByEmail(email);

        if (user != null) {

            // 🔥 tạo password mới
            String newPass = String.format("%06d", (int) (Math.random() * 1000000));

            // hash password
            String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(newPass, org.mindrot.jbcrypt.BCrypt.gensalt());

            // update DB
            dao.updatePassword(email, hashed);

            // gửi mail
            String subject = "🔐 Reset mật khẩu";
            String content = "<div style='font-family:sans-serif'>"
                    + "<h2>🔐 Reset mật khẩu</h2>"
                    + "<p>Xin chào <b>" + user.getUsername() + "</b>,</p>"
                    + "<p>Mật khẩu mới của bạn là:</p>"
                    + "<h1 style='color:red'>" + newPass + "</h1>"
                    + "<p>👉 Vui lòng đăng nhập và đổi lại mật khẩu để bảo mật tài khoản.</p>"
                    + "<hr>"
                    + "<p>💖 Cảm ơn bạn đã tin tưởng TKT Shop</p>"
                    + "</div>";

            util.MailUtil.sendEmail(email, subject, content);

            request.getSession().setAttribute("message", "Đã gửi mật khẩu mới về email!");

        } else {
            request.getSession().setAttribute("message", "Email không tồn tại!");
        }

        response.sendRedirect("index.jsp");
    }

    // ================= SAVE UPDATE =================
    protected void doSaveUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserDTO u = extractUserFromRequest(request);

        String error = validateUser(u, true);
        String msg = "";

        if (error.isEmpty()) {

            UserDAO dao = new UserDAO();

            if (dao.updateUser(u)) {
                msg = "Cập nhật thành công!";
            } else {
                error = "Không thể cập nhật!";
            }
        }

        request.setAttribute("u", u);
        request.setAttribute("mode", "update");
        request.setAttribute("msg", msg);
        request.setAttribute("error", error);

        request.getRequestDispatcher("university-form.jsp").forward(request, response);
    }
// ================= DELETE USER =================

    @Override
    // ================= DELETE USER (SOFT DELETE) =================
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String keyword = request.getParameter("keyword");

        if (keyword == null) {
            keyword = "";
        }

        UserDAO dao = new UserDAO();

        // 🔥 đổi sang update status = 0
        boolean success = dao.disableUser(email);

        if (success) {
            response.sendRedirect("UserController?action=searchUserByAd&keyword=" + keyword);
        } else {
            request.setAttribute("error", "Không thể xóa user!");
            request.getRequestDispatcher("user.jsp").forward(request, response);
        }
    }

    // ================= GET ALL USERS =================
    protected void doGetAll(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserDAO dao = new UserDAO();

        ArrayList<UserDTO> list = new ArrayList<>(dao.getAllUsers());

        request.setAttribute("list", list);

        request.getRequestDispatcher("search.jsp").forward(request, response);
    }

    // ================= MAIN CONTROLLER =================
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action == null) {
            doSearch(request, response);
            return;
        }

        switch (action) {

            case "login":
                doLogin(request, response);
                break;

            case "Userlogout":
                doLogout(request, response);
                break;

            case "addUser":
                doAdd(request, response);
                break;
            case "forgotPassword":
                doForgotPassword(request, response);
                break;
            case "deleteUser":
                doDelete(request, response);
                break;

            case "updateUser":
                doUpdate(request, response);
                break;

            case "saveUpdateUser":
                doSaveUpdate(request, response);
                break;

            case "getAllUsers":
                doGetAll(request, response);
                break;

            case "searchUserByAd":
                doSearch(request, response);
                break;
            default:
                doSearch(request, response);
                break;
        }
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

    @Override
    public String getServletInfo() {
        return "User Controller";
    }
}
