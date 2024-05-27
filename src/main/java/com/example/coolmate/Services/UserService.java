package com.example.coolmate.Services;

import com.example.coolmate.Components.JwtTokenUtil;
import com.example.coolmate.Dtos.UserDtos.UserDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.PermissionDenyException;
import com.example.coolmate.Models.User.Role;
import com.example.coolmate.Models.User.User;
import com.example.coolmate.Repositories.RoleRepository;
import com.example.coolmate.Repositories.UserRepository;
import com.example.coolmate.Services.Impl.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        //đăng ký user
        String phoneNumber = userDTO.getPhoneNumber();
        //kt sdt có tồn tại chưa
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataNotFoundException("Phone number  " + phoneNumber + " đã tồn tại");
        }
        // Kiểm tra độ dài số điện thoại
        if (phoneNumber.length() != 10) {
            throw new DataNotFoundException("Số điện thoại phải có 10 ký tự");
        }
        //lấy quyền từ csdl
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if (role.getName().toUpperCase().equals(Role.ADMIN)) {//kt có phải quyền admin k
            throw new PermissionDenyException("Bạn không thể đăng ký tài khoản admin");
        }
        //chuyển đổi userDTo -> user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .googleAccountId(userDTO.getGoogleAccountId())
                .active(true)
                .build();

        newUser.setRole(role);

        //kiểm tra nếu có accountId, k cần password
        if (userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password); // Mã hóa mật khẩu
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);//lưu user mới
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
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int userId) throws Exception {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy user Id = "
                        + userId));
    }

    @Override
    public void deleteUserById(int id) throws DataNotFoundException {
        // Kiểm tra xem người dùng có tồn tại không
        if (userRepository.existsById(id)) {
            // Nếu tồn tại, thực hiện xóa người dùng
            userRepository.deleteById(id);
        } else {
            // Nếu không tồn tại, bạn có thể bắt hoặc ném một ngoại lệ, hoặc thực hiện xử lý khác theo nhu cầu của bạn
            throw new DataNotFoundException("Không tìm thấy người dùng với ID " + id);
        }
    }


    @Override
    public void lockUserById(int id, UserDTO userDTO) throws DataNotFoundException {
        User user = userRepository.findById(id).orElseThrow(()
                -> new DataNotFoundException("Không tìm thấy người dùng với ID " + id));

        // Kiểm tra active trước khi vô hiệu hóa
        if (!user.isActive()) {
            throw new DataNotFoundException("Người dùng với ID " + id + " đã được vô hiệu hóa.");
        }
        // Nếu người dùng chưa bị vô hiệu hóa, thực hiện vô hiệu hóa và lưu lại vào cơ sở dữ liệu
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void unlockUserById(int id, UserDTO userDTO) throws DataNotFoundException {
        User user = userRepository.findById(id).orElseThrow(()
                -> new DataNotFoundException("Không tìm thấy người dùng với ID " + id));
        //kt trc khi kich hoạt lại
        if (user.isActive()) {
            throw new DataNotFoundException("Người dùng với ID " + id + " đã được kích hoạt.");
        }
        user.setActive(true);
        userRepository.save(user);
    }

}



