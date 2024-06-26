package com.example.coolmate.Services.Impl.Product;

import com.example.coolmate.Dtos.ProductDtos.ProductDTO;
import com.example.coolmate.Dtos.ProductDtos.ProductImageDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Product;
import com.example.coolmate.Models.Product.ProductImage;
import com.example.coolmate.Responses.Product.ProductResponse;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;

    List<ProductResponse> getAllProducts(int page, int limit);

    Product getProductById(int productId) throws Exception;

    Product updateProduct(int id, ProductDTO productDTO) throws Exception;

    void deleteProduct(int id) throws DataNotFoundException;

    List<Product> searchProductByName(String name);

    ProductImage createProductImage(
            int productId,
            ProductImageDTO productImageDTO) throws Exception;

    List<Product> findByCategoryId(int categoryId);


}
