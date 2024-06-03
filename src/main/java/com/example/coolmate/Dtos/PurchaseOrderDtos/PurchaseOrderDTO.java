package com.example.coolmate.Dtos.PurchaseOrderDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderDTO {

    private String code;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("supplier_id")
    private int supplierId;

    private String status;

}
