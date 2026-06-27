/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.DbUtil;

/**
 *
 * @author truon
 */
public class AdminDAO {

    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */

    public AdminDTO searchByAdminName(String Username) {
        AdminDTO admin = null;
        
            String sql = "SELECT * FROM admins WHERE username=?";
            try (Connection conn = DbUtil.getConnection()) {             
                System.out.println(sql);
                
                PreparedStatement letter = conn.prepareStatement(sql);
                letter.setString(1, Username);
                ResultSet rs = letter.executeQuery();
                
                if (rs.next()) {
                    
                    String user = rs.getString("username");
                    String pass = rs.getString("password");
                    
                    admin = new AdminDTO(user, pass);
                }
                rs.close();
                letter.close();
            
        } catch (Exception e) {
}
        return admin;
    }

    public AdminDTO adLogin(String Username, String Password) {
        AdminDTO admin = searchByAdminName(Username);
        if (admin != null && Password.equals(admin.getPassword())) {
                    
            return admin;
        }
        return null;
    }

    public boolean addAd(AdminDTO ad) {
        int result = 0;
        try {

            Connection conn = DbUtil.getConnection();
            String sql = "INSERT INTO admins (username, password) VALUES (?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ad.getUsername());
            ps.setString(2, ad.getPassword());
            result = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean UpdateAd(AdminDTO ad) {
        int result = 0;
        try {

            Connection conn = DbUtil.getConnection();
            String sql = "UPDATE admins"
                    + "   SET password = ?"
                    + " WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ad.getPassword());
            ps.setString(2, ad.getUsername());
            result = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
