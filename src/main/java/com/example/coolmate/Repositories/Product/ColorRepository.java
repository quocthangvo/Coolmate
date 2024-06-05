package com.example.coolmate.Repositories.Product;

import com.example.coolmate.Models.Product.Color;
import com.example.coolmate.Models.Product.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color,Integer> {
    Optional<Color> findByName(String name);
}
