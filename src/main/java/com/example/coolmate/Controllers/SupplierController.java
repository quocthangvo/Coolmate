package com.example.coolmate.Controllers;

import com.example.coolmate.Dtos.SupplierDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.Supplier;
import com.example.coolmate.Responses.ApiResponses.ApiResponse;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Services.Impl.ISupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/suppliers")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")

public class SupplierController {
    private final ISupplierService supplierService;

    @PostMapping("")
    public ResponseEntity<?> createSupplier(
            @Valid @RequestBody SupplierDTO supplierDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                // Trả về lỗi nếu có lỗi trong dữ liệu đầu vào
                return ResponseEntity.badRequest().body("Dữ liệu đầu vào không hợp lệ");
            }

            Supplier createdSupplier = supplierService.createSupplier(supplierDTO);
            return ApiResponseUtil.successResponse("Supplier created successfully", createdSupplier); // Trả về dữ liệu của danh mục được tạo
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("Đã xảy ra lỗi không xác định"));
        }
    }


    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Supplier>>> getAllSuppliers(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Supplier> suppliers = supplierService.getAllSuppliers(page, limit);
        return ApiResponseUtil.successResponse("Successfully", suppliers);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable int id) {
        try {
            Supplier supplier = supplierService.getSupplierById(id);
            return ApiResponseUtil.successResponse("Successfully", supplier);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable int id) {
        try {
            supplierService.deleteSupplier(id);
            String message = "Xóa nhà cung cấp thành công";
            return ResponseEntity.ok(new SuccessfulMessage(message));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(
            @PathVariable int id,
            @Valid @RequestBody SupplierDTO supplierDTO) {
        try {
            Supplier updatedSupplier = supplierService.updateSupplier(id, supplierDTO);
            return ApiResponseUtil.successResponse("CẬp nhật nhà cung cấp thành công", updatedSupplier);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSupplierByName(@RequestParam("name") String name) {
        try {
            List<Supplier> suppliers = supplierService.searchSupplierByName(name);
            if (suppliers.isEmpty()) {
                ErrorMessage errorMessage = new ErrorMessage("Không tìm thấy nhà cung cấp có tên : " + name);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
            return ApiResponseUtil.successResponse("successful ", suppliers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage("Đã xảy ra lỗi khi tìm kiếm tên sản phẩm : "
                    + e.getMessage()));
        }
    }
}
