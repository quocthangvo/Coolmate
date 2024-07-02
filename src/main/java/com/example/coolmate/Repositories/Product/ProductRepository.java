package com.example.coolmate.Repositories.Product;

import com.example.coolmate.Models.Category;
import com.example.coolmate.Models.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);

    Page<Product> findAll(Pageable pageable); //phân trang

    List<Product> findByNameContaining(String name);

    Product findByName(String name);

<<<<<<< HEAD
=======
    List<Product> findByCategoryId(Category category);
>>>>>>> 4b960ba16cc7910a535a6bf521d064262714b715

}
