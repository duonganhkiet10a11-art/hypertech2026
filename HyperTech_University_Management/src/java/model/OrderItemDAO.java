package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.DbUtil;

public class OrderItemDAO {

    // =====================================================
    // 1. LẤY DANH SÁCH ITEM THEO ORDER (RẤT QUAN TRỌNG)
    // =====================================================
    public ArrayList<OrderItemDTO> getByOrderId(int orderId) {
        ArrayList<OrderItemDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractItem(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // 2. LẤY THEO ID
    // =====================================================
    public OrderItemDTO getById(int id) {
        String sql = "SELECT * FROM order_items WHERE id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractItem(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // 3. INSERT (Tạo khi checkout)
    // =====================================================
    public boolean insert(OrderItemDTO item) {
        String sql = "INSERT INTO order_items(order_id, product_id, price, quantity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, item.getOrderID());
            ps.setInt(2, item.getProductID());
            ps.setFloat(3, (float) item.getPrice());
            ps.setInt(4, item.getQuantity());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // 4. DELETE THEO ORDER (Xóa toàn bộ item khi xóa order)
    // =====================================================
    public boolean deleteByOrderId(int orderId) {
        String sql = "DELETE FROM order_items WHERE order_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // 5. TỔNG TIỀN CỦA 1 ORDER
    // =====================================================
    public float getTotalAmountByOrder(int orderId) {
        float total = 0;
        String sql = "SELECT SUM(price * quantity) FROM order_items WHERE order_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getFloat(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    // =====================================================
    // 6. TỔNG SỐ LƯỢNG BÁN
    // =====================================================
    public int getTotalSoldQuantity() {
        int total = 0;
        String sql = "SELECT SUM(quantity) FROM order_items";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    // =====================================================
    // 7. TOP 5 SẢN PHẨM BÁN CHẠY
    // =====================================================
    public ArrayList<Integer> getTopSellingProductIDs() {
        ArrayList<Integer> list = new ArrayList<>();

        String sql = "SELECT product_id, SUM(quantity) AS total "
                   + "FROM order_items "
                   + "GROUP BY product_id "
                   + "ORDER BY total DESC";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int count = 0;
            while (rs.next() && count < 5) {
                list.add(rs.getInt("product_id"));
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // HÀM HỖ TRỢ
    // =====================================================
    private OrderItemDTO extractItem(ResultSet rs) throws Exception {
        return new OrderItemDTO(
                rs.getInt("id"),
                rs.getInt("order_id"),
                rs.getInt("product_id"),
                rs.getFloat("price"),
                rs.getInt("quantity")
        );
    }
}