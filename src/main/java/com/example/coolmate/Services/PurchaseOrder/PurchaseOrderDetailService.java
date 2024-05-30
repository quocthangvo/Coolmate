package com.example.coolmate.Services.PurchaseOrder;

import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrderDetail;
import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.example.coolmate.Repositories.PurchaseOrder.PurchaseOrderDetailRepository;
import com.example.coolmate.Repositories.PurchaseOrder.PurchaseOrderRepository;
import com.example.coolmate.Services.Impl.PurchaseOrder.IPurchaseOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderDetailService implements IPurchaseOrderDetailService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductDetailRepository productDetailRepository;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;

    @Override
    public PurchaseOrderDetail createPurchaseOrderDetail(PurchaseOrderDetailDTO purchaseOrderDetailDTO) throws DataNotFoundException {
        //tìm xem order có tồn tại ko
        PurchaseOrder purchaseOrder = purchaseOrderRepository
                .findById(purchaseOrderDetailDTO.getPurchaseOrderId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Không tìm thấy đơn đặt hàng id: " + purchaseOrderDetailDTO.getPurchaseOrderId()));
        //tìm product theo id
        ProductDetail productDetail = productDetailRepository
                .findById(purchaseOrderDetailDTO.getProductDetailId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Không tìm thấy chi tiết sản phẩm id: " + purchaseOrderDetailDTO.getPurchaseOrderId()));
        PurchaseOrderDetail purchaseOrderDetail = PurchaseOrderDetail.builder()
                .purchaseOrderId(purchaseOrder)
                .productDetailId(productDetail)
                .quantity(purchaseOrderDetailDTO.getQuantity())
                .price(purchaseOrderDetailDTO.getPrice())
                .unit(purchaseOrderDetailDTO.getUnit())
                .active(true)
                .build();
        //lưu vào db
        return purchaseOrderDetailRepository.save(purchaseOrderDetail);
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
