package com.example.coolmate.Models.PurchaseOrder;

import com.example.coolmate.Models.BaseEntity;
import com.example.coolmate.Models.Supplier;
import com.example.coolmate.Models.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "purchase_order")
@Entity
public class PurchaseOrder extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "order_date", nullable = false)
    private Date orderDate;

    @Column(name = "status")
    private String status;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

}
