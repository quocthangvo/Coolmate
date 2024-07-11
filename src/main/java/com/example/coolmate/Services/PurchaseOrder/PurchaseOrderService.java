package com.example.coolmate.Services.PurchaseOrder;

import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Inventory;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrderDetail;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService implements IPurchaseOrderService {

    // Mã đơn hàng có độ dài cố định
    private static final int ORDER_CODE_LENGTH = 10;
    // Để đảm bảo độ dài mã đơn hàng cố định, bạn có thể sử dụng prefix
    private static final String ORDER_CODE_PREFIX = "ORD-";


    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;
    private final ProductDetailRepository productDetailRepository;
    private final InventoryRepository inventoryRepository;
    private IInventoryService inventoryService;


    @Override
    public PurchaseOrder createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws DataNotFoundException {
        // Tìm người dùng có quyền admin
        User user = userRepository.findByRoleId(2)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng có quyền admin"));

        // Ngày đặt hàng bắt đầu từ hôm nay
        LocalDateTime orderDate = LocalDateTime.now();

        // Ngày giao hàng dự kiến là 3 ngày sau khi đặt hàng
        LocalDate shippingDate = orderDate.plusDays(3).toLocalDate();

        // Tìm nhà cung cấp theo ID từ DTO
        Supplier supplier = supplierRepository.findById(purchaseOrderDTO.getSupplierId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy nhà cung cấp với id: " + purchaseOrderDTO.getSupplierId()));

        // Tạo đối tượng PurchaseOrder và thiết lập các thuộc tính từ DTO và thông tin từ User
        PurchaseOrder newPurchaseOrder = PurchaseOrder.builder()
                .code(generateRandomCode())
                .orderDate(orderDate)
                .status(PurchaseOrderStatus.PENDING)
                .shippingDate(shippingDate)
                .supplier(supplier)
                .user(user)
                .versionCode(purchaseOrderDTO.getVersionCode())
                .build();

        // Kiểm tra và xử lý danh sách productDetailId từ DTO
        List<Integer> productDetailIds = purchaseOrderDTO.getProductDetailId();
        if (productDetailIds == null || productDetailIds.isEmpty()) {
            throw new IllegalArgumentException("Danh sách productDetailId không được để trống hoặc null.");
        }

        // Tạo danh sách chi tiết đơn hàng từ danh sách productDetailId từ DTO
        List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();
        for (Integer productDetailId : productDetailIds) {
            // Tìm product detail theo ID từ DTO
            ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy chi tiết sản phẩm id: " + productDetailId));

            // Tạo đối tượng PurchaseOrderDetail từ DTO
            PurchaseOrderDetail purchaseOrderDetail = PurchaseOrderDetail.builder()
                    .purchaseOrder(newPurchaseOrder)
                    .productDetail(productDetail)
                    .quantity(purchaseOrderDTO.getQuantity()) // Sử dụng cùng một quantity cho tất cả sản phẩm trong đơn hàng
                    .price(purchaseOrderDTO.getPrice()) // Sử dụng cùng một price cho tất cả sản phẩm trong đơn hàng
                    .build();

            // Thêm vào danh sách chi tiết đơn hàng
            purchaseOrderDetails.add(purchaseOrderDetail);
        }

        // Lưu đơn hàng vào cơ sở dữ liệu
        newPurchaseOrder = purchaseOrderRepository.save(newPurchaseOrder);

        // Lưu danh sách chi tiết đơn hàng vào cơ sở dữ liệu
        purchaseOrderDetailRepository.saveAll(purchaseOrderDetails);

        // Trả về đối tượng PurchaseOrder đã được lưu
        return newPurchaseOrder;
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
            if (!PurchaseOrderStatus.DELIVERED.equals(purchaseOrder.getStatus())) {
                // Đánh dấu đơn hàng là không hoạt động (active = false)

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

        // Kiểm tra xem nếu đơn đặt hàng đã được giao hàng, không cho phép thay đổi trạng thái
        if (PurchaseOrderStatus.DELIVERED.equals(purchaseOrder.getStatus())) {
            throw new IllegalStateException("Đơn đặt hàng đã được giao.");
        }

        // Cập nhật trạng thái đơn hàng thành DELIVERED
        purchaseOrder.setStatus(PurchaseOrderStatus.DELIVERED);

        // Lưu lại đơn hàng đã được cập nhật trạng thái
        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        // Lưu chi tiết sản phẩm vào kho
        List<PurchaseOrderDetail> orderDetails = purchaseOrder.getPurchaseOrderDetails();
        for (PurchaseOrderDetail detail : orderDetails) {
            ProductDetail productDetail = detail.getProductDetail();
            int orderedQuantity = detail.getQuantity();

            // Lấy thông tin kho hiện tại
            Inventory inventory = inventoryRepository.findByProductDetail(productDetail);

            // Kiểm tra nếu không có bản ghi kho cho sản phẩm này, tạo mới
            if (inventory == null) {
                inventory = new Inventory();
                inventory.setProductDetail(productDetail);
                inventory.setQuantity(0);
                inventory.setReservedQuantity(0);
            }

            // Cập nhật kho sau khi giao hàng
            inventory.setQuantity(inventory.getQuantity() + orderedQuantity);
            inventoryRepository.save(inventory);
        }

        return purchaseOrder;
    }


    @Override
    public List<PurchaseOrder> searchPurchaseOrderByCode(String code) {
        return purchaseOrderRepository.searchPurchaseOrderByCode(code);

    }

    @Override
    public List<PurchaseOrder> getOrderDate(LocalDate orderDate) {
        return purchaseOrderRepository.findByOrderDate(orderDate);
    }


    private String generateRandomCode() {
        // Tạo một chuỗi ngẫu nhiên với UUID và cắt nó để đảm bảo độ dài cố định
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, ORDER_CODE_LENGTH - ORDER_CODE_PREFIX.length());
        return ORDER_CODE_PREFIX + uuid;
    }
}
