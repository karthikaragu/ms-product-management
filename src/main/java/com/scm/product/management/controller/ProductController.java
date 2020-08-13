package com.scm.product.management.controller;

import com.scm.product.management.dto.ErrorDTO;
import com.scm.product.management.dto.ProductDTO;
import com.scm.product.management.dto.ProductResponseDTO;
import com.scm.product.management.enums.ErrorType;
import com.scm.product.management.exception.ProductCreationException;
import com.scm.product.management.exception.ProductModifyException;
import com.scm.product.management.service.ProductService;
import lombok.AllArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/dealer")
@AllArgsConstructor
public class ProductController {
    
    private ProductService productService;
    
    @PostMapping(value="/createproduct")
    public ResponseEntity<ProductResponseDTO> createProduct (@RequestBody @Valid ProductDTO productDTO,
                                                             BindingResult result) {
        if(result.hasFieldErrors()){
            return ResponseEntity.badRequest().body(fetchProductResponseDTO(result));
        }
        return Optional.ofNullable(productService.createProduct(productDTO))
                .map(product -> ResponseEntity.ok().body(product))
                .orElseThrow(() -> new ProductCreationException(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Product Creation Failed"));
    }

    @PutMapping(value = "/editproduct/{productid}")
    public ResponseEntity<ProductResponseDTO> editProduct (@PathVariable("productid") Integer productId,
                           @RequestParam(value = "stock") Integer stock,
                           @RequestParam(value = "cost", required = false) BigDecimal cost,
                           @RequestParam(value = "productDescription", required = false) String productDescription) {
        return Optional.ofNullable(productService.editProduct(ProductDTO.builder()
                                                                .productDescription(productDescription)
                                                                .stock(stock)
                                                                .cost(cost).build(),productId))
                .map(product -> ResponseEntity.ok().body(product))
                .orElseThrow(() -> new ProductModifyException(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Product Creation Failed"));
    }

    @DeleteMapping(value="/deleteproduct/{productid}")
    public String deleteProduct(@PathVariable("productid")Integer productId){
        return productService.deleteProduct(productId);
    }

    private ProductResponseDTO fetchProductResponseDTO(BindingResult result){
        List<ErrorDTO> errorList = new ArrayList<>();
        result.getFieldErrors().forEach(error ->
                errorList.add(ErrorDTO.builder()
                        .errorSeverity(ErrorType.FIELD_VALIDATION)
                        .errorMessage(error.getDefaultMessage())
                        .errorCode("PM-FV01").build()));
        return ProductResponseDTO.builder()
                .errors(errorList)
                .build();
    }

}
