package com.example.coolmate.Services.Product;

import com.example.coolmate.Dtos.ProductDtos.ProductDTO;
import com.example.coolmate.Dtos.ProductDtos.ProductDetailDTO;
import com.example.coolmate.Dtos.ProductDtos.ProductImageDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.InvalidParamException;
import com.example.coolmate.Models.Category;
import com.example.coolmate.Models.Product.*;
import com.example.coolmate.Repositories.CategoryRepository;
import com.example.coolmate.Repositories.Product.*;
import com.example.coolmate.Responses.ProductResponse;
import com.example.coolmate.Services.Impl.Product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;
    private final ProductDetailRepository productDetailRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws Exception {
        // Kiểm tra sự tồn tại của sản phẩm trùng lặp
        if (productRepository.existsByName(productDTO.getName())) {
            throw new DataNotFoundException("Product với tên '" + productDTO.getName() + "' đã tồn tại.");
        }

        // Tìm kiếm category theo id
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find category with id: " + productDTO.getCategoryId()));

        // Tạo sản phẩm mới
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .promotionPrice(productDTO.getPromotionPrice())
                .image(productDTO.getImage())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();

        // Lưu sản phẩm mới
        newProduct =  productRepository.save(newProduct);

        // Tạo và lưu các chi tiết của sản phẩm
        if(productDTO.getProductDetails()!=null){
            for (ProductDetailDTO detailDTO : productDTO.getProductDetails()){
                detailDTO.setProductId(newProduct.getId());
                createProductDetail(detailDTO);
            }
        }
        return newProduct;
    }


    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        //láy ds sp theo page và limit
        return productRepository
                .findAll(pageRequest)
                .map(ProductResponse::fromProduct);
    }

    @Override
    public Product getProductById(int productId) throws Exception {
        return productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy product id = "
                        + productId));
    }

    @Override
    public Product updateProduct(int id, ProductDTO productDTO) throws Exception {
        Product existingProduct = getProductById(id);
        if (existingProduct != null) {
            // Kiểm tra sự tồn tại của sản phẩm khác có cùng tên
            if (productRepository.existsByName(productDTO.getName())) {
                throw new DataNotFoundException("Product với tên '" + productDTO.getName() + "' bị trùng.");
            }

            Category existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException(
                            "Không tìm thấy category với id: " + productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setImage(productDTO.getImage());
            return productRepository.save(existingProduct);
        }
        throw new DataNotFoundException("Không tìm thấy product với id = " + id);
    }


    @Override
    public void deleteProduct(int id) throws DataNotFoundException {
        if (!productRepository.existsById(id)) {
            throw new DataNotFoundException("Không tìm thấy product id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> searchProductByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    @Override
    public ProductImage createProductImage(int productId, ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm có id: " + productId));

        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        int size = productImageRepository.findByProductId(productId).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException("Number of images must be <= 5");
        }

        return productImageRepository.save(newProductImage);
    }

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
        return productDetailRepository.save(newProductDetail);
    }


}
