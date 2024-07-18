package com.example.coolmate.Services.Product;

import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.Color;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Models.Product.Size;
import com.example.coolmate.Repositories.Product.ColorRepository;
import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.example.coolmate.Repositories.Product.ProductRepository;
import com.example.coolmate.Repositories.Product.SizeRepository;
import com.example.coolmate.Responses.Product.ProductDetailResponse;
import com.example.coolmate.Services.Impl.Product.IProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductDetailService implements IProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;

//    @Override
//    public ProductDetail createProductDetail(ProductDetailDTO productDetailDTO) throws Exception {
//        // Tìm sản phẩm tồn tại theo ID
//        Product existingProduct = productRepository.findById(productDetailDTO.getProductId())
//                .orElseThrow(() -> new DataNotFoundException(
//                        "Không tìm thấy sản phẩm có id: " + productDetailDTO.getProductId()));
//        Color existingColor = colorRepository.findById(productDetailDTO.getColors())
//                .orElseThrow(() -> new DataNotFoundException(
//                        "Không tìm thấy sản phẩm có id: " + productDetailDTO.getColors()));
//        //tạo
//        ProductDetail newProductDetail = new ProductDetail();
//        newProductDetail.setProduct(existingProduct); // Thiết lập sản phẩm cho ProductDetail
//
//        // Tìm kích cỡ tồn tại theo ID
//        List<Size> sizes = new ArrayList<>();
//        for (Integer sizeId : productDetailDTO.getSizes()) {
//            Size existingSize = sizeRepository.findById(sizeId)
//                    .orElseThrow(() -> new DataNotFoundException(
//                            "Không tìm thấy kích cỡ có id: " + sizeId));
//            sizes.add(existingSize);
//        }
//        newProductDetail.setSize(sizes);
//
//        // Tìm màu sắc tồn tại theo ID
//        List<Color> colors = new ArrayList<>();
//        for (Integer colorId : productDetailDTO.getColors()) {
//            Color existingColor = colorRepository.findById(colorId)
//                    .orElseThrow(() -> new DataNotFoundException(
//                            "Không tìm thấy kích cỡ có id: " + colorId));
//            colors.add(existingColor);
//        }
//        newProductDetail.setColor(colors);
//
//
//        // Lưu ProductDetail mới vào cơ sở dữ liệu
//        productDetailRepository.save(newProductDetail);
//
//        return newProductDetail;
//    }


    @Override
    public List<ProductDetail> getAllProductDetails(int page, int limit) {
        // Lấy danh sách sản phẩm theo trang và giới hạn, sau đó chuyển đổi sang ProductDetailResponse
        return productDetailRepository.findAll();
    }

    @Override
    public ProductDetail getProductDetailById(int productDetailId) throws Exception {
        return productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy product detail id = "
                        + productDetailId));
    }

    @Override
    public List<ProductDetailResponse> findByProductId(int productId) throws DataNotFoundException {
        List<ProductDetail> productDetails = productDetailRepository.findProductDetailsByProductId(productId);
        if (productDetails.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy product id trong detail: " + productId);
        }
        return productDetails.stream()
                .map(ProductDetailResponse::fromProductDetail)
                .collect(Collectors.toList());

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
//        // Kiểm tra xem chi tiết sản phẩm có tồn tại trong cơ sở dữ liệu không
//        ProductDetail existingProductDetail = productDetailRepository.findById(productDetail.getId())
//                .orElseThrow(() -> new DataNotFoundException("Product detail not found with id: " + productDetail.getId()));
//
//        // Cập nhật thông tin kích thước và màu sắc
//        if (productDetail.getSize() != null && !productDetail.getSize().isEmpty()) {
//            existingProductDetail.getSize().clear(); // Xóa danh sách kích thước hiện tại
//            for (Size newSize : productDetail.getSize()) {
//                Size existingSize = sizeRepository.findById(newSize.getId())
//                        .orElseThrow(() -> new DataNotFoundException(
//                                "Không tìm thấy size có id: " + newSize.getId()));
//                existingProductDetail.getSizes().add(existingSize);
//            }
//        }
//
//        if (productDetail.getColors() != null && !productDetail.getColors().isEmpty()) {
//            existingProductDetail.getColors().clear(); // Xóa danh sách màu sắc hiện tại
//            for (Color newColor : productDetail.getColors()) {
//                Color existingColor = colorRepository.findById(newColor.getId())
//                        .orElseThrow(() -> new DataNotFoundException(
//                                "Không tìm thấy màu có id: " + newColor.getId()));
//                existingProductDetail.getColors().add(existingColor);
//            }
//        }
//
//        // Lưu chi tiết sản phẩm đã cập nhật và trả về
//        return productDetailRepository.save(existingProductDetail);
        return null;
    }


    @Override
    public List<ProductDetail> findBySizeId(int sizeId) {
        Size size = sizeRepository.findById(sizeId).orElse(null);
        if (size != null) {
            System.out.println("Size found: " + size);
            return productDetailRepository.findBySizeId(sizeId);
        }
        return List.of();
    }

    @Override
    public List<ProductDetail> findByColorId(int colorId) {
        Color color = colorRepository.findById(colorId).orElse(null);
        if (color != null) {
            System.out.println("Color found: " + color);
            return productDetailRepository.findByColorId(colorId);
        }
        return List.of();
    }


    @Override
    public List<ProductDetail> findBySizeIdAndColorId(int sizeId, int colorId) {
        Size size = sizeRepository.findById(sizeId).orElse(null);
        Color color = colorRepository.findById(colorId).orElse(null);
        if (size != null && color != null) {
            System.out.println("Size found: " + size);
            System.out.println("Color found: " + color);
            return productDetailRepository.findBySizeAndColor(size, color);
        }
        return List.of();
    }

    @Override
    public List<ProductDetail> searchVersionName(String versionName) {
        return productDetailRepository.findByVersionNameContaining(versionName);
    }


    @Override
    public ProductDetailResponse getProductDetailLastPrice(int productDetailId) throws Exception {
        return productDetailRepository.findById(productDetailId)
                .map(ProductDetailResponse::fromProductDetail)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy user Id = "
                        + productDetailId));
    }


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
