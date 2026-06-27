package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.DbUtil;

public class OrderDAO {

    // =====================================================
    // 1. INSERT ORDER (TRẢ VỀ ORDER ID MỚI TẠO)
    // =====================================================
    public int insert(OrderDTO order) {

        String sql = "INSERT INTO orders(email, total_price, status) VALUES (?, ?, ?)";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // 🔥 FIX: userID -> email
            ps.setString(1, order.getEmail());
            ps.setDouble(2, order.getTotalPrice());
            ps.setString(3, order.getStatus());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // =====================================================
    // 2. LẤY TẤT CẢ ORDER
    // =====================================================
    public ArrayList<OrderDTO> getAll() {

        ArrayList<OrderDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM orders ORDER BY created_at DESC";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractOrder(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // 3. LẤY ORDER THEO ID
    // =====================================================
    public OrderDTO getById(int id) {

        String sql = "SELECT * FROM orders WHERE id=?";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractOrder(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // 4. LẤY ORDER THEO EMAIL
    // =====================================================
    public ArrayList<OrderDTO> getOrdersByEmail(String email) {

        ArrayList<OrderDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM orders WHERE email=? ORDER BY created_at DESC";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractOrder(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<OrderDTO> searchOrders(String keywords) {

        ArrayList<OrderDTO> list = new ArrayList<>();

        String sql;
        System.out.println("dao: "+ keywords);
        if (keywords == null || keywords.trim().isEmpty()) {
            sql = "SELECT * FROM orders";
        } else {
            sql = "SELECT * FROM orders WHERE email LIKE ? ";
        }

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            if (keywords != null && !keywords.trim().isEmpty()) {
                ps.setString(1, "%" + keywords.trim() + "%");
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractOrder(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // 5. SEARCH ORDER
    // =====================================================
    public ArrayList<OrderDTO> searchByColumn(String column, String value) {

        ArrayList<OrderDTO> result = new ArrayList<>();

        if (!column.equals("id")
                && !column.equals("email")
                && !column.equals("status")) {
            return result;
        }

        String sql = "SELECT * FROM orders WHERE " + column + " = ?";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, value);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(extractOrder(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // =====================================================
    // 6. UPDATE ORDER
    // =====================================================
    public boolean update(OrderDTO order) {

        String sql = "UPDATE orders SET email=?, total_price=?, status=? WHERE id=?";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            // 🔥 FIX
            ps.setString(1, order.getEmail());
            ps.setDouble(2, order.getTotalPrice());
            ps.setString(3, order.getStatus());
            ps.setInt(4, order.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // 7. UPDATE STATUS
    // =====================================================
    public boolean updateStatus(int id, String status) {

        String sql = "UPDATE orders SET status=? WHERE id=?";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // 8. DELETE ORDER
    // =====================================================
    public boolean delete(int id) {

        String sql = "DELETE FROM orders WHERE id=?";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // 9. TOTAL REVENUE
    // =====================================================
    public double getTotalRevenue() {

        double total = 0;

        String sql = "SELECT SUM(total_price) AS total FROM orders WHERE status='completed'";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    // =====================================================
    // 10. TOTAL ORDERS
    // =====================================================
    public int getTotalOrders() {

        int count = 0;

        String sql = "SELECT COUNT(*) AS total FROM orders";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    // =====================================================
    // 11. EXTRACT ORDER
    // =====================================================
    private OrderDTO extractOrder(ResultSet rs) throws Exception {

        return new OrderDTO(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getDouble("total_price"),
                rs.getString("status"),
                rs.getTimestamp("created_at")
        );
    }
}
