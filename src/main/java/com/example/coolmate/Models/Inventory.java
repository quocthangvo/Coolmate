//package com.example.coolmate.Models;
//
//import com.example.coolmate.Models.Product.ProductDetail;
//import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Builder
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "inventories")
//@Entity
//public class Inventory extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private int id;
//
//    @Column(name = "inventory_quantity")
//    private int quantity;
//
//    @ManyToOne
//    @JoinColumn(name = "purchase_order_id")
//    private PurchaseOrder purchaseOrderId;
//
//    @ManyToOne
//    @JoinColumn(name = "product_detail_id")
//    private ProductDetail productDetailId;
//}
