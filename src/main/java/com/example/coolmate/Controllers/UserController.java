package com.example.coolmate.Controllers;

import com.example.coolmate.Dtos.UserDtos.ChangePasswordDTO;
import com.example.coolmate.Dtos.UserDtos.UserDTO;
import com.example.coolmate.Dtos.UserDtos.UserLoginDTO;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.User.User;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Responses.UserResponse;
import com.example.coolmate.Services.Impl.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
            User createUser = userService.createUser(userDTO);
            return ApiResponseUtil.successResponse("User created successfully", createUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO) {
        //kt thông tin đăng nhập và tạo token
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
            return ApiResponseUtil.successResponse("Login successful", token);
//            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassWord(@RequestBody ChangePasswordDTO changePasswordDTO
                                           ) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();
        try{
            boolean isChanged = userService.changePassword(phoneNumber,changePasswordDTO);
            if (isChanged) {
                return ApiResponseUtil.successResponse("Thay đổi mật khẩu thành công", null);
            } else {
                return ResponseEntity.badRequest().body(new ErrorMessage("Không thể thay đổi mật khẩu"));
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ErrorMessage("Đã xảy ra lỗi khi thay đổi mật khẩu : "
                    +e.getMessage()));
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
//        return ResponseEntity.ok(String.format("getAllUsers: page=%d, limit=%d", page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") int userId) {
        try {
            User existingUser = userService.getUserById(userId);
            return ResponseEntity.ok(UserResponse.fromUser(existingUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable int id) {
        try {
            userService.deleteUserById(id);
            String message = "Xóa nhà cung cấp có ID " + id + " thành công";
            return ResponseEntity.ok(new SuccessfulMessage(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> lockUser(@Valid @PathVariable int id, UserDTO userDTO) {
        //xóa mềm => cập nhật trường active = false
        //không xóa mất đi user, mà chỉ xóa để active trở về 0
        try {
            userService.lockUserById(id, userDTO);
            return ApiResponseUtil.successResponse("Vô hiệu hóa thành công", id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> unlockUser(@Valid @PathVariable int id, UserDTO userDTO) {
        try {
            userService.unlockUserById(id, userDTO);
            return ApiResponseUtil.successResponse("Mở khóa thành công", id);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

}
