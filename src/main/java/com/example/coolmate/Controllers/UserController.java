package com.example.coolmate.Controllers;

import com.example.coolmate.Dtos.UserDtos.ChangePasswordDTO;
import com.example.coolmate.Dtos.UserDtos.UserDTO;
import com.example.coolmate.Dtos.UserDtos.UserLoginDTO;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.User.User;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Responses.User.UserListResponse;
import com.example.coolmate.Responses.User.UserResponse;
import com.example.coolmate.Services.Impl.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")

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
        try {
            boolean isChanged = userService.changePassword(phoneNumber, changePasswordDTO);
            if (isChanged) {
                return ApiResponseUtil.successResponse("Thay đổi mật khẩu thành công", null);
            } else {
                return ResponseEntity.badRequest().body(new ErrorMessage("Không thể thay đổi mật khẩu"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage("Đã xảy ra lỗi khi thay đổi mật khẩu : "
                    + e.getMessage()));
        }
    }

    @GetMapping("")
    public ResponseEntity<UserListResponse> getAllUsers(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<UserResponse> userPage = userService.getAllUsers(pageRequest);
        int totalPages = userPage.getTotalPages();
        List<UserResponse> users = userPage.getContent();

        return ResponseEntity.ok(UserListResponse.builder()
                .users(users).totalPage(totalPages).build());
    }
//    @GetMapping("")
//    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(
//            @RequestParam("page") int page,
//            @RequestParam("limit") int limit
//    ) {
//        List<User> users = userService.getAllUsers(page, limit);
//        return ApiResponseUtil.successResponse("Successfully", users);
//
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        try {
            UserResponse user = userService.getUserById(id);
            return ApiResponseUtil.successResponse("Successfully", user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable int id) {
        try {
            userService.deleteUserById(id);
            String message = "Xóa user có ID " + id + " thành công";
            return ResponseEntity.ok(new SuccessfulMessage(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> lockUser(@Valid @PathVariable int id, UserResponse userResponse) {
        //xóa mềm => cập nhật trường active = false
        //không xóa mất đi user, mà chỉ xóa để active trở về 0
        try {
            userService.lockUserById(id, userResponse);
            return ApiResponseUtil.successResponse("Vô hiệu hóa thành công", id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> unlockUser(@Valid @PathVariable int id, UserResponse userResponse) {
        try {
            userService.unlockUserById(id, userResponse);
            return ApiResponseUtil.successResponse("Mở khóa thành công", id);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUserByPhoneNumber(@RequestParam("phone_number") String phoneNumber) {
        try {
            Optional<User> users = userService.findByPhoneNumber(phoneNumber);
            if (users.isEmpty()) {
                ErrorMessage errorMessage = new ErrorMessage("Không tìm thấy số điện thoại có tên : " + phoneNumber);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
            return ApiResponseUtil.successResponse("successful", users);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage("Đã xảy ra lỗi khi tìm kiếm tên sản phẩm : "
                    + e.getMessage()));
        }
    }

    @GetMapping("/search/full_name")
    public ResponseEntity<?> searchUsersByFullName(@RequestParam("full_name") String fullName) {
        try {
            List<UserResponse> users = userService.searchUsersByFullName(fullName);
            if (users.isEmpty()) {
                ErrorMessage errorMessage = new ErrorMessage("Không tìm thấy số điện thoại có tên : " + fullName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
            return ApiResponseUtil.successResponse("successful", users);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage("Đã xảy ra lỗi khi tìm kiếm tên sản phẩm : "
                    + e.getMessage()));
        }
    }

}
