package com.example.coolmate.Responses.User;

import com.example.coolmate.Models.User.User;
import com.example.coolmate.Responses.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Date;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends BaseResponse {
    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("google_account_id")
    private int googleAccountId;

    @JsonProperty("is_active")
    private boolean active;

    private String status;

    private String roleName;

    public static UserResponse fromUser(User user) {
        UserResponse userResponse = UserResponse.builder()
                .fullname(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth())
                .googleAccountId(user.getGoogleAccountId())
                .active(user.isActive())
                .roleName(user.getRole().getName())
                .build();
        userResponse.setStatus(user.isActive() ? "Đang hoạt động" : "Vô hiệu hóa");
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }

}
