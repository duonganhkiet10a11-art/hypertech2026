package model;

import java.sql.*;
import java.util.ArrayList;
import util.DbUtil;

public class ProductDiscountDAO {

    // ================= GET ALL =================
    public ArrayList<ProductDiscountDTO> getAll() {

        ArrayList<ProductDiscountDTO> list = new ArrayList<>();

        try {
            Connection con = DbUtil.getConnection();

            String sql = "SELECT * FROM ProductDiscount";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ProductDiscountDTO pd = new ProductDiscountDTO(
                        rs.getInt("id"),
                        rs.getInt("orderID"),
                        rs.getInt("productID"),
                        rs.getFloat("price"),
                        rs.getInt("quantity")
                );

                list.add(pd);
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= SEARCH BY PRODUCT =================
    public ArrayList<ProductDiscountDTO> searchByProductID(int productID) {

        ArrayList<ProductDiscountDTO> list = new ArrayList<>();

        try {
            Connection con = DbUtil.getConnection();

            String sql = "SELECT * FROM ProductDiscount WHERE productID=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, productID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ProductDiscountDTO pd = new ProductDiscountDTO(
                        rs.getInt("id"),
                        rs.getInt("orderID"),
                        rs.getInt("productID"),
                        rs.getFloat("price"),
                        rs.getInt("quantity")
                );

                list.add(pd);
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= ADD =================
    public boolean add(ProductDiscountDTO pd) {

        try {
            Connection con = DbUtil.getConnection();

            String sql = "INSERT INTO ProductDiscount(orderID,productID,price,quantity) VALUES(?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, pd.getOrderID());
            ps.setInt(2, pd.getProductID());
            ps.setFloat(3, pd.getPrice());
            ps.setInt(4, pd.getQuantity());

            int rows = ps.executeUpdate();

            con.close();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= DELETE =================
    public boolean delete(int id) {

        try {
            Connection con = DbUtil.getConnection();

            String sql = "DELETE FROM ProductDiscount WHERE id=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            con.close();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}