package com.example.coolmate.Controllers.PurchaseOrder;

import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDetailDTO;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrderDetail;
import com.example.coolmate.Repositories.PurchaseOrder.PurchaseOrderDetailRepository;
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
public class PurchaseOrderDetailController {
    private final IPurchaseOrderDetailService purchaseOrderDetailService;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;


    @PostMapping("")
    public ResponseEntity<?> createPurchaseOrderDetail(
            @Valid @RequestBody PurchaseOrderDetailDTO purchaseOrderDetailDTO
    ) {
        try {
            PurchaseOrderDetail newPurchaseOrderDetail = purchaseOrderDetailService
                    .createPurchaseOrderDetail(purchaseOrderDetailDTO);
            return ApiResponseUtil.successResponse("Purchase Order Detail created successfully", newPurchaseOrderDetail);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

    @GetMapping("/purchase_order/{purchaseOrderId}")
    public ResponseEntity<?> getpurchaseOrderId(@Valid @PathVariable("purchaseOrderId") int purchaseOrderId) {
        List<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrderDetailService.findPurchaseOrderById(purchaseOrderId);

        return ResponseEntity.ok().body(purchaseOrderDetails);


    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetailById(@Valid @PathVariable("id") int id) {
        try {
            PurchaseOrderDetail purchaseOrderDetail = purchaseOrderDetailService.getPurchaseOrderDetailById(id);
            return ResponseEntity.ok().body(purchaseOrderDetail);// trả về theo from đã định dạng
            //        return ResponseEntity.ok(orderDetail);
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
}
