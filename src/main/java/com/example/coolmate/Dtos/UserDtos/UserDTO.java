package com.example.coolmate.Dtos.UserDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @JsonProperty("is_active")
    private boolean active;


    @JsonProperty("google_account_id")
    private int googleAccountId;

    //    @NotNull(message = "Role ID is required")
    @JsonProperty("role_id")
    private int roleId;

//    @JsonProperty("email")
//    @NotBlank(message = "email is required")
//    private String email;

    private String address;


    //    @JsonProperty("date_of_birth")
//    private Date dateOfBirth;
    private String profilePicture;


}
