package com.scm.product.management.repository;

import com.scm.product.management.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "ratings", path = "ratings")
public interface RatingRepository extends JpaRepository<Rating, Integer>, JpaSpecificationExecutor<Rating> {

    Page<Rating> findByProductProductName(@Param("productName") String productName, Pageable pageable);

    Page<Rating> findByProductProductDescriptionIgnoreCaseContaining(@Param("productDescription") String productDescription, Pageable pageable);

}