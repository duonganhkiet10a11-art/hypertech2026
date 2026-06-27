package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.DbUtil;

public class PaymentDAO {

    // ================= GET ALL =================
    public ArrayList<PaymentDTO> getAll() {
        ArrayList<PaymentDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM payments ORDER BY paid_at DESC";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractPayment(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= GET BY ID =================
    public PaymentDTO getById(int id) {
        String sql = "SELECT * FROM payments WHERE id=?";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractPayment(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= GET BY ORDER =================
    public PaymentDTO getByOrderId(int orderId) {
        String sql = "SELECT * FROM payments WHERE order_id=?";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractPayment(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= GET BY USER =================
    public ArrayList<PaymentDTO> getByUserId(String email) {

        ArrayList<PaymentDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM payments WHERE email=? ORDER BY paid_at DESC";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractPayment(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= INSERT =================
    public boolean insert(PaymentDTO p) {

        String sql = "INSERT INTO payments "
                + "(order_id, email, payment_method, amount, status, transaction_code, paid_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getOrderId());
            ps.setString(2, p.getEmail()); // 🔥 FIX
            ps.setString(3, p.getPaymentMethod());
            ps.setFloat(4, p.getAmount());
            ps.setString(5, p.getStatus());
            ps.setString(6, p.getTransactionCode());
            ps.setDate(7, p.getPaid_at());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= UPDATE =================
    public boolean update(PaymentDTO p) {

        String sql = "UPDATE payments SET order_id=?, email=?, payment_method=?, "
                + "amount=?, status=?, transaction_code=?, paid_at=? WHERE id=?";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getOrderId());
            ps.setString(2, p.getEmail()); // 🔥 FIX
            ps.setString(3, p.getPaymentMethod());
            ps.setFloat(4, p.getAmount());
            ps.setString(5, p.getStatus());
            ps.setString(6, p.getTransactionCode());
            ps.setDate(7, p.getPaid_at());
            ps.setInt(8, p.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= UPDATE STATUS =================
    public boolean updateStatus(int id, String status) {

        String sql = "UPDATE payments SET status=? WHERE id=?";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= DELETE =================
    public boolean delete(int id) {

        String sql = "DELETE FROM payments WHERE id=?";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= COUNT =================
    public int countPayments() {

        int count = 0;

        String sql = "SELECT COUNT(*) FROM payments";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    // ================= TOTAL =================
    public float getTotalPaidAmount() {

        float total = 0;

        String sql = "SELECT SUM(amount) FROM payments WHERE status='paid'";

        try ( Connection conn = DbUtil.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getFloat(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    // ================= EXTRACT =================
    private PaymentDTO extractPayment(ResultSet rs) throws Exception {

        return new PaymentDTO(
                rs.getInt("id"),
                rs.getInt("order_id"),
                rs.getString("email"), // 🔥 FIX QUAN TRỌNG
                rs.getString("payment_method"),
                rs.getFloat("amount"),
                rs.getString("status"),
                rs.getString("transaction_code"),
                rs.getDate("paid_at")
        );
    }
}
