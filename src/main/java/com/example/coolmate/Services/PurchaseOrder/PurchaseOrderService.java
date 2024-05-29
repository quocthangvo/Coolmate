package com.example.coolmate.Services.PurchaseOrder;

import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrderStatus;
import com.example.coolmate.Models.Supplier;
import com.example.coolmate.Models.User.User;
import com.example.coolmate.Repositories.PurchaseOrder.PurchaseOrderRepository;
import com.example.coolmate.Repositories.SupplierRepository;
import com.example.coolmate.Repositories.UserRepository;
import com.example.coolmate.Services.Impl.PurchaseOrder.IPurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService implements IPurchaseOrderService {


    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SupplierRepository supplierRepository;

    @Override
    public PurchaseOrder createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws DataNotFoundException {
        // Kiểm tra sự tồn tại của User
        User user = userRepository.findById(purchaseOrderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng với id: "
                        + purchaseOrderDTO.getUserId()));

        // Kiểm tra shipping date phải >= ngày hôm nay
        LocalDate shippingDate = purchaseOrderDTO.getShippingDate() == null ? LocalDate.now()
                : purchaseOrderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Ngày giao bắt đầu từ hôm nay");
        }

        // Kiểm tra sự tồn tại của Supplier
        Supplier supplier = supplierRepository.findById(purchaseOrderDTO.getSupplierId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy nhà cung cấp với id: "
                        + purchaseOrderDTO.getSupplierId()));

        // Tạo đối tượng PurchaseOrder và thiết lập các thuộc tính từ DTO và thông tin từ User
        PurchaseOrder newPurchaseOrder = new PurchaseOrder();
        newPurchaseOrder.setCode(purchaseOrderDTO.getCode());
        newPurchaseOrder.setOrderDate(new Date());
        newPurchaseOrder.setStatus(PurchaseOrderStatus.PENDING);
        newPurchaseOrder.setShippingDate(shippingDate);
        newPurchaseOrder.setSupplier(supplier);
        newPurchaseOrder.setUser(user);


        // Lưu đơn hàng vào cơ sở dữ liệu
        return purchaseOrderRepository.save(newPurchaseOrder);
    }


    @Override
    public PurchaseOrder getPurchaseOrderById(int id) {
        return null;
    }

    @Override
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return List.of();
    }

    @Override
    public void deletePurchaseOrder(int id) throws DataNotFoundException {
    }

    @Override
    public PurchaseOrder updatePurchaseOrder(int purchaseOrderId, PurchaseOrderDTO purchaseOrderDTO) throws DataNotFoundException {
        return null;
    }
}
