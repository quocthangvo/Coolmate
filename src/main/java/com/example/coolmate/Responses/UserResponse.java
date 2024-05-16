package com.example.coolmate.Responses;

import com.example.coolmate.Models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Date;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends BaseResponse {
    private String fullname;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;


//    @JsonProperty("google_account_id")
//    private int googleAccountId;

    public static UserResponse fromUser(User user) {
        UserResponse userResponse = UserResponse.builder()
                .fullname(user.getFullname())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth())
                .build();
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }

}
