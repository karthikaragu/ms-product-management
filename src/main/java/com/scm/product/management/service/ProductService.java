package com.scm.product.management.service;

import com.scm.product.management.client.UserClient;
import com.scm.product.management.dto.ErrorDTO;
import com.scm.product.management.dto.ProductDTO;
import com.scm.product.management.dto.ProductResponseDTO;
import com.scm.product.management.dto.RatingDTO;
import com.scm.product.management.entity.Product;
import com.scm.product.management.entity.Rating;
import com.scm.product.management.enums.ErrorType;
import com.scm.product.management.exception.ProductCreationException;
import com.scm.product.management.exception.ProductModifyException;
import com.scm.product.management.mapper.ProductDTOMapper;
import com.scm.product.management.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;
    private ProductDTOMapper mapper;
    private UserClient userClient;

    public ProductResponseDTO createProduct(ProductDTO productDTO){
        Product product = null;
        List<ErrorDTO> errorList = new ArrayList<>();
        try{
            validateProduct(productDTO,errorList);
            if(errorList.isEmpty()){
                product = mapper.convertProductDTOToProduct(productDTO);
                productRepository.save(product);
            }
        }catch(DataIntegrityViolationException e){
            log.error("Error occured while Creating Product",e);
            throw new ProductCreationException(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return ProductResponseDTO.builder().errors(errorList).product(product).build();
    }

    public ProductResponseDTO editProduct(ProductDTO productDTO, Integer productId){
        List<ErrorDTO> errorList = null;
        Product product = null;
        try{
            product  = productRepository.findById(productId).orElse(null);
            if(Objects.isNull(product)){
                errorList = Collections.singletonList(retrieveErrorDTO("Invalid Product Id", "PM-ER02"));
            }else{
                productRepository.save(mapper.convertProductDTOToModifyProduct(productDTO, product));
            }
        }catch(Exception e){
            log.error("Error occured while Editing Product",e);
            throw new ProductModifyException(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return ProductResponseDTO.builder().errors(errorList).product(product).build();
    }

    public ProductResponseDTO rateProduct(RatingDTO ratingDTO){
        List<ErrorDTO> errorList = null;
        Product product = null;
        try{
            product  = productRepository.findById(ratingDTO.getProductId()).orElse(null);
            if(Objects.isNull(product)){
                errorList = Collections.singletonList(retrieveErrorDTO("Invalid Product Id", "PM-ER02"));
            }else{
                populateRating(product, ratingDTO);
                productRepository.save(product);
            }
        }catch(Exception e){
            log.error("Error occured while Saving Rating",e);
            throw new ProductModifyException(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return ProductResponseDTO.builder().errors(errorList).product(product).build();
    }

    public String deleteProduct(Integer productId){
       try{
           productRepository.deleteById(productId);
       }catch(Exception e){
            log.error("Error occured while Deleting Product",e);
            throw new ProductModifyException(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return "Product Deleted Successfully";
    }

    private void validateProduct(ProductDTO productDTO,List<ErrorDTO> errorList){
        if(userClient.checkUserExists(productDTO.getVendorId())){
            Product existingProduct = productRepository.findByProductNameAndVendorId(productDTO.getProductName(),
                    productDTO.getVendorId());
            if(Objects.nonNull(existingProduct)){
                errorList.add(retrieveErrorDTO("Product Already Exists", "PM-ER01"));
            }
        }else{
            errorList.add(retrieveErrorDTO("Invalid Vendor Id for the Product or User service unavailable", "PM-ER03"));
        }
    }

    private void populateRating(Product product,RatingDTO ratingDTO){
        Rating newRating = mapper.convertRatingDTOToRatingEntity(ratingDTO);
        newRating.setProduct(product);
        if(Objects.isNull(product.getRatings())){
            product.setRatings(new HashSet<>(Arrays.asList(newRating)));
        }else{
            Rating existingRating = product.getRatings().stream()
                    .filter(rate -> rate.getProduct().getProductId().equals(ratingDTO.getProductId()))
                    .filter(rate -> rate.getUserId().equals(ratingDTO.getUserId()))
                    .findAny().orElse(null);
            if(existingRating != null){
                existingRating.setRatingValue(ratingDTO.getRatingValue());
                existingRating.setReviewDate(LocalDateTime.now());
                existingRating.setReview(ratingDTO.getReview());
            }else{
                product.getRatings().add(newRating);
            }
        }
    }

    private ErrorDTO retrieveErrorDTO(String message, String errorCode){
        return ErrorDTO.builder()
                .errorSeverity(ErrorType.ERROR)
                .errorMessage(message)
                .errorCode(errorCode).build();
    }
}
