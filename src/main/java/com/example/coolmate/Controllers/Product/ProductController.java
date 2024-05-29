package com.example.coolmate.Controllers.Product;

import com.example.coolmate.Dtos.ProductDtos.ProductDTO;
import com.example.coolmate.Dtos.ProductDtos.ProductImageDTO;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.Product.Product;
import com.example.coolmate.Models.Product.ProductImage;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Responses.ProductResponse;
import com.example.coolmate.Services.Impl.Product.IProductService;
import com.example.coolmate.Services.ProductServices.ProductListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            // Tạo sản phẩm mới
            Product newProduct = productService.createProduct(productDTO);
            // Trả về cả thông điệp thành công và dữ liệu sản phẩm
            return ApiResponseUtil.successResponse("Product create successfully", newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }


    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();

        return ResponseEntity.ok(ProductListResponse
                .builder()
                .products(products)
                .totalPage(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") int productId) {
        try {
            Product existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        try {
            productService.deleteProduct(id);
            String message = "Xóa sản phẩm có ID " + id + " thành công";
            return ResponseEntity.ok(new SuccessfulMessage(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable int id,
                                           @RequestBody ProductDTO productDTO) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDTO);
            return ApiResponseUtil.successResponse("Product updated successfully", updatedProduct);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

    @PostMapping(value = "uploads/{id}")
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") int productId,
            @ModelAttribute("files") List<MultipartFile> files) {
        try {
            Product existingProduct = productService.getProductById(productId);
            // biến files kt có null ko, nếu null tạo 1 ds rỗng mới
            files = files == null ? new ArrayList<MultipartFile>() : files;
            //số lượng ảnh dc up 1 lần -> 5 ảnh
            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body("you can only upload more than 5 images");
            }
            List<ProductImage> productImages = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                // kiểm tra kích thước file định dạng
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large? Maximum size is 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                String filename = storeFile(file);
                //lưu vào đối tượng trên database
                ProductImage productImage = productService.createProductImage(
                        existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(filename)
                                .build());
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        //thêm UUID vào trươớc tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        //đường dẫn dến thư mục mà bạn muốn lưu file
        Path uploadDir = Paths.get("uploads");
        //kiểm tra và tạo thư mục nếu nó k tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        //đường dẫn đầy đủ đến file
        Path destination = uploadDir.resolve(uniqueFilename);
        //sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null || contentType.startsWith("image/");
    }
}
