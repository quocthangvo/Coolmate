package com.example.coolmate.Repositories.Product;

import com.example.coolmate.Models.Category;
import com.example.coolmate.Models.Product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand,Integer> {
    Optional<Brand> findByName(String name);
}
