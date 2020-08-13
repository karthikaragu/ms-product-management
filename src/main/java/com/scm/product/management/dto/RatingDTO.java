package com.scm.product.management.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class RatingDTO implements Serializable {

    private static final long serialVersionUID = 64672579L;

    @NotNull(message="Rating Points is mandatory")
    private BigDecimal ratingValue;

    private String review;

    @FutureOrPresent(message ="Review date must be current date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reviewDate;

    @NotNull(message = "UserId is mandatory to give rating")
    private Integer userId;

    @NotNull(message="Product Id is Mandatory")
    private Integer productId;
}
