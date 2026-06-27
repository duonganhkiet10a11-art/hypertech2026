package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.DbUtil;

public class ProductDAO {

    public ArrayList<ProductDTO> getAll() {
        ArrayList<ProductDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try ( Connection con = DbUtil.getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

public ProductDTO getById(int id) {

    String sql = "SELECT * FROM products WHERE id=?";

    try (Connection con = DbUtil.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new ProductDTO(
                    rs.getInt("id"),
                    rs.getInt("category_id"),
                    rs.getString("name"),
                    rs.getString("cpu"),
                    rs.getString("gpu"),
                    rs.getString("ram"),
                    rs.getString("ssd"),
                    rs.getString("screen"),
                    rs.getString("refresh_rate"),
                    rs.getFloat("old_price"),
                    rs.getFloat("new_price"),
                    rs.getInt("stock"),
                    rs.getString("description"),
                    rs.getString("image"),
                    rs.getBoolean("status")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}


    public ArrayList<ProductDTO> searchByName(String name) {
        ArrayList<ProductDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ? AND status = 1";

        try ( Connection con = DbUtil.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<ProductDTO> searchByNamepro(String name, int category_id) {
    ArrayList<ProductDTO> list = new ArrayList<>();

    String sql = "SELECT * FROM products WHERE name LIKE ? AND category_id=?";

    try (Connection con = DbUtil.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, "%" + name + "%");
        ps.setInt(2, category_id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(extractProduct(rs));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    public boolean add(ProductDTO p) {

        String sqlLaptop = "INSERT INTO products "
                + "(category_id,name,cpu,gpu,ram,ssd,screen,refresh_rate,old_price,new_price,stock,description,image) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        String sqlOther = "INSERT INTO products "
                + "(category_id,name,old_price,new_price,stock,description,image) "
                + "VALUES (?,?,?,?,?,?,?)";

        try ( Connection con = DbUtil.getConnection()) {

            PreparedStatement ps;

            // ===== nếu là laptop =====
            if (p.getCategory_id() == 1) {

                ps = con.prepareStatement(sqlLaptop);

                ps.setInt(1, p.getCategory_id());
                ps.setString(2, p.getName());
                ps.setString(3, p.getCpu());
                ps.setString(4, p.getGpu());
                ps.setString(5, p.getRam());
                ps.setString(6, p.getSsd());
                ps.setString(7, p.getScreen());
                ps.setString(8, p.getRefresh_rate());
                ps.setFloat(9, p.getOld_price());
                ps.setFloat(10, p.getNew_price());
                ps.setInt(11, p.getStock());
                ps.setString(12, p.getDescription());
                ps.setString(13, p.getImage());

            } // ===== sản phẩm khác (chuột, bàn phím...) =====
            else {

                ps = con.prepareStatement(sqlOther);

                ps.setInt(1, p.getCategory_id());
                ps.setString(2, p.getName());
                ps.setFloat(3, p.getOld_price());
                ps.setFloat(4, p.getNew_price());
                ps.setInt(5, p.getStock());
                ps.setString(6, p.getDescription());
                ps.setString(7, p.getImage());
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(ProductDTO p) {

        String sqlLaptop = "UPDATE products SET "
                + "category_id=?, name=?, cpu=?, gpu=?, ram=?, ssd=?, "
                + "screen=?, refresh_rate=?, old_price=?, new_price=?, "
                + "stock=?, description=?, image=? "
                + "WHERE id=?";

        String sqlOther = "UPDATE products SET "
                + "category_id=?, name=?, old_price=?, new_price=?, "
                + "stock=?, description=?, image=? "
                + "WHERE id=?";

        try ( Connection con = DbUtil.getConnection()) {

            PreparedStatement ps;
            
            // ===== nếu là laptop =====
            if (p.getCategory_id() == 1) {

                ps = con.prepareStatement(sqlLaptop);

                ps.setInt(1, p.getCategory_id());
                ps.setString(2, p.getName());
                ps.setString(3, p.getCpu());
                ps.setString(4, p.getGpu());
                ps.setString(5, p.getRam());
                ps.setString(6, p.getSsd());
                ps.setString(7, p.getScreen());
                ps.setString(8, p.getRefresh_rate());
                ps.setFloat(9, p.getOld_price());
                ps.setFloat(10, p.getNew_price());
                ps.setInt(11, p.getStock());
                ps.setString(12, p.getDescription());
                ps.setString(13, p.getImage());
                ps.setInt(14, p.getId());

            } // ===== phụ kiện (chuột, bàn phím...) =====
            else {

                ps = con.prepareStatement(sqlOther);

                ps.setInt(1, p.getCategory_id());
                ps.setString(2, p.getName());
                ps.setFloat(3, p.getOld_price());
                ps.setFloat(4, p.getNew_price());
                ps.setInt(5, p.getStock());
                ps.setString(6, p.getDescription());
                ps.setString(7, p.getImage());
                ps.setInt(8, p.getId());
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public boolean delete(int id) {
        String sql = "UPDATE products SET status = 0 WHERE id = ?";

        try ( Connection con = DbUtil.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int countProducts() {
        String sql = "SELECT COUNT(*) FROM products WHERE status = 1";

        try ( Connection con = DbUtil.getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public ArrayList<ProductDTO> getAllWithDiscount() {

        ArrayList<ProductDTO> list = new ArrayList<>();

        String sql = "SELECT p.*, d.discount_percent "
                + "FROM products p "
                + "LEFT JOIN product_discounts pd ON p.id = pd.product_id "
                + "LEFT JOIN discounts d ON pd.discount_id = d.id "
                + "AND GETDATE() BETWEEN d.start_date AND d.end_date "
                + "WHERE p.status = 1";

        try ( Connection con = DbUtil.getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //discount 
    public ProductDTO getByIdWithDiscount(int id) {
        ProductDTO p = null;

        try {
            Connection con = DbUtil.getConnection();

            String sql = "SELECT p.*, d.discount_percent "
                    + "FROM products p "
                    + "LEFT JOIN product_discounts pd ON p.id = pd.product_id "
                    + "LEFT JOIN discounts d ON pd.discount_id = d.id "
                    + "WHERE p.id = ? "
                    + "AND (d.start_date IS NULL OR GETDATE() BETWEEN d.start_date AND d.end_date)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                p = new ProductDTO(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        rs.getFloat("old_price"),
                        rs.getFloat("new_price"),
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getBoolean("status")
                );

                // 🔥 FIX NULL DISCOUNT
                Integer discount = (Integer) rs.getObject("discount_percent");

                if (discount == null) {
                    discount = 0;
                }

                p.setDiscountPercent(discount);

                System.out.println("DEBUG discount = " + discount);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    // =====================================================
    // HÀM HỖ TRỢ
    // =====================================================
    private ProductDTO extractProduct(ResultSet rs) throws Exception {

        ProductDTO p = new ProductDTO(
                rs.getInt("id"),
                rs.getInt("category_id"),
                rs.getString("name"),
                rs.getString("cpu"),
                rs.getString("gpu"),
                rs.getString("ram"),
                rs.getString("ssd"),
                rs.getString("screen"),
                rs.getString("refresh_rate"),
                rs.getFloat("old_price"),
                rs.getFloat("new_price"),
                rs.getInt("stock"),
                rs.getString("description"),
                rs.getString("image"),
                rs.getBoolean("status")
        );

        // ===== THÊM ĐOẠN NÀY =====
        return p;
    }

    public ArrayList<ProductDTO> getByCategory(int category_id) {

        ArrayList<ProductDTO> list = new ArrayList<>();

        try {
            Connection con = DbUtil.getConnection();
            String sql = "SELECT * FROM products WHERE category_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, category_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ProductDTO p = new ProductDTO(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        rs.getFloat("old_price"),
                        rs.getFloat("new_price"),
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getBoolean("status")
                );

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean addLaptop(ProductDTO l) {

        String sql = "INSERT INTO products(name,cpu,gpu,ram,ssd,screen,refresh_rate,old_price,new_price,image_url) VALUES(?,?,?,?,?,?,?,?,?,?)";

        try {
            Connection con = DbUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, l.getName());
            ps.setString(2, l.getCpu());
            ps.setString(3, l.getGpu());
            ps.setString(4, l.getRam());
            ps.setString(5, l.getSsd());
            ps.setString(6, l.getScreen());
            ps.setString(7, l.getRefresh_rate());
            ps.setFloat(8, l.getOld_price());
            ps.setFloat(9, l.getNew_price());
            ps.setString(10, l.getImage());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<ProductDTO> getAllLaptop() {

        ArrayList<ProductDTO> list = new ArrayList<>();

        try {
            Connection con = DbUtil.getConnection();
            String sql = "SELECT * FROM products";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ProductDTO l = new ProductDTO(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        rs.getFloat("old_price"),
                        rs.getFloat("new_price"),
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getBoolean("status"));

                list.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<ProductDTO> getLaptopUnder25() {

        ArrayList<ProductDTO> list = new ArrayList<>();

        try {
            Connection con = DbUtil.getConnection();
            String sql = "SELECT * FROM products WHERE new_price > 18000000 AND new_price <= 25000000 AND category_id = 1";  
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ProductDTO l = new ProductDTO(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        rs.getFloat("old_price"),
                        rs.getFloat("new_price"),
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getBoolean("status"));

                list.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<ProductDTO> getLaptopUnder30() {

        ArrayList<ProductDTO> list = new ArrayList<>();

        try {
            Connection con = DbUtil.getConnection();        
            String sql = "SELECT * FROM products "
                    + "WHERE new_price > 25000000 AND new_price <= 30000000 "
                    + "AND category_id = 1 ";         
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ProductDTO l = new ProductDTO(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        rs.getFloat("old_price"),
                        rs.getFloat("new_price"),
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getBoolean("status"));

                list.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<ProductDTO> getLaptopHigher30() {

        ArrayList<ProductDTO> list = new ArrayList<>();

        try {
            Connection con = DbUtil.getConnection();
            String sql = "SELECT * FROM products "
                    + "WHERE new_price > 30000000 "
                    + "AND category_id = 1 ";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ProductDTO l = new ProductDTO(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        rs.getFloat("old_price"),
                        rs.getFloat("new_price"),
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getBoolean("status"));

                list.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<ProductDTO> getDealMouse() {

        ArrayList<ProductDTO> list = new ArrayList<>();

        try {
            Connection conn = DbUtil.getConnection();

            String sql = "SELECT TOP 12 * FROM products "
                    + "WHERE category_id = 4 "
                    + "AND new_price < 1000000 "
                    + "AND name NOT LIKE N'%Chuột văn phòng%' "
                    + "ORDER BY new_price ASC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                float oldPrice = rs.getFloat("old_price");   // lấy giá cũ

                double newPrice = oldPrice * 0.8;            // giảm 20% cho Deal hời

                ProductDTO p = new ProductDTO(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        oldPrice, // giá cũ
                        (float) newPrice, // giá mới sau khi giảm
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getBoolean("status")
                );

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<ProductDTO> getGamingMouse() {

        ArrayList<ProductDTO> list = new ArrayList<>();

        try {
            Connection conn = DbUtil.getConnection();

            String sql = "SELECT TOP 12 * FROM products "
                    + "WHERE category_id = 4 AND new_price > 1000000 "
                    + "ORDER BY new_price ASC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                float oldPrice = rs.getFloat("old_price");
                double newPrice = oldPrice * 0.95;   // giảm 5%

                ProductDTO p = new ProductDTO(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        oldPrice,
                        (float) newPrice,
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getBoolean("status")
                );

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<ProductDTO> getOfficeMouse() {

        ArrayList<ProductDTO> list = new ArrayList<>();

        try {
            Connection conn = DbUtil.getConnection();

            String sql = "SELECT TOP 12 * FROM products "
                    + "WHERE category_id = 4 "
                    + "AND name LIKE N'%Chuột văn phòng%' "
                    + "ORDER BY new_price ASC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                float oldPrice = rs.getFloat("old_price");
                double newPrice = oldPrice * 0.98;   // giảm 2%

                ProductDTO p = new ProductDTO(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        oldPrice,
                        (float) newPrice,
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getBoolean("status")
                );

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<ProductDTO> getKeyboard() {

        ArrayList<ProductDTO> list = new ArrayList<>();

        try {
            Connection conn = DbUtil.getConnection();

            String sql = "SELECT * FROM products "
                    + "WHERE category_id = 3 "
                    + "AND name LIKE N'%Bàn phím%' "
                    + "ORDER BY new_price ASC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                float oldPrice = rs.getFloat("old_price");
                double newPrice = oldPrice * 0.95;   // giảm 5%

                ProductDTO p = new ProductDTO(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        oldPrice,
                        (float) newPrice,
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getBoolean("status")
                );

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<ProductDTO> getMonitor() {

        ArrayList<ProductDTO> list = new ArrayList<>();

        try {
            Connection conn = DbUtil.getConnection();

            String sql = "SELECT * FROM products "
                    + "WHERE category_id = 2 "
                    + "AND id IN ( "
                    + "   SELECT TOP 11 id FROM products "
                    + "   WHERE category_id = 5 "
                    + "   ORDER BY id ASC "
                    + ") "
                    + "ORDER BY id ASC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                float oldPrice = rs.getFloat("old_price");
                double newPrice = oldPrice * 0.7;   // 

                ProductDTO p = new ProductDTO(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        oldPrice,
                        (float) newPrice,
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getBoolean("status")
                );

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<ProductDTO> getMonitor24Inch() {
        ArrayList<ProductDTO> list = new ArrayList<>();

        try {
            Connection conn = DbUtil.getConnection();
            String sql = "SELECT TOP 7 * FROM products "
                    + "WHERE category_id = 2 "
                    + "AND screen IN ('24 inch', '25 inch') "
                    + "AND id NOT IN ( "
                    + "   SELECT TOP 12 id FROM products "
                    + "   WHERE category_id = 2 "
                    + "   ORDER BY id ASC "
                    + ") "
                    + "ORDER BY new_price ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                float oldPrice = rs.getFloat("old_price");
                double newPrice = oldPrice * 0.9;

                ProductDTO p = new ProductDTO(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        oldPrice,
                        (float) newPrice,
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getBoolean("status")
                );

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<ProductDTO> getMonitor27Inch() {
        ArrayList<ProductDTO> list = new ArrayList<>();

        try {
            Connection conn = DbUtil.getConnection();

             String sql = "SELECT TOP 11 * FROM products "
                    + "WHERE category_id = 5 "
                    + "AND screen = '27 inch' "
                    + "AND id NOT IN ( "
                    + "    SELECT TOP 12 id FROM products "
                    + "    WHERE category_id = 5 "
                    + "   ORDER BY id ASC "
                    + ") "
                    + "ORDER BY new_price ASC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                float oldPrice = rs.getFloat("old_price");
                double newPrice = oldPrice * 0.9;

                ProductDTO p = new ProductDTO(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        oldPrice,
                        (float) newPrice,
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getBoolean("status")
                );

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public ArrayList<ProductDTO> getMonitorOLED() {
    ArrayList<ProductDTO> list = new ArrayList<>();

    try {
        Connection conn = DbUtil.getConnection();

        String sql = "SELECT TOP 12 * FROM products "
                + "WHERE category_id = 2 "
                + "AND name LIKE N'%OLED%' "
                + "AND id NOT IN ( "
                + "    SELECT TOP 12 id FROM products "
                + "    WHERE category_id = 2 "
                + "    ORDER BY id ASC "
                + ") "
                + "ORDER BY new_price ASC";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            float oldPrice = rs.getFloat("old_price");
            double newPrice = oldPrice * 0.9;

            ProductDTO p = new ProductDTO(
                    rs.getInt("id"),
                    rs.getInt("category_id"),
                    rs.getString("name"),
                    rs.getString("cpu"),
                    rs.getString("gpu"),
                    rs.getString("ram"),
                    rs.getString("ssd"),
                    rs.getString("screen"),
                    rs.getString("refresh_rate"),
                    oldPrice,
                    (float) newPrice,
                    rs.getInt("stock"),
                    rs.getString("description"),
                    rs.getString("image"),
                    rs.getBoolean("status")
            );

            list.add(p);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

}
