package com.example.coolmate.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDTO {
    @NotBlank(message = "Category name không để trống")
    private String name;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;
}
