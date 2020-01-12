package com.asn.app.product.service;

import java.util.List;

import com.asn.app.product.dto.ProductDTO;
import com.asn.app.product.dto.ProductUpdateDTO;

public interface ProductService {

    List<ProductDTO> getAllProducts();

    ProductDTO getProduct(Integer id);
    
    void updateProduct(Integer id, ProductUpdateDTO product);

    ProductDTO saveProduct(ProductUpdateDTO product);

}
