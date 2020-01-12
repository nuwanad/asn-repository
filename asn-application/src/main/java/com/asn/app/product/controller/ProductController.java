package com.asn.app.product.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asn.app.product.dto.ProductDTO;
import com.asn.app.product.dto.ProductUpdateDTO;
import com.asn.app.product.service.ProductService;

import lombok.AllArgsConstructor;

@Validated
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @GetMapping("/products")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ProductDTO getProduct(@Min(1) @PathVariable Integer id) {
        return productService.getProduct(id);
    }

    @PutMapping("/products/{id}")
    public ProductDTO updateProduct(@Min(1) @PathVariable Integer id, @Valid @RequestBody ProductUpdateDTO product) {
        return productService.updateProduct(id, product);
    }

    @PostMapping("/products")
    public ProductDTO createProduct(@Valid @RequestBody ProductUpdateDTO product) {
        return productService.saveProduct(product);
    }
}
