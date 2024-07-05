package com.example.coolmate.Services.PurchaseOrder;

import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrderDetail;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrderStatus;
import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.example.coolmate.Repositories.PurchaseOrder.PurchaseOrderDetailRepository;
import com.example.coolmate.Repositories.PurchaseOrder.PurchaseOrderRepository;
import com.example.coolmate.Services.Impl.PurchaseOrder.IPurchaseOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderDetailService implements IPurchaseOrderDetailService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductDetailRepository productDetailRepository;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;

    public List<PurchaseOrderDetail> createPurchaseOrderDetail(PurchaseOrderDetailDTO purchaseOrderDetailDTO) throws DataNotFoundException {
        // Tìm xem order có tồn tại không
        PurchaseOrder purchaseOrder = purchaseOrderRepository
                .findById(purchaseOrderDetailDTO.getPurchaseOrderId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Không tìm thấy đơn đặt hàng id: " +
                                purchaseOrderDetailDTO.getPurchaseOrderId()));

        // Kiểm tra xem đơn đặt hàng có hoạt động không
        if (!purchaseOrder.isActive()) {
            throw new IllegalStateException(
                    "Không thể thêm chi tiết đơn đặt hàng cho đơn hàng không hoạt động.");
        }

        // Kiểm tra xem đơn đặt hàng ở trạng thái "pending" không
        if (!purchaseOrder.getStatus().equals(PurchaseOrderStatus.PENDING)) {
            throw new IllegalStateException(
                    "Không thể thêm chi tiết đơn đặt hàng cho đơn hàng không ở trạng thái pending.");
        }

        List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();

        for (Integer productDetailId : purchaseOrderDetailDTO.getProductDetailId()) {
            // Tìm product theo id
            ProductDetail productDetail = productDetailRepository
                    .findById(productDetailId)
                    .orElseThrow(() -> new DataNotFoundException(
                            "Không tìm thấy chi tiết sản phẩm id: " + productDetailId));

            PurchaseOrderDetail purchaseOrderDetail = PurchaseOrderDetail.builder()
                    .purchaseOrderId(purchaseOrder)
                    .productDetail(productDetail)
                    .quantity(purchaseOrderDetailDTO.getQuantity())
                    .price(purchaseOrderDetailDTO.getPrice())
                    .unit(purchaseOrderDetailDTO.getUnit())
                    .active(purchaseOrderDetailDTO.isActive())
                    .build();

            // Lưu vào danh sách chi tiết đơn đặt hàng
            purchaseOrderDetails.add(purchaseOrderDetail);
        }

        // Lưu tất cả các chi tiết đơn đặt hàng vào cơ sở dữ liệu
        return purchaseOrderDetailRepository.saveAll(purchaseOrderDetails);
    }


    @Override
    public PurchaseOrderDetail getPurchaseOrderDetailById(int id) throws DataNotFoundException {
        return purchaseOrderDetailRepository.findById(id).orElseThrow(()
                -> new DataNotFoundException("Không tìm thấy order detail id" + id));
    }

    @Override
    public List<PurchaseOrderDetail> findPurchaseOrderById(int id) {
        return purchaseOrderDetailRepository.findPurchaseOrderById(id);
    }

    @Override
    public void deletePurchaseOrderDetail(int id) throws DataNotFoundException {
        Optional<PurchaseOrderDetail> optionalPurchaseOrderDetail = purchaseOrderDetailRepository.findById(id);
        if (optionalPurchaseOrderDetail.isPresent()) {
            PurchaseOrderDetail purchaseOrderDetail = optionalPurchaseOrderDetail.get();
            // Đặt trạng thái active của đơn hàng thành false
            purchaseOrderDetail.setActive(false);
            purchaseOrderDetailRepository.save(purchaseOrderDetail);
        } else {
            throw new DataNotFoundException("Đơn đặt hàng có '" + id + "' không tồn tại.");
        }
    }


    @Override
    public PurchaseOrderDetail updatePurchaseOrderDetail(int purchaseOrderDetailId, PurchaseOrderDetailDTO purchaseOrderDetailDTO) throws DataNotFoundException {
        return null;
    }
}
