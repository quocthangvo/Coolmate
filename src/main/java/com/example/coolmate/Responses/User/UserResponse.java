package com.example.coolmate.Responses.User;

import com.example.coolmate.Models.User.Role;
import com.example.coolmate.Models.User.User;
import com.example.coolmate.Responses.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends BaseResponse {
    @JsonProperty("id")
    private int id;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("password")
    private String password;

    @JsonProperty("google_account_id")
    private int googleAccountId;

    @JsonProperty("is_active")
    private boolean active;

    private String status;


    private Role role;

    public static UserResponse fromUser(User user) {
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .googleAccountId(user.getGoogleAccountId())
                .active(user.isActive())
                .role(user.getRole())
                .build();
        userResponse.setStatus(user.isActive() ? "Đang hoạt động" : "Vô hiệu hóa");
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }

}
