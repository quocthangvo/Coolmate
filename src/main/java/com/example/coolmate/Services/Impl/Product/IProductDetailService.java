package com.example.coolmate.Services.Impl.Product;

import com.example.coolmate.Dtos.ProductDtos.ProductDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.ProductDetail;

import java.util.List;

public interface IProductDetailService {
//    ProductDetail createProductDetail(ProductDetailDTO productDetailDTO) throws Exception;

    List<ProductDetailDTO> getAllProductDetails(int page, int limit);

    ProductDetail getProductDetailById(int productDetailId) throws Exception;

    List<ProductDetail> findByProductId(int productId) throws DataNotFoundException;


    void deleteProductDetail(int id) throws DataNotFoundException;

    ProductDetail updateProductDetail(ProductDetail productDetail) throws Exception;

//    ProductDetail updateProductDetail(int id, ProductDetailDTO productDetailDTO) throws Exception;
//    ProductDetail getProductDetailByProductIdAndSizeId(int productId, int sizeId);
//

//
//    ProductDetail getProductDetailByProductIdSizeAndColor(int productId, int sizeId, int colorId);
//
//    // Search
//    List<ProductDetail> searchProductDetailsByColor(String color);
//
//    List<ProductDetail> searchProductDetailBySize(String size);

}
