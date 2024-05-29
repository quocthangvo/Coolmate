package com.example.coolmate.Services.Impl.Product;

import com.example.coolmate.Dtos.ProductDtos.ProductDTO;
import com.example.coolmate.Dtos.ProductDtos.ProductImageDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Product;
import com.example.coolmate.Models.Product.ProductImage;
import com.example.coolmate.Responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;

    Page<ProductResponse> getAllProducts(PageRequest pageRequest);

    Product getProductById(int productId) throws Exception;

    Product updateProduct(int id, ProductDTO productDTO) throws Exception;

    void deleteProduct(int id) throws DataNotFoundException;


    ProductImage createProductImage(
            int productId,
            ProductImageDTO productImageDTO) throws Exception;
}
