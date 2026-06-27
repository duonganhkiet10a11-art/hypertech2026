package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.DbUtil;

public class DiscountDAO {

    // =====================================================
    // 1. LẤY TẤT CẢ DISCOUNT (Admin)
    // =====================================================
    public ArrayList<DiscountDTO> getAll() {
        ArrayList<DiscountDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM discounts";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractDiscount(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // 2. LẤY THEO ID
    // =====================================================
    public DiscountDTO getById(int id) {
        String sql = "SELECT * FROM discounts WHERE id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractDiscount(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // 3. LẤY THEO TÊN (User nhập mã)
    // =====================================================
    public DiscountDTO getByName(String name) {
        String sql = "SELECT * FROM discounts WHERE name = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractDiscount(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // 4. KIỂM TRA MÃ CÒN HIỆU LỰC
    // =====================================================
    public DiscountDTO getValidDiscount(String name) {
        String sql = "SELECT * FROM discounts "
                   + "WHERE name = ? "
                   + "AND GETDATE() BETWEEN start_date AND end_date";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractDiscount(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // 5. INSERT
    // =====================================================
    public boolean insert(DiscountDTO discount) {
        String sql = "INSERT INTO discounts (name, discount_percent, start_date, end_date) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, discount.getName());
            ps.setInt(2, discount.getDiscount_percent());
            ps.setDate(3, discount.getStart_date());
            ps.setDate(4, discount.getEnd_date());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // 6. UPDATE
    // =====================================================
    public boolean update(DiscountDTO discount) {
        String sql = "UPDATE discounts SET name=?, discount_percent=?, "
                   + "start_date=?, end_date=? WHERE id=?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, discount.getName());
            ps.setInt(2, discount.getDiscount_percent());
            ps.setDate(3, discount.getStart_date());
            ps.setDate(4, discount.getEnd_date());
            ps.setInt(5, discount.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // 7. DELETE
    // =====================================================
    public boolean delete(int id) {
        String sql = "DELETE FROM discounts WHERE id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // 8. ĐẾM SỐ DISCOUNT (Dashboard)
    // =====================================================
    public int countDiscounts() {
        String sql = "SELECT COUNT(*) FROM discounts";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // =====================================================
    // HÀM HỖ TRỢ
    // =====================================================
    private DiscountDTO extractDiscount(ResultSet rs) throws Exception {
        return new DiscountDTO(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("discount_percent"),
                rs.getDate("start_date"),
                rs.getDate("end_date")
        );
    }
}