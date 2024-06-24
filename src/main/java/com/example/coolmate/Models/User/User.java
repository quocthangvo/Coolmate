package com.example.coolmate.Models.User;

import com.example.coolmate.Models.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fullname", length = 100, nullable = false)
    private String fullName;

    @Column(name = "phone_number", length = 10, nullable = false)
    private String phoneNumber;

    @Column(name = "email", length = 200)
    private String email;

    @Column(name = "password", length = 200)
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "google_account_id")
    private int googleAccountId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "address", length = 200)
    private String address;


//    @Column(name = "date_of_birth")
//    private Date dateOfBirth;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + getRole().getName().toUpperCase()));//kt xem quyền
//        authorityList.add(new SimpleGrantedAuthority("ADMIN"));
        return authorityList;
    }


    @Override
    public String getUsername() {
        //giá trị để đăng nhập
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        //giới hạn
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //khóa
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
