/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.DbUtil;

/**
 *
 * @author truon
 */
public class LaptopDAO {

    public boolean addLaptop(LaptopDTO l) {

        String sql = "INSERT INTO laptops(name,cpu,gpu,ram,ssd,screen,refresh_rate,old_price,new_price,image_url) VALUES(?,?,?,?,?,?,?,?,?,?)";

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
            ps.setString(10, l.getImage_url());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<LaptopDTO> getAllLaptop() {

        ArrayList<LaptopDTO> list = new ArrayList<>();

        try {
            Connection con = DbUtil.getConnection();
            String sql = "SELECT * FROM laptops";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                LaptopDTO l = new LaptopDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        rs.getFloat("old_price"),
                        rs.getFloat("new_price"),
                        rs.getString("image_url")
                );

                list.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<LaptopDTO> getLaptopUnder25() {

        ArrayList<LaptopDTO> list = new ArrayList<>();

        try {
            Connection con = DbUtil.getConnection();
            String sql = "SELECT * FROM laptops WHERE new_price <= 25000000";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                LaptopDTO l = new LaptopDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        rs.getFloat("old_price"),
                        rs.getFloat("new_price"),
                        rs.getString("image_url")
                );

                list.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<LaptopDTO> getLaptopUnder30() {

        ArrayList<LaptopDTO> list = new ArrayList<>();

        try {
            Connection con = DbUtil.getConnection();
            String sql = "SELECT * FROM laptops WHERE new_price > 25000000 AND new_price <= 30000000";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                LaptopDTO l = new LaptopDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        rs.getFloat("old_price"),
                        rs.getFloat("new_price"),
                        rs.getString("image_url")
                );

                list.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<LaptopDTO> getLaptopTop30() {

        ArrayList<LaptopDTO> list = new ArrayList<>();

        try {
            Connection con = DbUtil.getConnection();
            String sql = "SELECT * FROM laptops WHERE new_price > 30000000";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                LaptopDTO l = new LaptopDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getString("ram"),
                        rs.getString("ssd"),
                        rs.getString("screen"),
                        rs.getString("refresh_rate"),
                        rs.getFloat("old_price"),
                        rs.getFloat("new_price"),
                        rs.getString("image_url")
                );

                list.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    
}
