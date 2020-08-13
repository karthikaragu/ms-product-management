package com.scm.product.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "rating",schema="autosparescm")
@Entity
@Data
public class Rating implements Serializable {
    private static final long serialVersionUID = 57687641L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, name = "ratingid", nullable = false)
    private Integer ratingId;

    @Column(name = "rating", nullable = false)
    private BigDecimal ratingValue;

    @Column(name = "review")
    private String review;

    @Column(name = "reviewdate", nullable = false)
    private LocalDateTime reviewDate;

    @Column(name = "userid", nullable = false)
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="productid",nullable = false)
    @NotNull(message = "Mandatory Field - Product")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Product product;

    
}