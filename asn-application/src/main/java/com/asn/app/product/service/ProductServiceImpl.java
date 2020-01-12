package com.asn.app.product.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.asn.app.product.dto.ProductDTO;
import com.asn.app.product.dto.ProductUpdateDTO;
import com.asn.app.product.entity.Product;
import com.asn.app.product.exception.DuplicateProductNameException;
import com.asn.app.product.exception.ProductNotFoundException;
import com.asn.app.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProduct(Integer id) {
        return productRepository.findById(id).map(this::convert).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public void updateProduct(Integer id, ProductUpdateDTO product) {
        
        Optional<Product> dbProduct = productRepository.findById(id);

        if (dbProduct.isPresent()) {
            
            Optional<Product> productByName = productRepository.findByName(product.getName());
            
            if (productByName.isPresent() && !productByName.get().getId().equals(id)) {
                throw new DuplicateProductNameException();
            }
            
            Product entity = Product.builder().id(id).name(product.getName()).currentPrice(product.getCurrentPrice())
                    .lastUpdate(new Timestamp(System.currentTimeMillis()))
                    .description(product.getDescription())
                    .build();

            productRepository.save(entity);
            
        } else {
            throw new ProductNotFoundException();
        }
    }

    @Override
    public ProductDTO saveProduct(ProductUpdateDTO product) {

        if (productRepository.findByName(product.getName()).isPresent()) {
            throw new DuplicateProductNameException();
        }
        
        Product entity = Product.builder().name(product.getName()).currentPrice(product.getCurrentPrice())
                .lastUpdate(new Timestamp(System.currentTimeMillis()))
                .description(product.getDescription())
                .build();

        return convert(productRepository.save(entity));
    }

    private ProductDTO convert(Product product) {
        return ProductDTO.builder().id(product.getId()).name(product.getName()).currentPrice(product.getCurrentPrice())
                .lastUpdate(product.getLastUpdate())
                .description(product.getDescription())
                .build();
    }

}
