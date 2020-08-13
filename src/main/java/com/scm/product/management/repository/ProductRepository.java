package com.scm.product.management.repository;

import com.scm.product.management.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    @RestResource(exported = false)
    Product findByProductNameAndVendorId(String productName,Integer vendorId);

    List<Product> findByProductIdIn(@Param("productIds") List<Integer> productIds);

    Page<Product> findByLaunchDateGreaterThan(@Param("date")@DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss") LocalDateTime date, Pageable page);
}