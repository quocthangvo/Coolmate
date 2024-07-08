package com.example.coolmate.Services.PurchaseOrder;

import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrderStatus;
import com.example.coolmate.Models.Supplier;
import com.example.coolmate.Models.User.User;
import com.example.coolmate.Repositories.InventoryRepository;
import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.example.coolmate.Repositories.PurchaseOrder.PurchaseOrderDetailRepository;
import com.example.coolmate.Repositories.PurchaseOrder.PurchaseOrderRepository;
import com.example.coolmate.Repositories.SupplierRepository;
import com.example.coolmate.Repositories.UserRepository;
import com.example.coolmate.Services.Impl.IInventoryService;
import com.example.coolmate.Services.Impl.PurchaseOrder.IPurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService implements IPurchaseOrderService {


    // Hằng số để định dạng mã đơn hàng
    private static final String ORDER_CODE_PREFIX = "DH";
    private static final int ORDER_CODE_LENGTH = 6; // Độ dài của mã đơn hàng (bao gồm cả prefix)
    private static final int ORDER_CODE_NUMBER_LENGTH = 4; // Số lượng chữ số trong mã số đơn hàng
    // Biến để lưu số thứ tự hiện tại
    private static int currentOrderNumber = 0;


    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;
    private final ProductDetailRepository productDetailRepository;
    private final InventoryRepository inventoryRepository;
    private IInventoryService inventoryService;


    @Override
    public PurchaseOrder createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws DataNotFoundException {
        User user = userRepository.findByRoleId(2)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng có quyền admin"));

        // Ngày đặt hàng bắt đầu từ hôm nay
        LocalDateTime orderDate = LocalDateTime.now();

        // Ngày giao hàng dự kiến là 3 ngày sau khi đặt hàng
        LocalDate shippingDate = orderDate.plusDays(3).toLocalDate();

        Supplier supplier = supplierRepository.findById(purchaseOrderDTO.getSupplierId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy nhà cung cấp với id: "
                        + purchaseOrderDTO.getSupplierId()));

        // Tạo đối tượng PurchaseOrder và thiết lập các thuộc tính từ DTO và thông tin từ User
        PurchaseOrder newPurchaseOrder = new PurchaseOrder();

        String orderCode = generateOrderCode();
        newPurchaseOrder.setCode(orderCode);
        newPurchaseOrder.setOrderDate(orderDate);
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
    public List<PurchaseOrder> getAllPurchaseOrders(int page, int limit) {
        return purchaseOrderRepository.findAll();
    }


    @Override
    public void deletePurchaseOrder(int id) throws DataNotFoundException {
        Optional<PurchaseOrder> optionalPurchaseOrder = purchaseOrderRepository.findById(id);
        if (optionalPurchaseOrder.isPresent()) {
            PurchaseOrder purchaseOrder = optionalPurchaseOrder.get();
            if (purchaseOrder.isActive() && !PurchaseOrderStatus.DELIVERED.equals(purchaseOrder.getStatus())) {
                // Đánh dấu đơn hàng là không hoạt động (active = false)
                purchaseOrder.setActive(false);
                purchaseOrder.setStatus(PurchaseOrderStatus.CANCELLED);
                purchaseOrderRepository.save(purchaseOrder);
            } else {
                // Nếu đơn hàng đã được đánh dấu là không hoạt động hoặc đã được giao, trả về thông báo
                throw new DataNotFoundException("Đơn hàng đã xóa hoặc được giao.");
            }
        } else {
            // Nếu không tìm thấy đơn đặt hàng với ID đã cung cấp, trả về thông báo lỗi
            throw new DataNotFoundException("Không tìm thấy order id: " + id);
        }
    }


    @Override
    public PurchaseOrder updatePurchaseOrder(int purchaseOrderId, PurchaseOrderDTO purchaseOrderDTO)
            throws DataNotFoundException {
        // Tìm PurchaseOrder theo ID
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn đặt hàng với ID: " + purchaseOrderId));

        // Kiểm tra xem nếu đơn đặt hàng đã được hủy hoặc đã giao hàng, không cho phép thay đổi trạng thái
        if (!purchaseOrder.isActive() || PurchaseOrderStatus.DELIVERED.equals(purchaseOrder.getStatus())) {
            throw new IllegalStateException("Đơn đặt hàng đã bị hủy hoặc đã được giao.");
        }
        //cập nhật
        purchaseOrder.setActive(false);
        purchaseOrder.setStatus(PurchaseOrderStatus.DELIVERED);

        // Lưu
        return purchaseOrderRepository.save(purchaseOrder);
    }

    @Override
    public List<PurchaseOrder> searchPurchaseOrderByCode(String code) {
        return purchaseOrderRepository.searchPurchaseOrderByCode(code);

    }

    @Override
    public List<PurchaseOrder> getOrderDate(LocalDate orderDate) {
        return purchaseOrderRepository.findByOrderDate(orderDate);
    }


    // Hàm để sinh mã đơn hàng mới
    public String generateOrderCode() {
        // Tăng số thứ tự lên mỗi khi tạo đơn hàng mới
        currentOrderNumber++;

        // Định dạng chuỗi số thứ tự với số lượng chữ số quy định
        DecimalFormat df = new DecimalFormat("0".repeat(ORDER_CODE_NUMBER_LENGTH));
        String orderNumber = df.format(currentOrderNumber);

        // Kiểm tra và đảm bảo mã đơn hàng không trùng lặp
        String orderCode = ORDER_CODE_PREFIX + orderNumber;
        while (purchaseOrderRepository.existsByCode(orderCode)) {
            currentOrderNumber++;
            orderNumber = df.format(currentOrderNumber);
            orderCode = ORDER_CODE_PREFIX + orderNumber;
        }

        return orderCode;
    }
}
