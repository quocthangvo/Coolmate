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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService implements IPurchaseOrderService {


    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;
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

        // Kiểm tra sự tồn tại của mã đơn hàng (code)
        boolean isCodeExist = purchaseOrderRepository.existsByCode(purchaseOrderDTO.getCode());
        if (isCodeExist) {
            throw new DataNotFoundException("Mã đơn hàng đã tồn tại: " + purchaseOrderDTO.getCode());
        }


        // Tạo đối tượng PurchaseOrder và thiết lập các thuộc tính từ DTO và thông tin từ User
        PurchaseOrder newPurchaseOrder = new PurchaseOrder();
        newPurchaseOrder.setCode(purchaseOrderDTO.getCode());
        newPurchaseOrder.setOrderDate(new Date());
        newPurchaseOrder.setStatus(PurchaseOrderStatus.PENDING);
        newPurchaseOrder.setShippingDate(shippingDate);
        newPurchaseOrder.setSupplier(supplier);
        newPurchaseOrder.setUser(user);
        newPurchaseOrder.setActive(true);


        // Lưu đơn hàng vào cơ sở dữ liệu
        return purchaseOrderRepository.save(newPurchaseOrder);
    }


    @Override
    public PurchaseOrder getPurchaseOrderById(int id) throws DataNotFoundException {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy purchase order id = "
                        + id));
    }

    @Override
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }


    @Override
    public void deletePurchaseOrder(int id) throws DataNotFoundException {
        Optional<PurchaseOrder> optionalPurchaseOrder = purchaseOrderRepository.findById(id);
        if (optionalPurchaseOrder.isPresent()) {
            PurchaseOrder purchaseOrder = optionalPurchaseOrder.get();
            if (purchaseOrder.isActive()) {
                // Đánh dấu đơn hàng là không hoạt động (active = false)
                purchaseOrder.setActive(false);
                purchaseOrderRepository.save(purchaseOrder);

            } else {
                // Nếu đơn hàng đã được đánh dấu là không hoạt động, trả về thông báo đã xóa
                throw new DataNotFoundException("Đơn hàng có ID " + id + " đã được xóa trước đó.");
            }
        } else {
            // Nếu không tìm thấy đơn đặt hàng với ID đã cung cấp, trả về thông báo lỗi
            throw new DataNotFoundException("Không tìm thấy order id: " + id);
        }
    }


//    @Override
//    public PurchaseOrder updatePurchaseOrder(int purchaseOrderId, PurchaseOrderDTO purchaseOrderDTO) throws DataNotFoundException {
//        // Tìm PurchaseOrder hiện tại bằng ID
//        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId).orElseThrow(() ->
//                new DataNotFoundException("Không tìm thấy order id " + purchaseOrderId));
//
//        // Kiểm tra nếu thuộc tính "code" trong PurchaseOrderDTO trùng với một PurchaseOrder khác
//        PurchaseOrder existingPurchaseOrderByCode = purchaseOrderRepository.findByCode(purchaseOrderDTO.getCode());
//        if (existingPurchaseOrderByCode != null && existingPurchaseOrderByCode.getId() != purchaseOrderId) {
//            throw new DataNotFoundException("Mã đơn hàng đã tồn tại cho đơn hàng khác");
//        }
//
//        // Tìm User hiện tại bằng ID
//        User existingUser = userRepository.findById(purchaseOrderDTO.getUserId()).orElseThrow(() ->
//                new DataNotFoundException("Không tìm thấy user id " + purchaseOrderDTO.getUserId()));
//
//        // Cập nhật các thuộc tính của PurchaseOrder từ PurchaseOrderDTO
//        purchaseOrder.setUser(existingUser);
//        purchaseOrder.setCode(purchaseOrderDTO.getCode());
//        purchaseOrder.setOrderDate(new Date());
//        purchaseOrder.setShippingDate(purchaseOrderDTO.getShippingDate());
//        // Các thuộc tính khác nếu có trong PurchaseOrderDTO cần được cập nhật
//        // purchaseOrder.setOtherProperty(purchaseOrderDTO.getOtherProperty());
//
//        // Lưu đối tượng PurchaseOrder đã cập nhật vào cơ sở dữ liệu
//        return purchaseOrderRepository.save(purchaseOrder);
//    }

    @Override
    public PurchaseOrder updatePurchaseOrder(int purchaseOrderId, PurchaseOrderDTO purchaseOrderDTO) throws DataNotFoundException {
// Tìm PurchaseOrderDetail theo ID
        PurchaseOrder purchaseOrder = purchaseOrderRepository
                .findById(purchaseOrderId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy chi tiết đơn đặt hàng với ID: " + purchaseOrderId));

        // Các trạng thái hợp lệ
        List<String> validStatuses = Arrays.asList("pending", "processing", "shipping", "delivered", "cancelled");

        // Lấy trạng thái mới từ DTO
        String newStatus = purchaseOrderDTO.getStatus();

        // Kiểm tra xem status mới có hợp lệ không
        if (!validStatuses.contains(newStatus)) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ: " + newStatus);
        }

        // Cập nhật thuộc tính status
        purchaseOrder.setStatus(newStatus);

        // Lưu lại thay đổi vào cơ sở dữ liệu
        return purchaseOrderRepository.save(purchaseOrder);
    }

}
