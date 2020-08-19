package com.scm.product.management.service;


import com.scm.product.management.dto.ErrorDTO;
import com.scm.product.management.dto.ProductDTO;
import com.scm.product.management.dto.ProductResponseDTO;
import com.scm.product.management.dto.RatingDTO;
import com.scm.product.management.entity.Rating;
import com.scm.product.management.exception.ProductCreationException;
import com.scm.product.management.exception.ProductModifyException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    private MockWebServer userWebServer;

    @Before
    public void init() throws IOException {
        userWebServer = new MockWebServer();
        userWebServer.start(8011);
    }

    @After
    public void tearDown() throws IOException{
        userWebServer.shutdown();
    }

    @Test
    public void createProduct_happyFlow(){
        ProductDTO requestDto = fetchProductDTO();
        userWebServer.enqueue(new MockResponse().setResponseCode(200).addHeader("Content-Type","application/json").setBody("true"));
        ProductResponseDTO responseDTO = productService.createProduct(requestDto);
        Assert.assertNotNull(responseDTO);
        Assert.assertEquals(0,responseDTO.getErrors().size());
        Assert.assertEquals("ABC",responseDTO.getProduct().getProductName());
    }

    @Test
    public void createProduct_userServiceDown(){
        ProductDTO requestDto = fetchProductDTO();
        ProductResponseDTO responseDTO = productService.createProduct(requestDto);
        Assert.assertNotNull(responseDTO);
        Assert.assertEquals(1,responseDTO.getErrors().size());
        Assert.assertEquals("PM-ER03",responseDTO.getErrors().stream().findFirst().map(ErrorDTO::getErrorCode).orElse(null));
    }

    @Test
    public void createProduct_duplicateProduct(){
        ProductDTO requestDto = fetchProductDTO();
        requestDto.setProductName("Brakeshoe");
        requestDto.setVendorId(8);
        userWebServer.enqueue(new MockResponse().setResponseCode(200).addHeader("Content-Type","application/json").setBody("true"));
        ProductResponseDTO responseDTO = productService.createProduct(requestDto);
        Assert.assertNotNull(responseDTO);
        Assert.assertEquals(1,responseDTO.getErrors().size());
        Assert.assertEquals("PM-ER01",responseDTO.getErrors().stream().findFirst().map(ErrorDTO::getErrorCode).orElse(null));
    }

    @Test
    public void createProduct_exceptionFlow(){
        ProductDTO requestDto = fetchProductDTO();
        requestDto.setProductName(null);
        userWebServer.enqueue(new MockResponse().setResponseCode(200).addHeader("Content-Type","application/json").setBody("true"));
        ProductCreationException ex = Assert.assertThrows(ProductCreationException.class,
                () -> productService.createProduct(requestDto));
        Assert.assertTrue(ex.getMessage().contains("ConstraintViolationException"));
    }

    @Test
    @Sql({"/data.sql"})
    public void editProduct_happyFlow(){
        ProductDTO requestDto = fetchProductDTO();
        ProductResponseDTO responseDTO = productService.editProduct(requestDto,1);
        Assert.assertNotNull(responseDTO);
        Assert.assertNull(responseDTO.getErrors());
        Assert.assertEquals("Brakeshoe",responseDTO.getProduct().getProductName());
    }

    @Test
    public void editProduct_invalidProduct(){
        ProductDTO requestDto = fetchProductDTO();
        ProductResponseDTO responseDTO = productService.editProduct(requestDto,10);
        Assert.assertNotNull(responseDTO);
        Assert.assertEquals(1,responseDTO.getErrors().size());
        Assert.assertEquals("PM-ER02",responseDTO.getErrors().stream().findFirst().map(ErrorDTO::getErrorCode).orElse(null));
    }

    @Test
    public void editProduct_exceptionFlow(){
        ProductDTO requestDto = fetchProductDTO();
        requestDto.setStock(null);
        requestDto.setProductName(null);
        Assert.assertThrows(TransactionSystemException.class,
                () -> productService.editProduct(requestDto,1));
    }

    @Test
    public void rateProduct_happyFlow(){
        RatingDTO requestDto = fetchRatingDTO();
        ProductResponseDTO responseDTO = productService.rateProduct(requestDto);
        Assert.assertNotNull(responseDTO);
        Assert.assertNull(responseDTO.getErrors());
        Assert.assertEquals(1,responseDTO.getProduct().getRatings().size());
        Assert.assertEquals(BigDecimal.valueOf(3.5),responseDTO.getProduct().getRatings()
                .stream().findFirst().map(Rating::getRatingValue).orElse(null));
    }

    @Test
    public void rateProduct_invalidProduct(){
        RatingDTO requestDto = fetchRatingDTO();
        requestDto.setProductId(10);
        ProductResponseDTO responseDTO = productService.rateProduct(requestDto);
        Assert.assertNotNull(responseDTO);
        Assert.assertEquals(1,responseDTO.getErrors().size());
        Assert.assertEquals("PM-ER02",responseDTO.getErrors()
                .stream().findFirst().map(ErrorDTO::getErrorCode).orElse(null));
    }

    @Test
    public void rateProduct_exceptionFlow(){
        RatingDTO requestDto = fetchRatingDTO();
        requestDto.setRatingValue(null);
        ProductModifyException ex = Assert.assertThrows(ProductModifyException.class,
                () -> productService.rateProduct(requestDto));
        Assert.assertTrue(ex.getMessage().contains("ConstraintViolationException"));
    }

    @Test
    public void deleteProduct_happyFlow(){
        Assert.assertEquals("Product Deleted Successfully",productService.deleteProduct(2));
    }

    private ProductDTO fetchProductDTO(){
        return ProductDTO.builder()
                .productName("ABC").productDescription("XYZ")
                .cost(BigDecimal.valueOf(100)).stock(10).availability("Y").vendorId(1)
                .build();
    }

    private RatingDTO fetchRatingDTO(){
        return RatingDTO.builder().productId(1).ratingValue(BigDecimal.valueOf(3.5)).userId(1)
                .review("Good").reviewDate(LocalDateTime.now()).build();
    }

}
