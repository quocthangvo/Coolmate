package com.example.coolmate.Services.ProductServices;

import com.example.coolmate.Dtos.ProductDtos.ProductDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Product;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.example.coolmate.Repositories.Product.ProductRepository;
import com.example.coolmate.Services.Impl.Product.IProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailService implements IProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public ProductDetail createProductDetail(ProductDetailDTO productDetailDTO) throws Exception {
        Product existingProduct = productRepository.findById(productDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id: " + productDetailDTO.getProductId()));
        ProductDetail newProductDetail = ProductDetail.builder()
                .product(existingProduct)
                .build();

        // Lưu
        return productDetailRepository.save(newProductDetail);
    }

    @Override
    public List<ProductDetail> getAllProductDetails() {
        return productDetailRepository.findAll();
    }

    @Override
    public ProductDetail getProductDetailById(int productDetailId) throws Exception {
        return productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy product id = "
                        + productDetailId));
    }

    @Override
    public void deleteProductDetail(int id) throws DataNotFoundException {
        if (!productDetailRepository.existsById(id)) {
            throw new DataNotFoundException("Không tìm thấy product detail id: " + id);
        }
        productDetailRepository.deleteById(id);
    }

//    @Override
//    public ProductDetail updateProductDetail(int id, ProductDetailDTO productDetailDTO) throws Exception {
//        Optional<ProductDetail> existingProductDetail = productDetailRepository.findById(id);
//
//        if (existingProductDetail.isPresent()) {
//            throw new DataNotFoundException("Danh mục đã tồn tại: " + productDetailDTO.getProductId());
//        } else {
//            // Create a new Category entity
//            ProductDetail newProductDetail = ProductDetail.builder()
//                    .id(productDetailDTO.getProductId())
//                    .build();
//            return productDetailRepository.save(newProductDetail);
//        }
//    }
}
