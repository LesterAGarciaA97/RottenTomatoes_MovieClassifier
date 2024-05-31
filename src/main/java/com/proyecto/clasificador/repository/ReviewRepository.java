package com.proyecto.clasificador.repository;

import com.proyecto.clasificador.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Buscar reviews por el ID del usuario
    List<Review> findByUser_Id(Long userId);
}