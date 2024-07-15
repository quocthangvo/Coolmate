package com.example.coolmate.Repositories;

import com.example.coolmate.Models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    //tìm kiếm tên để tạo
    Optional<Supplier> findByName(String name);

    Optional<Supplier> findByPhoneNumber(String phoneNumber);

    Optional<Supplier> findByAddress(String address);

    //kt lỗi update khi trùng
    boolean existsByName(String name);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByAddress(String address);

    List<Supplier> findByNameContaining(String name);
}
