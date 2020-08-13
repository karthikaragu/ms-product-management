package com.scm.product.management.mapper;

import com.scm.product.management.dto.ProductDTO;
import com.scm.product.management.dto.RatingDTO;
import com.scm.product.management.entity.Product;
import com.scm.product.management.entity.Rating;
import com.scm.product.management.enums.ProductStatusEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface ProductDTOMapper {

    @Mapping(target ="launchDate", expression ="java(fetchLaunchDate(productDTO))" )
    @Mapping(target ="availability", expression ="java(fetchAvailability(productDTO))" )
    @Mapping(target = "ratings", ignore = true)
    Product convertProductDTOToProduct(ProductDTO productDTO);

    @Mapping(target ="stock", source= "productDTO.stock")
    @Mapping(target ="cost", expression= "java(fetchCost(productDTO, product))")
    @Mapping(target ="productDescription", expression= "java(fetchDescription(productDTO, product))")
    @Mapping(target ="availability", expression ="java(fetchAvailability(productDTO))")
    @Mapping(target ="productName", ignore = true)
    @Mapping(target = "vendorId", ignore = true)
    @Mapping(target = "launchDate", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    Product convertProductDTOToModifyProduct(ProductDTO productDTO, @MappingTarget Product product);

    @Mapping(target ="reviewDate", expression ="java(fetchReviewDate(ratingDTO))" )
    Rating convertRatingDTOToRatingEntity(RatingDTO ratingDTO);

    default LocalDateTime fetchLaunchDate(ProductDTO productDTO){
        return Objects.isNull(productDTO.getLaunchDate()) ? LocalDateTime.now() : productDTO.getLaunchDate();
    }

    default LocalDateTime fetchReviewDate(RatingDTO ratingDTO){
        return Objects.isNull(ratingDTO.getReviewDate()) ? LocalDateTime.now() : ratingDTO.getReviewDate();
    }

    default String fetchAvailability(ProductDTO productDTO){
        String availability = ProductStatusEnum.AVAILABLE.code;
        if(NumberUtils.INTEGER_ZERO.equals(productDTO.getStock())){
            availability = ProductStatusEnum.OUTOFSTOCK.code;
        }
        return availability;
    }

    default BigDecimal fetchCost(ProductDTO productDTO, Product product){
        return Objects.isNull(productDTO.getCost()) ? product.getCost() : productDTO.getCost();
    }

    default String fetchDescription(ProductDTO productDTO, Product product){
        return StringUtils.isEmpty(productDTO.getProductDescription()) ? product.getProductDescription()
                : productDTO.getProductDescription();
    }
}
