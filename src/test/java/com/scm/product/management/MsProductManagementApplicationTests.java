package com.scm.product.management;

import com.scm.product.management.controller.ProductController;
import com.scm.product.management.controller.RatingController;
import com.scm.product.management.repository.ProductRepository;
import com.scm.product.management.repository.RatingRepository;
import com.scm.product.management.service.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MsProductManagementApplicationTests {

	@Autowired
	private ProductController productController;

	@Autowired
	private RatingController ratingController;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private RatingRepository ratingRepository;

	@Test
	void contextLoads() {
		Assert.assertNotNull(productController);
		Assert.assertNotNull(ratingController);
		Assert.assertNotNull(productService);
		Assert.assertNotNull(productRepository);
		Assert.assertNotNull(ratingRepository);

	}

}
