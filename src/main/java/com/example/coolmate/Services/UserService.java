package com.example.coolmate.Services;

import com.example.coolmate.Components.JwtTokenUtil;
import com.example.coolmate.Dtos.UserDtos.ChangePasswordDTO;
import com.example.coolmate.Dtos.UserDtos.UserDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.PermissionDenyException;
import com.example.coolmate.Models.User.Role;
import com.example.coolmate.Models.User.User;
import com.example.coolmate.Repositories.RoleRepository;
import com.example.coolmate.Repositories.UserRepository;
import com.example.coolmate.Responses.User.UserResponse;
import com.example.coolmate.Services.Impl.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        // Đăng ký user
        String phoneNumber = userDTO.getPhoneNumber();

        // Kiểm tra số điện thoại đã tồn tại chưa
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataNotFoundException("Phone number " + phoneNumber + " đã tồn tại");
        }

        // Kiểm tra độ dài số điện thoại
        if (phoneNumber.length() != 10) {
            throw new DataNotFoundException("Số điện thoại phải có 10 ký tự");
        }

        // Tìm quyền từ cơ sở dữ liệu
        Role role = null;
        Integer roleId = userDTO.getRoleId();
        if (roleId != null) {
            role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new DataNotFoundException("Role not found"));
        } else {
            // Nếu không có roleId được cung cấp, sử dụng quyền mặc định có id là 1
            role = roleRepository.findById(1)
                    .orElseThrow(() -> new DataNotFoundException("Default Role not found"));
        }

        // Kiểm tra nếu role là admin
        if (role.getName().equals(Role.ADMIN)) {
            throw new PermissionDenyException("Bạn không thể đăng ký tài khoản admin");
        }

        // Chuyển đổi từ userDTO sang user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .googleAccountId(userDTO.getGoogleAccountId())
                .active(true)
                .build();

        newUser.setRole(role);

        // Kiểm tra nếu có accountId, không cần password
        if (userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password); // Mã hóa mật khẩu
            newUser.setPassword(encodedPassword);
        }

        return userRepository.save(newUser); // Lưu user mới
    }


    @Override
    public String login(String phoneNumber, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("Tài khoản hoặc mật khẩu không tồn tại");
        }
        User existingUser = optionalUser.get();
        // Kiểm tra trạng thái hoạt động của người dùng
        if (!existingUser.isActive()) {
            throw new DisabledException("Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ với quản trị viên.");
        }
        //kiểm tra password nếu không dùng gg account
        if (existingUser.getGoogleAccountId() == 0) {
            if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("Mật khẩu không chính xác");
            }
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password,
                existingUser.getAuthorities());
        //authentication with java spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public Page<UserResponse> getAllUsers(PageRequest pageRequest) {
        return userRepository
                .findAll(pageRequest)
                .map(UserResponse::fromUser);
    }

//    public List<User> getAllUsers(int page, int limit) {
//        return userRepository.findAll();
//    }

    @Override
    public UserResponse getUserById(int userId) throws Exception {
        return userRepository.findById(userId)
                .map(UserResponse::fromUser)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy user Id = "
                        + userId));
    }

    @Override
    public void deleteUserById(int id) throws DataNotFoundException {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy người dùng với ID " + id);
        }

        User user = userOptional.get();

        // Kiểm tra nếu người dùng có quyền "admin"
        if (user.getRole().getId() == 2) {
            throw new DataNotFoundException("Không thể xóa người dùng với quyền admin");
        }

        userRepository.deleteById(id);
    }


    @Override
    public void lockUserById(int id, UserResponse userResponse) throws DataNotFoundException {
        User user = userRepository.findById(id).orElseThrow(()
                -> new DataNotFoundException("Không tìm thấy người dùng với ID " + id));

        if (user.getRole().getId() == 2) {
            throw new DataNotFoundException("Không thể khóa tài khoản với quyền là admin.");
        }
        // Kiểm tra active trước khi vô hiệu hóa
        if (!user.isActive()) {
            throw new DataNotFoundException("Người dùng " + userResponse.getFullName() + " đã được vô hiệu hóa.");
        }
        // thực hiện vô hiệu hóa và lưu
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void unlockUserById(int id, UserResponse userResponse) throws DataNotFoundException {
        User user = userRepository.findById(id).orElseThrow(()
                -> new DataNotFoundException("Không tìm thấy người dùng với ID " + id));
        //kt trc khi kich hoạt lại
        if (user.isActive()) {
            throw new DataNotFoundException("Người dùng " + userResponse.getFullName() + " đang được kích hoạt.");
        }
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public boolean changePassword(String phoneNumber, ChangePasswordDTO changePasswordDTO) throws Exception {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng"));
        if (passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public List<UserResponse> searchUsersByFullName(String fullName) {
        return userRepository.findByFullNameContainingIgnoreCase(fullName)
                .stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }

}





