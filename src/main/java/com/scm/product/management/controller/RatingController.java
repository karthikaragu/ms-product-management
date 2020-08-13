package com.scm.product.management.controller;

import com.scm.product.management.dto.ErrorDTO;
import com.scm.product.management.dto.ProductResponseDTO;
import com.scm.product.management.dto.RatingDTO;
import com.scm.product.management.enums.ErrorType;
import com.scm.product.management.exception.ProductModifyException;
import com.scm.product.management.service.ProductService;
import lombok.AllArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class RatingController {

    private ProductService productService;

    @PostMapping(value="/rateproduct")
    public ResponseEntity<ProductResponseDTO> rateProduct (@RequestBody @Valid RatingDTO ratingDTO,
                                                           BindingResult result){
        if(result.hasFieldErrors()){
            return ResponseEntity.badRequest().body(fetchProductResponseDTO(result));
        }
        return Optional.ofNullable(productService.rateProduct(ratingDTO))
                .map(product -> ResponseEntity.ok().body(product))
                .orElseThrow(() -> new ProductModifyException(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Product Rating Failed"));
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
