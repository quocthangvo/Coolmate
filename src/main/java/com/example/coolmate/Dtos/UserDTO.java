package com.example.coolmate.Dtos;

import com.example.coolmate.Models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Date;

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

//    @JsonProperty("email")
//    @NotBlank(message = "email is required")
//    private String email;

    private String address;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("is_active")
    private boolean active;

    @JsonProperty("google_account_id")
    private int googleAccountId;

    @NotNull(message = "Role ID is required")
    @JsonProperty("role_id")
    private int roleId;

//    public UserDTO(User user) {
//        this.fullName = user.getFullname();
//        this.phoneNumber = user.getPhoneNumber();
//        this.address = user.getAddress();
//        this.dateOfBirth = user.getDateOfBirth();
//        this.active = user.isActive();
//        this.googleAccountId = user.getGoogleAccountId();
//        this.roleId = user.getRole() != null ? user.getRole().getId() : null; // Adjust as needed
//    }
}
