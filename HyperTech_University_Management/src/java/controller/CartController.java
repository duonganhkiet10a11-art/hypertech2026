package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import model.CartDAO;
import model.CartDTO;
import model.ProductDAO;
import model.ProductDTO;
import model.UserDTO;

public class CartController extends HttpServlet {

    // ===== GET USER EMAIL =====
    private String getUserEmail(HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        return user != null ? user.getEmail() : null;
    }

    // ===== MAIN PROCESS =====
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action == null) {
            action = "viewCart";
        }

        System.out.println("Cart action: " + action);

        switch (action) {

            case "viewCart":
                doViewCart(request, response);
                break;

            case "AddCart":
                doAdd(request, response);
                break;

            case "UpdateCart":
                doUpdate(request, response);
                break;

            case "RemoveCart":
                doDelete(request, response);
                break;

            case "clearCart":
                doClear(request, response);
                break;

            default:
                doViewCart(request, response);
        }
    }

    // ===== VIEW CART =====
    private void doViewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = getUserEmail(request);

        if (email == null) {
            request.getSession().setAttribute("showLoginModal", true);
            response.sendRedirect("index.jsp");
            return;
        }

        HttpSession session = request.getSession();

        CartDAO cartDAO = new CartDAO();
        ProductDAO productDAO = new ProductDAO();

        ArrayList<CartDTO> list = cartDAO.getByUserEmail(email);

        HashMap<Integer, ProductDTO> cartMap = new HashMap<>();

        for (CartDTO item : list) {

            ProductDTO product = productDAO.getByIdWithDiscount(item.getProductId());

            if (product != null) {
                product.setQuantity(item.getQuantity());
                cartMap.put(product.getId(), product);
            }
        }

        CartDTO cart = new CartDTO();
        cart.setCart(cartMap);

        session.setAttribute("CART", cart);

        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }

    // ===== ADD TO CART =====
    private void doAdd(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();

        int productId = parseInt(request.getParameter("productID"));
        int quantity = parseInt(request.getParameter("quantity"));

        if (quantity <= 0) {
            quantity = 1;
        }

        ProductDAO productDAO = new ProductDAO();
        ProductDTO product = productDAO.getByIdWithDiscount(productId);

        if (product == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        CartDTO cart = (CartDTO) session.getAttribute("CART");

        if (cart == null) {
            cart = new CartDTO();
        }

        product.setQuantity(quantity);
        cart.add(product);

        session.setAttribute("CART", cart);

        String email = getUserEmail(request);

        if (email != null) {

            CartDAO dao = new CartDAO();
            CartDTO existing = dao.getItem(email, productId);

            if (existing == null) {
                dao.insert(new CartDTO(email, productId, quantity));
            } else {
                dao.updateQuantity(email, productId, existing.getQuantity() + quantity);
            }
        }

        response.sendRedirect("MainController?action=viewCart");
    }

    // ===== UPDATE CART =====
    private void doUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String email = getUserEmail(request);

        if (email == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int productId = parseInt(request.getParameter("productID"));
        int quantity = parseInt(request.getParameter("quantity"));

        CartDAO dao = new CartDAO();

        if (quantity <= 0) {
            dao.deleteItem(email, productId);
        } else {
            dao.updateQuantity(email, productId, quantity);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }

    // ===== REMOVE ITEM =====
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String email = getUserEmail(request);

        if (email == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int productId = parseInt(request.getParameter("productID"));

        CartDAO dao = new CartDAO();
        dao.deleteItem(email, productId);

        HttpSession session = request.getSession();
        CartDTO cart = (CartDTO) session.getAttribute("CART");

        if (cart != null && cart.getCart() != null) {
            cart.remove(productId);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }

    // ===== 🔥 CLEAR CART (FIX CHUẨN) =====
    private void doClear(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String email = getUserEmail(request);

        if (email == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // ❌ Xóa DB
        CartDAO dao = new CartDAO();
        dao.clearCart(email);

        // ❌ Xóa session
        HttpSession session = request.getSession();
        session.removeAttribute("CART");

        // 🔥 QUAN TRỌNG: phải redirect
        response.sendRedirect("MainController?action=viewCart");
    }

    // ===== PARSE INT =====
    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    // ===== HTTP METHODS =====
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