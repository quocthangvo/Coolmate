package com.example.coolmate.Services.Impl.Product;

import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Responses.Product.ProductDetailResponse;

import java.util.List;

public interface IProductDetailService {
//    ProductDetail createProductDetail(ProductDetailDTO productDetailDTO) throws Exception;

    List<ProductDetail> getAllProductDetails(int page, int limit);

    ProductDetailResponse getProductDetailById(int productDetailId) throws Exception;

    List<ProductDetailResponse> findByProductId(int productId) throws DataNotFoundException;


    void deleteProductDetail(int id) throws DataNotFoundException;

    ProductDetail updateProductDetail(ProductDetail productDetail) throws Exception;

    List<ProductDetail> findBySizeId(int sizeId);

    List<ProductDetail> findByColorId(int colorId);

    List<ProductDetail> findBySizeIdAndColorId(int sizeId, int colorId);


    List<ProductDetailResponse> searchVersionName(String versionName);

    ProductDetailResponse getProductDetailLastPrice(int productDetailId) throws Exception;
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
