package com.demo.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount_available", nullable = false)
    private Integer amountAvailable;

    @NotBlank
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "cost", nullable = false)
    private Integer cost;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user = new User();

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public Integer getAmountAvailable() {
        return amountAvailable;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getCost() {
        return cost;
    }

    public User getUser() {
        return user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmountAvailable(Integer amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

