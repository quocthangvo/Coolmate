package com.example.coolmate.Controllers;

import com.example.coolmate.Dtos.UserDTO;
import com.example.coolmate.Dtos.UserLoginDTO;
import com.example.coolmate.Models.User;
import com.example.coolmate.Responses.UserResponse;
import com.example.coolmate.Services.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result) {
        try {
            //kt lỗi
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors() //lấy ds lỗi
                        .stream()
                        .map(FieldError::getDefaultMessage)//xuất lỗi từ FieldError
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO) {
        //kt thông tin đăng nhập và tạo token
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("")
    public ResponseEntity<List<User>> getAllCategories(
            // tham số bắt buộc
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
//        return ResponseEntity.ok(String.format("getAllCategories: page=%d, limit=%d", page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") int userId) {
        try {
            User existingUser = userService.getUserById(userId);
            return ResponseEntity.ok(UserResponse.fromUser(existingUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable int id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok(String.format("user with id %d deleted", id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> lockUser(@Valid @PathVariable int id) {
        //xóa mềm => cập nhật trường active = false
        //không xóa mất đi user, mà chỉ xóa để active trở về 0
        try{
            userService.lockUserById(id);
            return ResponseEntity.ok("Vô hiệu hóa tài khoản id "+id+" thành công");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> unlockUser(@Valid @PathVariable int id) {
        try {
            userService.unlockUserById(id);
            return ResponseEntity.ok("Kích hoạt tài khoản id " + id + " thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
