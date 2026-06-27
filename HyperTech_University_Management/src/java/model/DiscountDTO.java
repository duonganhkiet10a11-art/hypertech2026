/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author hasot
 */
public class DiscountDTO {
    private int id;
    private String name;
    private int discount_percent;
    private Date start_date;
    private Date end_date;
    

    public DiscountDTO() {
    }  

    public DiscountDTO(int id, String name, int discount_percent, Date start_date, Date end_date) {
        this.id = id;
        this.name = name;
        this.discount_percent = discount_percent;
        this.start_date = start_date;
        this.end_date = end_date;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(int discount_percent) {
        this.discount_percent = discount_percent;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }
 
}
