package com.example.coolmate.Services.Impl.Product;

import com.example.coolmate.Dtos.ProductDtos.ProductDTO;
import com.example.coolmate.Dtos.ProductDtos.ProductDetailDTO;
import com.example.coolmate.Dtos.ProductDtos.ProductImageDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Product;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Models.Product.ProductImage;
import com.example.coolmate.Responses.Product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;

    Page<ProductResponse> getAllProducts(PageRequest pageRequest);

    Product getProductById(int productId) throws Exception;

    Product updateProduct(int id, ProductDTO productDTO) throws Exception;

    void deleteProduct(int id) throws DataNotFoundException;

    List<Product> searchProductByName(String name);

    ProductImage createProductImage(
            int productId,
            ProductImageDTO productImageDTO) throws Exception;

    ProductDetail createProductDetail(ProductDetailDTO productDetailDTO) throws Exception;


}
