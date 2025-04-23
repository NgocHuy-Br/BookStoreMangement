package com.bookstore.entity;

import jakarta.persistence.*;

@Entity
public class CustomerSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double discountRate;

    private int requiredPointsForMembership;

    // mỗi nhà sách có 1 CustomerSetting riêng
    @OneToOne
    @JoinColumn(name = "bookstore_id", referencedColumnName = "id", unique = true)
    private Bookstore bookstore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
