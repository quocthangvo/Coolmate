package com.example.coolmate.Repositories.Product;

import com.example.coolmate.Models.Product.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SizeRepository extends JpaRepository<Size, Integer> {
    Optional<Size> findByName(String name);


}
