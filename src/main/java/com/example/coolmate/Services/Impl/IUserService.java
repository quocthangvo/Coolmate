package com.example.coolmate.Services.Impl;

import com.example.coolmate.Dtos.UserDtos.UserDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.User.User;

import java.util.List;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password) throws Exception;

    List<User> getAllUsers();

    User getUserById(int id) throws Exception;

    void deleteUserById(int id) throws DataNotFoundException;

    void lockUserById(int id) throws DataNotFoundException;

    void unlockUserById(int id) throws DataNotFoundException;
}
