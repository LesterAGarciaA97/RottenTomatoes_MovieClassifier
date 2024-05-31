package com.proyecto.clasificador.repository;

import com.proyecto.clasificador.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Métodos adicionales si son necesarios
}