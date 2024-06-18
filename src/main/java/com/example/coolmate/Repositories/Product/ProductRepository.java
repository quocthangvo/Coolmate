package com.example.coolmate.Repositories.Product;

import com.example.coolmate.Models.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);

    Page<Product> findAll(Pageable pageable); //phân trang

    List<Product> findByNameContaining(String name);

    Optional<Product> findByName(String name);

}
