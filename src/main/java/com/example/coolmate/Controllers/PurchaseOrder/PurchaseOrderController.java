package com.example.coolmate.Controllers.PurchaseOrder;

import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import com.example.coolmate.Responses.ApiResponses.ApiResponse;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Responses.PurchaseOrders.PurchaseOrderResponse;
import com.example.coolmate.Services.Impl.PurchaseOrder.IPurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/purchase_orders")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})


public class PurchaseOrderController {
    private final IPurchaseOrderService purchaseOrderService;

    @PostMapping("")
    public ResponseEntity<?> createPurchaseOrder(@RequestBody PurchaseOrderDTO purchaseOrderDTO) {
        try {
            PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrderDTO);
            PurchaseOrderResponse purchaseOrderResponse = PurchaseOrderResponse.fromPurchase(purchaseOrder);
            return ApiResponseUtil.successResponse(
                    "Tạo đơn đặt haàng thành công", purchaseOrderResponse);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(e.getMessage()));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getPurchaseOrderById(@PathVariable("id") int purchaseChaseId) {
        try {
            PurchaseOrder existingPurchaseOrder = purchaseOrderService
                    .getPurchaseOrderById(purchaseChaseId);
            PurchaseOrderResponse purchaseOrderResponse = PurchaseOrderResponse.fromPurchase(existingPurchaseOrder);
            return ApiResponseUtil.successResponse(
                    "Purchase Order Detail created successfully", purchaseOrderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<PurchaseOrderResponse>>> getAllPurchaseOrders(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        Page<PurchaseOrder> purchaseOrdersPage = purchaseOrderService.getAllPurchaseOrders(page, limit); // page từ 0

        // Chuyển đổi từ PurchaseOrder sang PurchaseOrderResponse
        Page<PurchaseOrderResponse> purchaseOrderResponsesPage = purchaseOrdersPage.map(PurchaseOrderResponse::fromPurchase);

        return ApiResponseUtil.successResponse("Successfully", purchaseOrderResponsesPage);
    }


//    @GetMapping("")
//    public ResponseEntity<?> getAllPurchaseOrders(
//            @RequestParam(value = "page") int page,
//            @RequestParam(value = "limit") int limit) {
//
////        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
//        List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrders(page, limit);
//
//        return ApiResponseUtil.successResponse("Successfully", purchaseOrders);
//    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePurchaseOrder(@Valid @PathVariable int id) throws Exception {
        //xóa mềm => cập nhật trường active = false
        //không xóa mất đi order, mà chỉ xóa để active trở về 0
        try {
            purchaseOrderService.deletePurchaseOrder(id);
            String message = "Hủy đơn hàng có ID " + id + " thành công";
            return ResponseEntity.ok(new SuccessfulMessage(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePurchaseOrder(
            @PathVariable int id,
            @Valid @RequestBody PurchaseOrderDTO purchaseOrderDTO) {
        try {
            PurchaseOrder updatePurchaseOrder = purchaseOrderService.updatePurchaseOrder(id, purchaseOrderDTO);
            PurchaseOrderResponse purchaseOrderResponse = PurchaseOrderResponse.fromPurchase(updatePurchaseOrder);
            return ApiResponseUtil.successResponse(
                    "Đơn đặt hàng đã được xác nhận", purchaseOrderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProductByName(@RequestParam("code") String code) {
        try {
            List<PurchaseOrder> purchaseOrders = purchaseOrderService.searchPurchaseOrderByCode(code);
            if (purchaseOrders.isEmpty()) {
                ErrorMessage errorMessage = new ErrorMessage("Không tìm thấy sản phẩm có tên : " + code);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
            List<PurchaseOrderResponse> orderResponses = purchaseOrders.stream()
                    .map(PurchaseOrderResponse::fromPurchase)
                    .collect(Collectors.toList());

            return ApiResponseUtil.successResponse("Successfully", orderResponses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage("Đã xảy ra lỗi khi tìm kiếm tên sản phẩm : "
                    + e.getMessage()));
        }
    }

    @GetMapping("/order_date")
    public ResponseEntity<ApiResponse<List<?>>> getOrdersByDate(
            @RequestParam("orderDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate orderDate) {
        List<PurchaseOrder> orders = purchaseOrderService.getOrderDate(orderDate);

        // Map each PurchaseOrder to PurchaseOrderResponse
        List<PurchaseOrderResponse> orderResponses = orders.stream()
                .map(PurchaseOrderResponse::fromPurchase)
                .collect(Collectors.toList());

        return ApiResponseUtil.successResponse("Successfully", orderResponses);

    }
}
