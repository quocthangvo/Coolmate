package com.example.coolmate.Dtos.PurchaseOrderDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderDTO {


    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("supplier_id")
    private int supplierId;

    private String status;

}
