package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import util.DbUtil;

public class UserDAO {

    // ================= SEARCH USER BY EMAIL =================
    public UserDTO searchByEmail(String email) {

        UserDTO user = null;
        String sql = "SELECT * FROM users WHERE email = ?";

        try (
                 Connection con = DbUtil.getConnection();  PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String username = rs.getString("username");
                String password = rs.getString("password");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                boolean status = rs.getBoolean("status");

                user = new UserDTO(email, username, password, phone, address, status);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public ArrayList<UserDTO> filterByColumn2(String column, String value) {

        ArrayList<UserDTO> result = new ArrayList<>();

        try {

            Connection conn = DbUtil.getConnection();

            String sql = "SELECT * FROM users WHERE " + column + " LIKE ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, "%" + value + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                UserDTO u = new UserDTO(
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getBoolean("status")
                );

                result.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // ================= FILTER USER =================
    public ArrayList<UserDTO> filterByColumn(String column, String value) {

        ArrayList<UserDTO> result = new ArrayList<>();

        try {

            Connection conn = DbUtil.getConnection();

            String sql = "SELECT * FROM users WHERE status = 1 AND " + column + " LIKE ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, "%" + value + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                UserDTO u = new UserDTO(
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getBoolean("status")
                );

                result.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // ================= GET ALL USERS =================
    public List<UserDTO> getAllUsers() {

        List<UserDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (
                 Connection con = DbUtil.getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery();) {

            while (rs.next()) {

                UserDTO user = new UserDTO(
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getBoolean("status")
                );

                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= LOGIN =================
    public UserDTO login(String email, String passwordInput) {

        UserDTO user = searchByEmail(email);
        if (user != null) {

            if (BCrypt.checkpw(passwordInput, user.getPassword())) {
                if (user.isStatus()) {
                    return user;
                }
            }
            return null;
        }

        return null;
    }

    // ================= REGISTER USER =================
    public boolean add(UserDTO u) {

        String sql = "INSERT INTO users (username, email, password, phone, address, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (
                 Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);) {

            String hashedPassword = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getEmail());
            ps.setString(3, hashedPassword);
            ps.setString(4, u.getPhone());
            ps.setString(5, u.getAddress());
            ps.setBoolean(6, true);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= UPDATE USER =================
    public boolean updateUser(UserDTO u) {

        int result = 0;

        try {

            Connection conn = DbUtil.getConnection();

            String sql = "UPDATE users "
                    + "SET username = ?, password = ?, phone = ?, address = ? "
                    + "WHERE email = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getPhone());
            ps.setString(4, u.getAddress());
            ps.setString(5, u.getEmail());

            result = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result > 0;
    }

    // ================= UPDATE STATUS =================
    public boolean updateUserStatus(String email, boolean status) {

        boolean check = false;
        String sql = "UPDATE users SET status = ? WHERE email = ?";

        try (
                 Connection con = DbUtil.getConnection();  PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setBoolean(1, status);
            ps.setString(2, email);

            check = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return check;
    }

    public boolean updateContact(String email, String phone, String address) {
        try {
            Connection con = DbUtil.getConnection();

            String sql = "UPDATE users SET phone=?, address=? WHERE email=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, phone);
            ps.setString(2, address);
            ps.setString(3, email);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(String email) {

        String sql = "DELETE FROM users WHERE email = ?";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= COUNT USERS =================
    public int countUsers() {

        int count = 0;

        String sql = "SELECT COUNT(*) FROM users";

        try (
                 Connection con = DbUtil.getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery();) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
    // ================= UPDATE PASSWORD =================

    public boolean updatePassword(String email, String password) {
        try {
            Connection con = DbUtil.getConnection();

            String sql = "UPDATE users SET password=? WHERE email=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, password);
            ps.setString(2, email);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean disableUser(String email) {

        String sql = "UPDATE users SET status = 0 WHERE email = ?";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
