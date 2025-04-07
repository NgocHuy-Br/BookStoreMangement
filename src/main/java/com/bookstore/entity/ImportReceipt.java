package com.bookstore.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class ImportReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    @OneToMany(mappedBy = "importReceipt")
    private List<ImportDetail> details;

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<ImportDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ImportDetail> details) {
        this.details = details;
    }
}
