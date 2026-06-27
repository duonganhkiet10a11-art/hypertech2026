package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.DbUtil;

public class ComplaintDAO {

    // =====================================================
    // 1. LẤY TẤT CẢ COMPLAINT (Admin xem toàn bộ)
    // =====================================================
    public ArrayList<ComplaintDTO> getAll() {
        ArrayList<ComplaintDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM complaints";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractComplaint(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // 2. LẤY THEO ID (Xem chi tiết)
    // =====================================================
    public ArrayList<ComplaintDTO> searchByCategory(int categoryId) {
    ArrayList<ComplaintDTO> list = new ArrayList<>();

String sql = "SELECT c.* " +
             "FROM complaints c " +
             "JOIN products p ON c.product_id = p.id " +
             "WHERE p.category_id = ?";

    try (Connection conn = DbUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, categoryId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(extractComplaint(rs));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    // =====================================================
    // 3. LẤY THEO USER (User xem khiếu nại của mình)
    // =====================================================
    public ArrayList<ComplaintDTO> searchComplaints(String keyword, int categoryId) {
    ArrayList<ComplaintDTO> list = new ArrayList<>();

String sql = "SELECT c.* " +
             "FROM complaints c " +
             "JOIN products p ON c.product_id = p.id " +
             "WHERE p.name LIKE ? AND p.category_id = ?";


    try (Connection conn = DbUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, "%" + keyword + "%");
        ps.setInt(2, categoryId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(extractComplaint(rs));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    // =====================================================
    // 4. LỌC THEO TRẠNG THÁI (Admin lọc pending, resolved...)
    // =====================================================
    public ArrayList<ComplaintDTO> searchByProductName(String keyword) {
    ArrayList<ComplaintDTO> list = new ArrayList<>();

String sql = "SELECT c.* " +
             "FROM complaints c " +
             "JOIN products p ON c.product_id = p.id " +
             "WHERE p.name LIKE ?";

    try (Connection conn = DbUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(extractComplaint(rs));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    // =====================================================
    // 5. INSERT (User gửi khiếu nại)
    // =====================================================
    public boolean insert(ComplaintDTO c) {
        String sql = "INSERT INTO complaints (email, order_id, product_id, title, content) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getEmail());
            ps.setInt(2, c.getOrderId());
            ps.setInt(3, c.getProductId());
            ps.setString(4, c.getTitle());
            ps.setString(5, c.getContent());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // 6. UPDATE TOÀN BỘ (Nếu cho phép chỉnh sửa)
    // =====================================================
    public boolean update(ComplaintDTO c) {
        String sql = "UPDATE complaints SET user_id=?, order_id=?, product_id=?, "
                + "title=?, content=? WHERE id=?";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getEmail());
            ps.setInt(2, c.getOrderId());
            ps.setInt(3, c.getProductId());
            ps.setString(4, c.getTitle());
            ps.setString(5, c.getContent());
            ps.setInt(7, c.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // 7. UPDATE STATUS (Admin xử lý khiếu nại)
    // =====================================================
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE complaints SET status=? WHERE id=?";

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
    // 8. DELETE
    // =====================================================
    public boolean delete(int id) {
        String sql = "DELETE FROM complaints WHERE id=?";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // 9. ĐẾM SỐ COMPLAINT (Dashboard Admin)
    // =====================================================
    public int countComplaints() {
        String sql = "SELECT COUNT(*) FROM complaints";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // =====================================================
    // HÀM HỖ TRỢ: CHUYỂN ResultSet → ComplaintDTO
    // =====================================================
    private ComplaintDTO extractComplaint(ResultSet rs) throws Exception {
    ComplaintDTO c = new ComplaintDTO();

    c.setId(rs.getInt("id"));
    c.setEmail(rs.getString("email"));
    c.setOrderId(rs.getInt("order_id"));
    c.setProductId(rs.getInt("product_id"));
    c.setTitle(rs.getString("title"));
    c.setContent(rs.getString("content"));

    return c;
}
}
