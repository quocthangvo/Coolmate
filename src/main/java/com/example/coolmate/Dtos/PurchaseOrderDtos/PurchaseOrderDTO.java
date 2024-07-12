package com.example.coolmate.Dtos.PurchaseOrderDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderDTO {


    @JsonProperty("user_id")
    private int userId;

    private int roleId;

    @JsonProperty("supplier_id")
    private int supplierId;

    private String status;

    @JsonProperty("purchase_order_details")
    private List<PurchaseOrderDetailDTO> purchaseOrderDetails;

    @JsonProperty("version_code")
    private String versionCode;


}
