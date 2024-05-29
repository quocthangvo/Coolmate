package com.example.coolmate.Services.Impl.Product;

import com.example.coolmate.Dtos.ProductDtos.ProductDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.ProductDetail;

import java.util.List;

public interface IProductDetailService {
    ProductDetail createProductDetail(ProductDetailDTO productDetailDTO) throws Exception;

    List<ProductDetail> getAllProductDetails();

    ProductDetail getProductDetailById(int productDetailId) throws Exception;

    void deleteProductDetail(int id) throws DataNotFoundException;

//    ProductDetail updateProductDetail(int id, ProductDetailDTO productDetailDTO) throws Exception;


}
