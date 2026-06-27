package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.DbUtil;

public class CategoryDAO {

    // =====================================================
    // 1. LẤY TẤT CẢ CATEGORY ACTIVE
    // =====================================================
    public ArrayList<CategoryDTO> getAll() {

        ArrayList<CategoryDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM categories WHERE status = 1";

        try ( Connection con = DbUtil.getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractCategory(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // 2. LẤY CATEGORY THEO ID
    // =====================================================
    public CategoryDTO getById(int id) {

        String sql = "SELECT * FROM categories WHERE id = ? AND status = 1";

        try ( Connection con = DbUtil.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try ( ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return extractCategory(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // 3. SEARCH CATEGORY THEO TÊN
    // =====================================================
    public ArrayList<CategoryDTO> searchByName(String name) {

        ArrayList<CategoryDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM categories WHERE LOWER(name) LIKE LOWER(?) AND status = 1";

        try ( Connection con = DbUtil.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");

            try ( ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(extractCategory(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // 4. THÊM CATEGORY
    // =====================================================
    public boolean insert(CategoryDTO c) {

        String sql = "INSERT INTO categories (name, description, status) VALUES (?, ?, 1)";

        try ( Connection con = DbUtil.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // 5. UPDATE CATEGORY
    // =====================================================
    public boolean update(CategoryDTO c) {

        String sql = "UPDATE categories SET name = ?, description = ? WHERE id = ?";

        try ( Connection con = DbUtil.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.setInt(3, c.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // 6. SOFT DELETE CATEGORY
    // =====================================================
    public boolean softDelete(int id) {

        String sql = "UPDATE categories SET status = 0 WHERE id = ?";

        try ( Connection con = DbUtil.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // HÀM MAP RESULTSET → DTO
    // =====================================================
    private CategoryDTO extractCategory(ResultSet rs) throws Exception {

        return new CategoryDTO(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBoolean("status")
        );
    }
}
