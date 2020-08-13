package com.scm.product.management.dto;

import com.scm.product.management.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ProductResponseDTO implements Serializable {

    private static final long serialVersionUID = 65618819L;

    private Product product;
    private List<ErrorDTO> errors;
}
