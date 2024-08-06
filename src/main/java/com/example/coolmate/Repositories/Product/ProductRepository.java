package com.example.coolmate.Repositories.Product;

import com.example.coolmate.Models.Category;
import com.example.coolmate.Models.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {


    boolean existsByName(String name);

    Page<Product> findAll(Pageable pageable); //phân trang

    List<Product> findByNameContaining(String name);

    Page<Product> findByCategoryId(Category category, Pageable pageable);


}
