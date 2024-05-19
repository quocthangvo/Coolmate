package com.example.coolmate.Services;

import com.example.coolmate.Dtos.ProductDTO;
import com.example.coolmate.Dtos.ProductImageDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.InvalidParamException;
import com.example.coolmate.Models.Category;
import com.example.coolmate.Models.Product;
import com.example.coolmate.Models.ProductImage;
import com.example.coolmate.Repositories.CategoryRepository;
import com.example.coolmate.Repositories.ProductImageRepository;
import com.example.coolmate.Repositories.ProductRepository;
import com.example.coolmate.Responses.ProductResponse;
import com.example.coolmate.Services.Impl.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;

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
        return productRepository.save(newProduct);
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
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
        //kiểm tra sản phẩm có trong csdl
    }

    @Override
    public ProductImage createProductImage(int productId, ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy product id: " + productId));

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

}
