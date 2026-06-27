package model;

import java.sql.Timestamp;

public class OrderDTO {

    private int id;
    private String email;        // 🔥 đổi từ userID -> email
    private double totalPrice;
    private String status;
    private Timestamp createdAt;

    public OrderDTO() {
    }

    public OrderDTO(int id, String email, double totalPrice, String status, Timestamp createdAt) {
        this.id = id;
        this.email = email;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
