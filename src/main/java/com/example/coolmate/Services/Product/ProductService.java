package com.example.coolmate.Services.Product;

import com.example.coolmate.Dtos.ProductDtos.ProductDTO;
import com.example.coolmate.Dtos.ProductDtos.ProductImageDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.InvalidParamException;
import com.example.coolmate.Models.Category;
import com.example.coolmate.Models.Product.*;
import com.example.coolmate.Repositories.CategoryRepository;
import com.example.coolmate.Repositories.Product.*;
import com.example.coolmate.Responses.Product.ProductResponse;
import com.example.coolmate.Services.CategoryService;
import com.example.coolmate.Services.Impl.Product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;
    private final ProductDetailRepository productDetailRepository;

    private final CategoryService categoryService;

    @Override
    public Product createProduct(ProductDTO productDTO) throws Exception {
        // Check for duplicate product
        if (productRepository.existsByName(productDTO.getName())) {
            throw new DataNotFoundException("Sản phẩm với tên '" + productDTO.getName() + "' đã tồn tại.");
        }

        // Find category by ID
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Không tìm thấy danh mục với ID: " + productDTO.getCategoryId()));


        // Create new product
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .image(productDTO.getImage())
                .description(productDTO.getDescription())
                .categoryId(existingCategory)
                .build();

        // Save the product to the database
        newProduct = productRepository.save(newProduct);


//duyệt vòng lập để truyền list
        for (Integer size : productDTO.getSizeId()) {
            for (Integer color : productDTO.getColorId()) {
                Size existingSize = sizeRepository.findById(size).orElseThrow(()
                        -> new DataNotFoundException("not found" + productDTO.getSizeId()));
                Color existingColor = colorRepository.findById(color).orElseThrow(()
                        -> new DataNotFoundException("not found" + productDTO.getColorId()));
                ProductDetail newProductDetail =
                        ProductDetail.builder()
                                .product(newProduct)
                                .size(existingSize)
                                .color(existingColor)
                                .build();
                productDetailRepository.save(newProductDetail);
            }
        }

//        Integer colorId = 1;
//        Integer sizeId = 1;
//
//        Size size = sizeRepository.findById(sizeId).orElseThrow(() -> new DateTimeException("not found id"));
//        Color color = colorRepository.findById(colorId).orElseThrow(() -> new DateTimeException("not found id"));
//
//        ProductDetail newProductDetail = ProductDetail.builder()
//                .product(newProduct)
//                .size(existingSize)
//                .color(existingColor)
//                .build();
//
//        productDetailRepository.save(newProductDetail);


//

        return newProduct;
    }


    @Override
    public List<ProductResponse> getAllProducts(int page, int limit) {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
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
            existingProduct.setCategoryId(existingCategory);

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

    public List<Product> findByCategoryId(int categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null) {
            return productRepository.findByCategoryId(category);
        }
        return List.of();
    }


}
