package com.asn.app.product.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.asn.app.config.TestConfiguration;
import com.asn.app.product.dto.ProductDTO;
import com.asn.app.product.dto.ProductUpdateDTO;
import com.asn.app.product.entity.Product;
import com.asn.app.product.exception.DuplicateProductNameException;
import com.asn.app.product.exception.ProductNotFoundException;
import com.asn.app.product.repository.ProductRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceImpl productService;

    @Before
    public void before() {

        Product product1 = new Product(1, "product1", new BigDecimal("12.45"),
                new Timestamp(System.currentTimeMillis()), "product1");
        Product product2 = new Product(2, "product2", new BigDecimal("15.75"),
                new Timestamp(System.currentTimeMillis()), "product2");

        Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Mockito.when(productRepository.findById(Mockito.eq(1))).thenReturn(Optional.of(product1));
        Mockito.when(productRepository.findById(Mockito.eq(2))).thenReturn(Optional.of(product1));

        Mockito.when(productRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(productRepository.findByName(Mockito.eq("product1"))).thenReturn(Optional.of(product1));
        Mockito.when(productRepository.findByName(Mockito.eq("product2"))).thenReturn(Optional.of(product2));

    }

    @Test
    public void testGetAllProducts() {
        Assert.assertEquals(2, productService.getAllProducts().size());
    }

    @Test
    public void testGetProduct() {

        ProductDTO product1 = productService.getProduct(1);
        Assert.assertNotNull(product1);
        Assert.assertEquals(1, product1.getId().intValue());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProduct(3));

    }

    @Test
    public void testUpdateProduct() {

        ProductUpdateDTO product2 = new ProductUpdateDTO("product2", BigDecimal.ONE, "product2");
        ProductUpdateDTO product3 = new ProductUpdateDTO("product3", BigDecimal.ONE, "product3");

        Assertions.assertThrows(DuplicateProductNameException.class, () -> productService.updateProduct(1, product2));
        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(3, product3));

        Mockito.when(productRepository.save(Mockito.any())).thenReturn(new Product());
        Assert.assertNotNull(productService.updateProduct(2, product2));
    }

    @Test
    public void testSaveProduct() {

        ProductUpdateDTO product2 = new ProductUpdateDTO("product2", BigDecimal.ONE, "product2");
        ProductUpdateDTO product3 = new ProductUpdateDTO("product3", BigDecimal.ONE, "product3");

        Assertions.assertThrows(DuplicateProductNameException.class, () -> productService.saveProduct(product2));

        Mockito.when(productRepository.save(Mockito.any())).thenReturn(new Product());
        Assert.assertNotNull(productService.saveProduct(product3));
    }

}
