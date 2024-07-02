package com.example.coolmate.Services.Product;

import com.example.coolmate.Dtos.ProductDtos.PriceDTO;
import com.example.coolmate.Dtos.ProductDtos.ProductDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Price;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Repositories.Product.*;
import com.example.coolmate.Services.Impl.Product.IProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailService implements IProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;
    private final PriceRepository priceRepository ;

    @Override
    public List<ProductDetailDTO> getAllProductDetails(int page, int limit) {
        List<ProductDetail> productDetails = productDetailRepository.findAll();
        List<ProductDetailDTO> productDetailDTOs = new ArrayList<>();

        for (ProductDetail productDetail : productDetails) {
            productDetailDTOs.add(convertToProductDetailDTO(productDetail));
        }

        return productDetailDTOs;
    }

    @Override
    public ProductDetail getProductDetailById(int productDetailId) throws Exception {
        return productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy product detail id = "
                        + productDetailId));
    }

    @Override
    public List<ProductDetail> findByProductId(int productId) throws DataNotFoundException {
        List<ProductDetail> productDetails = productDetailRepository.findProductDetailsByProductId(productId);
        if (productDetails.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy product id trong detail: " + productId);
        }
        return productDetails;
    }


    @Override
    public void deleteProductDetail(int id) throws DataNotFoundException {
        if (!productDetailRepository.existsById(id)) {
            throw new DataNotFoundException("Không tìm thấy product detail id: " + id);
        }
        productDetailRepository.deleteById(id);
    }

//

    @Override
    public ProductDetail updateProductDetail(ProductDetail productDetail) throws Exception {
        return null;
    }

    private ProductDetailDTO convertToProductDetailDTO(ProductDetail productDetail) {
        Price price = priceRepository.findByProductDetail(productDetail);

        PriceDTO priceDTO = null;
        if (price != null) {
            priceDTO = PriceDTO.builder()
                    .price(price.getPrice())
                    .priceSelling(price.getPriceSelling())
                    .promotionPrice(price.getPromotionPrice())
                    .productDetailID(productDetail.getId())
                    .build();
        }

        return ProductDetailDTO.builder()
                .productId(productDetail.getProduct().getId())
                .sizes(List.of(productDetail.getSize().getId()))
                .colors(List.of(productDetail.getColor().getId()))
                .price(priceDTO)
                .build();
    }

//    @Override
//    public ProductDetail getProductDetailByProductIdAndSizeId(int productId, int sizeId) {
//        return productDetailRepository.findByProductIdAndSizeId(productId, sizeId);
//    }
//    @Override
//    public ProductDetail getProductDetailByProductIdSizeAndColor(int productId, int sizeId, int colorId) {
//        return productDetailRepository.findByProductIdAndSizeIdAndColorId(productId, sizeId, colorId);
//    }
//
//    @Override
//    public List<ProductDetail> searchProductDetailsByColor(String color) {
//        return productDetailRepository.findByColorName(color);
//    }
//
//    @Override
//    public List<ProductDetail> searchProductDetailBySize(String size) {
//        return productDetailRepository.findBySizeName(size);
//    }

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
