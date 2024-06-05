package com.example.coolmate.Dtos.UserDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {
    @JsonProperty("oldpassword")
    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @JsonProperty("newpassword")
    @NotBlank(message = "New Password is required")
    private String newPassword;
}
