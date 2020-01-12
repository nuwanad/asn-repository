package com.asn.app.product.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.asn.app.config.TestConfiguration;
import com.asn.app.exception.ControllerExceptionHandler;
import com.asn.app.product.dto.ProductDTO;
import com.asn.app.product.dto.ProductUpdateDTO;
import com.asn.app.product.exception.DuplicateProductNameException;
import com.asn.app.product.exception.ProductNotFoundException;
import com.asn.app.product.service.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)

public class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private ProductController productController;

    private MockMvc mockMvc;

    private static final String JSON_PATH_ID = "$.id";
    private static final String JSON_PATH_NAME = "$.name";
    private static final String JSON_PATH_CURRENT_PRICE = "$.currentPrice";
    private static final String JSON_PATH_DESCRIPTION = "$.description";

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new ControllerExceptionHandler()).build();

        ProductDTO product1 = new ProductDTO(1, "product1", new BigDecimal("12.45"),
                new Timestamp(System.currentTimeMillis()), "product1");
        ProductDTO product2 = new ProductDTO(2, "product2", new BigDecimal("15.75"),
                new Timestamp(System.currentTimeMillis()), "product2");
        ProductDTO product3 = new ProductDTO(3, "product3", new BigDecimal("16.65"),
                new Timestamp(System.currentTimeMillis()), "product3");

        Mockito.when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

        Mockito.when(productService.getProduct(Mockito.eq(1))).thenReturn(product1);
        Mockito.when(productService.getProduct(Mockito.eq(2))).thenReturn(product2);
        Mockito.when(productService.getProduct(Mockito.eq(3))).thenThrow(ProductNotFoundException.class);

        Mockito.when(productService.updateProduct(Mockito.eq(1), Mockito.any(ProductUpdateDTO.class)))
                .thenReturn(product1);
        Mockito.when(productService.updateProduct(Mockito.eq(2), Mockito.any(ProductUpdateDTO.class)))
                .thenThrow(DuplicateProductNameException.class);

        ProductUpdateDTO productUpdate1 = new ProductUpdateDTO("product1", new BigDecimal("12.45"), "product1");
        ProductUpdateDTO productUpdate3 = new ProductUpdateDTO("product3", new BigDecimal("16.65"), "product3");

        Mockito.when(productService.saveProduct(Mockito.eq(productUpdate3))).thenReturn(product3);

        Mockito.when(productService.saveProduct(Mockito.eq(productUpdate1)))
                .thenThrow(DuplicateProductNameException.class);

    }

    @Test
    public void getAllProducts() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products")).andExpect(status().isOk());
    }

    @Test
    public void getProduct() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_ID).value("1"))
                .andExpect(jsonPath(JSON_PATH_NAME).value("product1"))
                .andExpect(jsonPath(JSON_PATH_CURRENT_PRICE).value("12.45"))
                .andExpect(jsonPath(JSON_PATH_DESCRIPTION).value("product1"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/3")).andExpect(status().isNotFound());

    }

    @Test
    public void updateProduct() throws Exception {

        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        jsonBuilder.add("name", "product1");
        jsonBuilder.add("currentPrice", "12.45");
        jsonBuilder.add("description", "product1");
        String json = jsonBuilder.build().toString();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json)).andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_ID).value("1"))
                .andExpect(jsonPath(JSON_PATH_NAME).value("product1"))
                .andExpect(jsonPath(JSON_PATH_CURRENT_PRICE).value("12.45"))
                .andExpect(jsonPath(JSON_PATH_DESCRIPTION).value("product1"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/2")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void createProduct() throws Exception {

        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        jsonBuilder.add("name", "product3");
        jsonBuilder.add("currentPrice", "16.65");
        jsonBuilder.add("description", "product3");
        String json = jsonBuilder.build().toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_ID).value("3"))
                .andExpect(jsonPath(JSON_PATH_NAME).value("product3"))
                .andExpect(jsonPath(JSON_PATH_CURRENT_PRICE).value("16.65"))
                .andExpect(jsonPath(JSON_PATH_DESCRIPTION).value("product3"));

        jsonBuilder = Json.createObjectBuilder();
        jsonBuilder.add("name", "product1");
        jsonBuilder.add("currentPrice", "12.45");
        jsonBuilder.add("description", "product1");
        json = jsonBuilder.build().toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isBadRequest());
    }
}
