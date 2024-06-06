package com.example.coolmate.Models.Product;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

@Builder
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;
}
