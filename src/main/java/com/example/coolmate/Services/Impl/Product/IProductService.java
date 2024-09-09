package com.example.coolmate.Services.Impl.Product;

import com.example.coolmate.Dtos.ProductDtos.ProductDTO;
import com.example.coolmate.Dtos.ProductDtos.ProductImageDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Product;
import com.example.coolmate.Models.Product.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;

    Page<Product> getAllProducts(int page, int limit);

    Product getProductById(int productId) throws Exception;

    Product updateProduct(int id, ProductDTO productDTO) throws Exception;

    void deleteProduct(int id) throws DataNotFoundException;

    Page<Product> searchProductByName(String name, Pageable pageable);

    ProductImage createProductImage(
            int productId,
            ProductImageDTO productImageDTO) throws Exception;

    Page<Product> findByCategoryId(int categoryId, int page, int limit);


    List<ProductImage> getImageUrlByProductId(int productId);


}
