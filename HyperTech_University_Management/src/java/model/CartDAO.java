package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.DbUtil;

public class CartDAO {

    // ==========================================
    // 1. LẤY GIỎ HÀNG THEO EMAIL
    // ==========================================
    public ArrayList<CartDTO> getByUserEmail(String email) {

        ArrayList<CartDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM cart WHERE email = ?";

        try (
                 Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                CartDTO cart = new CartDTO(
                        rs.getString("email"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                );

                list.add(cart);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ==========================================
    // 2. KIỂM TRA ITEM ĐÃ TỒN TẠI CHƯA
    // ==========================================
    public CartDTO getItem(String email, int productId) {

        String sql = "SELECT * FROM cart WHERE email = ? AND product_id = ?";

        try (
                 Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setInt(2, productId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new CartDTO(
                        rs.getString("email"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ==========================================
    // 3. THÊM VÀO CART
    // ==========================================
    public boolean insert(CartDTO cart) {

        String sql = "INSERT INTO cart (email, product_id, quantity) VALUES (?, ?, ?)";

        try (
                 Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cart.getEmail());
            ps.setInt(2, cart.getProductId());
            ps.setInt(3, cart.getQuantity());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================================
    // 4. UPDATE QUANTITY
    // ==========================================
    public boolean updateQuantity(String email, int productId, int quantity) {

        String sql = "UPDATE cart SET quantity = ? WHERE email = ? AND product_id = ?";

        try (
                 Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setString(2, email);
            ps.setInt(3, productId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================================
    // 5. XÓA ITEM
    // ==========================================
    public boolean deleteItem(String email, int productId) {

        String sql = "DELETE FROM cart WHERE email = ? AND product_id = ?";

        try (
                 Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================================
    // 6. CLEAR CART
    // ==========================================
    public boolean clearCart(String email) {

        String sql = "DELETE FROM cart WHERE email = ?";

        try (
                 Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
