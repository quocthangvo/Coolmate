package com.example.coolmate.Services;

import com.example.coolmate.Dtos.UserDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;

    String login(String phoneNumber, String password) throws Exception;

}
