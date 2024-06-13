package com.example.coolmate.Services.Impl.Product;

import com.example.coolmate.Dtos.ProductDtos.ProductDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Responses.Product.ProductDetailResponse;

import java.util.List;

public interface IProductDetailService {
    ProductDetail createProductDetail(ProductDetailDTO productDetailDTO) throws Exception;

    List<ProductDetailResponse> getAllProductDetails(int page, int limit);

    ProductDetail getProductDetailById(int productDetailId) throws Exception;

    void deleteProductDetail(int id) throws DataNotFoundException;


    //    ProductDetail updateProductDetail(int id, ProductDetailDTO productDetailDTO) throws Exception;
    ProductDetail getProductDetailByProductIdAndSizeId(int productId, int sizeId);

    ProductDetail updateProductDetail(ProductDetail productDetail) throws Exception;

    ProductDetail getProductDetailByProductIdSizeAndColor(int productId, int sizeId, int colorId);

    // Search
    List<ProductDetail> searchProductDetailsByColor(String color);

    List<ProductDetail> searchProductDetailBySize(String size);

}
