package com.example.coolmate.Repositories;

import com.example.coolmate.Models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Optional<Supplier> findByName(String name);

    Optional<Supplier> findByPhoneNumber(String phoneNumber);

    Optional<Supplier> findByAddress(String address);


}
