package com.example.coolmate.Services.PurchaseOrder;

import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDTO;
import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDetailDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // độ dài
    private static final int ORDER_CODE_LENGTH = 10;
    // mã có ORD-
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
        // Tìm người dùng theo ID từ DTO
        User user = userRepository.findById(purchaseOrderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng với id: " + purchaseOrderDTO.getUserId()));

        // Kiểm tra quyền người dùng
        if (user.getRole().getId() != 2) {
            throw new DataNotFoundException("Người dùng không có quyền admin");
        }

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

        // Kiểm tra và xử lý danh sách chi tiết đơn hàng từ DTO
        List<PurchaseOrderDetailDTO> purchaseOrderDetailDTOs = purchaseOrderDTO.getPurchaseOrderDetails();
        if (purchaseOrderDetailDTOs == null || purchaseOrderDetailDTOs.isEmpty()) {
            throw new IllegalArgumentException("Danh sách chi tiết đơn hàng không được để trống hoặc null.");
        }

        // Tạo danh sách chi tiết đơn hàng từ DTO
        List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();
        for (PurchaseOrderDetailDTO purchaseOrderDetailDTO : purchaseOrderDetailDTOs) {
            // Tìm product detail theo ID từ DTO
            ProductDetail productDetail = productDetailRepository.findById(purchaseOrderDetailDTO.getProductDetailId())
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy chi tiết sản phẩm với id: " + purchaseOrderDetailDTO.getProductDetailId()));

            // Tạo đối tượng PurchaseOrderDetail từ DTO
            PurchaseOrderDetail purchaseOrderDetail = PurchaseOrderDetail.builder()
                    .purchaseOrder(newPurchaseOrder)
                    .productDetail(productDetail)
                    .quantity(purchaseOrderDetailDTO.getQuantity())
                    .price(purchaseOrderDetailDTO.getPrice())
                    .build();

            // Thêm vào danh sách chi tiết đơn hàng
            purchaseOrderDetails.add(purchaseOrderDetail);
        }

        // Lưu đơn hàng vào cơ sở dữ liệu
        newPurchaseOrder = purchaseOrderRepository.save(newPurchaseOrder);

        // Liên kết danh sách chi tiết đơn hàng với đơn hàng và lưu vào cơ sở dữ liệu
        for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetails) {
            purchaseOrderDetail.setPurchaseOrder(newPurchaseOrder);
        }
        purchaseOrderDetailRepository.saveAll(purchaseOrderDetails);

        // Trả về đối tượng PurchaseOrder đã được lưu
        return newPurchaseOrder;
    }

    private String generateRandomCode() {
        // Tạo một chuỗi ngẫu nhiên với UUID và cắt nó để đảm bảo độ dài cố định
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, ORDER_CODE_LENGTH - ORDER_CODE_PREFIX.length());
        return ORDER_CODE_PREFIX + uuid;
    }

    @Override
    public PurchaseOrder getPurchaseOrderById(int id) throws DataNotFoundException {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy purchase order id = "
                        + id));
    }

    @Override
    public Page<PurchaseOrder> getAllPurchaseOrders(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "orderDate"));
        return purchaseOrderRepository.findAll(pageable);
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
        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        // Lưu chi tiết sản phẩm vào kho
        List<PurchaseOrderDetail> orderDetails = purchaseOrder.getPurchaseOrderDetails();
        for (PurchaseOrderDetail detail : orderDetails) {
            ProductDetail productDetail = detail.getProductDetail();
            int orderedQuantity = detail.getQuantity();
            float purchasePrice = detail.getPrice(); // Lấy giá nhập từ chi tiết đơn hàng

            // Lấy thông tin kho hiện tại
            Inventory inventory = inventoryRepository.findByProductDetail(productDetail);

            // Kiểm tra nếu không có bản ghi kho cho sản phẩm này, tạo mới
            if (inventory == null) {
                inventory = new Inventory();
                inventory.setProductDetail(productDetail);
                inventory.setQuantity(0);
                inventory.setInventoryQuantity(0);
                inventory.setPrice(0);
            }

            // Cập nhật kho sau khi giao hàng
            inventory.setQuantity(inventory.getQuantity() + orderedQuantity);
            inventory.setInventoryQuantity(inventory.getInventoryQuantity() + orderedQuantity); // Cập nhật số lượng tồn kho
            inventory.setPrice(purchasePrice); // Cập nhật giá nhập hàng

            inventoryRepository.save(inventory);
        }

        // Lưu lại đơn hàng đã được cập nhật trạng thái
        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);
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


}
