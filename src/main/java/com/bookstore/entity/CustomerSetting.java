package com.bookstore.entity;

import jakarta.persistence.*;

@Entity
public class CustomerSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // % giảm giá cho khách hàng thành viên
    private double discountRate;

    // Số điểm tích lũy cần để trở thành thành viên
    private int requiredPointsForMembership;

    // Liên kết với Bookstore (mỗi nhà sách có 1 CustomerSetting riêng)
    @OneToOne
    @JoinColumn(name = "bookstore_id", referencedColumnName = "id", unique = true)
    private Bookstore bookstore;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public int getRequiredPointsForMembership() {
        return requiredPointsForMembership;
    }

    public void setRequiredPointsForMembership(int requiredPointsForMembership) {
        this.requiredPointsForMembership = requiredPointsForMembership;
    }

    public Bookstore getBookstore() {
        return bookstore;
    }

    public void setBookstore(Bookstore bookstore) {
        this.bookstore = bookstore;
    }
}
