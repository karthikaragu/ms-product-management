package com.scm.product.management.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Table(name = "product",schema="autosparescm")
@Entity
public class Product implements Serializable {
    private static final long serialVersionUID = 5948739L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid", insertable = false, nullable = false)
    private Integer productId;

    @Column(name = "productname", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @NotNull
    @Column(name = "userid", nullable = false)
    private Integer vendorId;

    @Column(name = "availability", nullable = false)
    private String availability;

    @NotNull
    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @Column(name = "launchdate", nullable = false)
    private LocalDateTime launchDate;

    @Column(name = "productdescription", nullable = false)
    private String productDescription;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Rating> ratings;

}