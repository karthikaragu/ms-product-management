package com.scm.product.management.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 6646590L;

    @NotEmpty(message = "Enter Product Name")
    private String productName;

    @NotNull(message = "Enter number of stock available")
    private Integer stock;

    @NotNull(message = "Vendor Id is mandatory")
    private Integer vendorId;

    @NotNull(message = "Enter availability")
    private String availability;

    @FutureOrPresent(message ="Date of Birth must be in the Past")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime launchDate;

    @NotNull(message = "Enter Product Cost")
    private BigDecimal cost;

    private String productDescription;

}

