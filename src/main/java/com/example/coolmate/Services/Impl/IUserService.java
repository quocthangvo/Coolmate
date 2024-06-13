package com.example.coolmate.Services.Impl;

import com.example.coolmate.Dtos.UserDtos.ChangePasswordDTO;
import com.example.coolmate.Dtos.UserDtos.UserDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.User.User;
import com.example.coolmate.Responses.User.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password) throws Exception;

    Page<UserResponse> getAllUsers(PageRequest pageRequest);

    User getUserById(int id) throws Exception;

    void deleteUserById(int id) throws DataNotFoundException;

    void lockUserById(int id, UserDTO userDTO) throws DataNotFoundException;

    void unlockUserById(int id, UserDTO userDTO) throws DataNotFoundException;

    boolean changePassword(String phoneNumber, ChangePasswordDTO changePasswordDTO) throws Exception;
}
