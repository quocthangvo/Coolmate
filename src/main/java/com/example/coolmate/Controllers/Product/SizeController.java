package com.example.coolmate.Controllers.Product;

import com.example.coolmate.Dtos.CategoryDTO;
import com.example.coolmate.Dtos.ProductDtos.SizeDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.Category;
import com.example.coolmate.Models.Product.Size;
import com.example.coolmate.Responses.ApiResponses.ApiResponse;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Services.Impl.Product.ISizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/sizes")
@RequiredArgsConstructor
public class SizeController {
    private final ISizeService sizeService;

    @PostMapping("")
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody SizeDTO sizeDTO, BindingResult result) {
        try {
            Size createSize = sizeService.createSize(sizeDTO);
            ApiResponse<Size> response = new ApiResponse<>(
                    "Size created successfully", createSize);
            return ResponseEntity.ok(response);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("Đã xảy ra lỗi không xác định"));
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Size>> getAllSize(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Size> sizes = sizeService.getAllSizes();
        return ResponseEntity.ok(sizes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSizeById(@PathVariable int id) {
        try {
            Size size = sizeService.getSizeById(id);
            return ResponseEntity.ok(size);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSizeById(@PathVariable int id) {
        try {
            sizeService.deleteSizeById(id);
            String message = "Xóa kích thước có ID " + id + " thành công";
            return ResponseEntity.ok(new SuccessfulMessage(message));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSize(
            @PathVariable int id,
            @Valid @RequestBody SizeDTO sizeDTO) {
        try {
            Size updateSize = sizeService.updateSize(id,sizeDTO);
            return ApiResponseUtil.successResponse("Size updated successfully", updateSize);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }
}
