package com.example.coolmate.Models;

import com.example.coolmate.Models.Product.ProductDetail;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventories")
@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "reserved_quantity")
    private int reservedQuantity;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;


//    @OneToMany(mappedBy = "inventory")
//    private List<ProductDetail> productDetails;
}
