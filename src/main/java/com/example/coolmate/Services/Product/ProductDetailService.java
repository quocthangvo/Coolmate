package com.example.coolmate.Services.Product;

import com.example.coolmate.Dtos.ProductDtos.ProductDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Color;
import com.example.coolmate.Models.Product.Product;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Models.Product.Size;
import com.example.coolmate.Repositories.Product.ColorRepository;
import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.example.coolmate.Repositories.Product.ProductRepository;
import com.example.coolmate.Repositories.Product.SizeRepository;
import com.example.coolmate.Responses.Product.ProductDetailResponse;
import com.example.coolmate.Services.Impl.Product.IProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailService implements IProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;

    @Override
    public ProductDetail createProductDetail(ProductDetailDTO productDetailDTO) throws Exception {
        Product existingProduct = productRepository.findById(productDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id: " + productDetailDTO.getProductId()));

        Size existingSize = sizeRepository.findById(productDetailDTO.getSizeId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy size có id: " + productDetailDTO.getSizeId()));

        Color existingColor = colorRepository.findById(productDetailDTO.getColorId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy màu có id: " + productDetailDTO.getColorId()));

        ProductDetail newProductDetail = ProductDetail.builder()
                .product(existingProduct)
                .size(existingSize)
                .color(existingColor)
                .build();


        productDetailRepository.save(newProductDetail);

        return newProductDetail;
    }


    @Override
    public List<ProductDetailResponse> getAllProductDetails(int page, int limit) {
        // Tạo một đối tượng PageRequest với trang và giới hạn
        PageRequest pageRequest = PageRequest.of(page, limit);

        // Lấy danh sách sản phẩm theo trang và giới hạn, sau đó chuyển đổi sang ProductDetailResponse
        return productDetailRepository.findAll(pageRequest)
                .map(ProductDetailResponse::fromProductDetail)
                .getContent();
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

    @Override
    public ProductDetail getProductDetailByProductIdAndSizeId(int productId, int sizeId) {
        return productDetailRepository.findByProductIdAndSizeId(productId, sizeId);
    }

    @Override
    public ProductDetail updateProductDetail(ProductDetail productDetail) throws Exception {
        // Kiểm tra xem chi tiết sản phẩm có tồn tại trong cơ sở dữ liệu không
        ProductDetail existingProductDetail = productDetailRepository.findById(productDetail.getId())
                .orElseThrow(() -> new DataNotFoundException("Product detail not found with id: " + productDetail.getId()));

        // Cập nhật thông tin
        existingProductDetail.setColor(productDetail.getColor());
        // Cập nhật thông tin kích thước nếu cần
        existingProductDetail.setSize(productDetail.getSize());

        // Lưu chi tiết sản phẩm đã cập nhật và trả về
        return productDetailRepository.save(existingProductDetail);
//        return productDetailRepository.save(productDetail);
    }

    @Override
    public ProductDetail getProductDetailByProductIdSizeAndColor(int productId, int sizeId, int colorId) {
        return productDetailRepository.findByProductIdAndSizeIdAndColorId(productId, sizeId, colorId);
    }

    @Override
    public List<ProductDetail> searchProductDetailsByColor(String color) {
        return productDetailRepository.findByColorName(color);
    }

    @Override
    public List<ProductDetail> searchProductDetailBySize(String size) {
        return productDetailRepository.findBySizeName(size);
    }

//    @Override
//    public ProductDetail updateProductDetail(int id, ProductDetailDTO productDetailDTO) throws Exception {
//        ProductDetail existingProductDetail = getProductDetailById(id);
//
//        Product existingProduct = productRepository.findById(productDetailDTO.getProductId())
//                .orElseThrow(() -> new DataNotFoundException(
//                        "Cannot find product with id: " + productDetailDTO.getProductId()));
//
//        Size existingSize = sizeRepository.findById(productDetailDTO.getSizeId())
//                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy size có id: " + productDetailDTO.getSizeId()));
//
//        Color existingColor = colorRepository.findById(productDetailDTO.getColorId())
//                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy màu có id: " + productDetailDTO.getColorId()));
//
//        existingProductDetail.setProduct(existingProduct);
//        existingProductDetail.setSize(existingSize);
//        existingProductDetail.setColor(existingColor);
//
//        // Lưu thông tin cập nhật của ProductDetail vào cơ sở dữ liệu
//        return productDetailRepository.save(existingProductDetail);
//    }
}
