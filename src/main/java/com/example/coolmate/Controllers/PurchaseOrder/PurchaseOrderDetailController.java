package com.example.coolmate.Controllers.PurchaseOrder;

import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrderDetail;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrderStatus;
import com.example.coolmate.Repositories.PurchaseOrder.PurchaseOrderDetailRepository;
import com.example.coolmate.Responses.ApiResponses.ApiResponse;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Services.Impl.PurchaseOrder.IPurchaseOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/purchase_order_details")
@CrossOrigin("http://localhost:3000")


public class PurchaseOrderDetailController {
    private final IPurchaseOrderDetailService purchaseOrderDetailService;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;


    @PostMapping("")
    public ResponseEntity<?> createPurchaseOrderDetail(
            @Valid @RequestBody PurchaseOrderDetailDTO purchaseOrderDetailDTO
    ) {
        try {
            List<PurchaseOrderDetail> newPurchaseOrderDetails = purchaseOrderDetailService
                    .createPurchaseOrderDetail(purchaseOrderDetailDTO);
            return ApiResponseUtil.successResponse("Purchase Order Details created successfully", newPurchaseOrderDetails);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }


    @GetMapping("/purchase_order/{purchaseOrderId}")
    public ResponseEntity<ApiResponse<List<PurchaseOrderDetail>>> findByProductId(@PathVariable PurchaseOrder purchaseOrderId) {
        try {
            List<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrderDetailService.findByPurchaseOrderId(purchaseOrderId);
            return ApiResponseUtil.successResponse("Successfully", purchaseOrderDetails);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPurchaseOrderDetailById(@PathVariable int id) {
        try {
            PurchaseOrderDetail purchaseOrderDetail = purchaseOrderDetailService.getPurchaseOrderDetailById(id);
            return ApiResponseUtil.successResponse("Successfully", purchaseOrderDetail);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePurchaseOrderDetailById(
            @Valid @PathVariable("id") int id) {
        try {
            purchaseOrderDetailService.deletePurchaseOrderDetail(id);
            String message = "Xóa chi tiết đơn đặt hàng có ID " + id + " thành công";
            return ResponseEntity.ok(new SuccessfulMessage(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePurchaseOrderDetailById(
            @Valid @PathVariable("id") int id,
            @RequestBody PurchaseOrderDetail updatedDetail) throws DataNotFoundException {
        PurchaseOrderDetail existingDetail = purchaseOrderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy chi tiết đơn đặt hàng id: " + id));

        // Lấy đối tượng PurchaseOrder từ PurchaseOrderDetail
        PurchaseOrder purchaseOrder = existingDetail.getPurchaseOrder();

        // Kiểm tra xem nếu đơn đặt hàng đã được giao hàng, không cho phép cập nhật chi tiết
        if (PurchaseOrderStatus.DELIVERED.equals(purchaseOrder.getStatus())) {
            throw new IllegalStateException("Không thể cập nhật chi tiết đơn hàng đã giao.");
        }
        if (PurchaseOrderStatus.CANCELLED.equals(purchaseOrder.getStatus())) {
            throw new IllegalStateException("Không thể cập nhật đơn hàng đã hủy");
        }

        existingDetail.setQuantity(updatedDetail.getQuantity());
        existingDetail.setPrice(updatedDetail.getPrice());
        purchaseOrderDetailRepository.save(existingDetail);

        return ResponseEntity.ok("Chi tiết đơn đặt hàng đã được cập nhật thành công.");
    }


}
