package com.example.coolmate.Services.PurchaseOrder;

import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrderDetail;
import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.example.coolmate.Repositories.PurchaseOrder.PurchaseOrderDetailRepository;
import com.example.coolmate.Repositories.PurchaseOrder.PurchaseOrderRepository;
import com.example.coolmate.Responses.PurchaseOrders.PurchaseOrderDetailResponse;
import com.example.coolmate.Services.Impl.PurchaseOrder.IPurchaseOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderDetailService implements IPurchaseOrderDetailService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductDetailRepository productDetailRepository;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;

    public List<PurchaseOrderDetail> createPurchaseOrderDetail(PurchaseOrderDetailDTO purchaseOrderDetailDTO) throws DataNotFoundException {
//        // Tìm xem order có tồn tại không
//        PurchaseOrder purchaseOrder = purchaseOrderRepository
//                .findById(purchaseOrderDetailDTO.getPurchaseOrderId())
//                .orElseThrow(() -> new DataNotFoundException(
//                        "Không tìm thấy đơn đặt hàng id: " +
//                                purchaseOrderDetailDTO.getPurchaseOrderId()));
//
//
//        // Kiểm tra xem đơn đặt hàng ở trạng thái "pending" không
//        if (!purchaseOrder.getStatus().equals(PurchaseOrderStatus.PENDING)) {
//            throw new IllegalStateException(
//                    "Không thể thêm chi tiết đơn đặt hàng cho đơn hàng không ở trạng thái pending.");
//        }
//
//        List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();
//
//        for (PurchaseOrderDetailDTO.ProductDetailOrder detailOrder : purchaseOrderDetailDTO.getProductDetails()) {
//            // Tìm product theo id
//            ProductDetail productDetail = productDetailRepository
//                    .findById(detailOrder.getProductDetailId())
//                    .orElseThrow(() -> new DataNotFoundException(
//                            "Không tìm thấy chi tiết sản phẩm id: " + detailOrder.getProductDetailId()));
//
//            PurchaseOrderDetail purchaseOrderDetail = PurchaseOrderDetail.builder()
//                    .purchaseOrder(purchaseOrder)
//                    .productDetail(productDetail)
//                    .quantity(detailOrder.getQuantity())
//                    .price(detailOrder.getPrice())
//                    .build();
//
//            // Lưu vào danh sách chi tiết đơn đặt hàng
//            purchaseOrderDetails.add(purchaseOrderDetail);
//        }
//
//        // Lưu tất cả các chi tiết đơn đặt hàng vào cơ sở dữ liệu
//        return purchaseOrderDetailRepository.saveAll(purchaseOrderDetails);
        return null;
    }

    @Override
    public PurchaseOrderDetail getPurchaseOrderDetailById(int id) throws DataNotFoundException {
        return purchaseOrderDetailRepository.findById(id).orElseThrow(()
                -> new DataNotFoundException("Không tìm thấy order detail id" + id));
    }

    @Override
    public List<PurchaseOrderDetailResponse> findByPurchaseOrderId(PurchaseOrder purchaseOrder) throws DataNotFoundException {
        // Fetch purchase order details by purchaseOrder
        List<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrderDetailRepository.findByPurchaseOrder(purchaseOrder);

        // Check if no details are found and throw exception if needed
        if (purchaseOrderDetails.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy chi tiết đơn hàng với ID: " + purchaseOrder.getId());
        }

        // Convert each PurchaseOrderDetail entity to PurchaseOrderDetailResponse
        return purchaseOrderDetails.stream()
                .map(PurchaseOrderDetailResponse::fromPurchaseOrderDetail)
                .collect(Collectors.toList());
    }


    @Override
    public void deletePurchaseOrderDetail(int id) throws DataNotFoundException {
        Optional<PurchaseOrderDetail> optionalPurchaseOrderDetail = purchaseOrderDetailRepository.findById(id);
        if (optionalPurchaseOrderDetail.isPresent()) {
            PurchaseOrderDetail purchaseOrderDetail = optionalPurchaseOrderDetail.get();

            purchaseOrderDetailRepository.save(purchaseOrderDetail);
        } else {
            throw new DataNotFoundException("Đơn đặt hàng có '" + id + "' không tồn tại.");
        }
    }


    @Override
    public PurchaseOrderDetail updatePurchaseOrderDetail(int purchaseOrderDetailId, PurchaseOrderDetailDTO purchaseOrderDetailDTO)
            throws DataNotFoundException {
        PurchaseOrderDetail purchaseOrderDetail = purchaseOrderDetailRepository.findById(purchaseOrderDetailId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy chi tiết đơn đặt hàng với ID: " + purchaseOrderDetailId));

        // Lấy đối tượng PurchaseOrder từ PurchaseOrderDetail
        PurchaseOrder purchaseOrder = purchaseOrderDetail.getPurchaseOrder();

        // Cập nhật giá và số lượng từ PurchaseOrderDetailDTO
        purchaseOrderDetail.setQuantity(purchaseOrderDetailDTO.getQuantity());
        purchaseOrderDetail.setPrice(purchaseOrderDetailDTO.getPrice());

        // Lưu đối tượng đã cập nhật lại vào cơ sở dữ liệu
        return purchaseOrderDetailRepository.save(purchaseOrderDetail);
    }


}
