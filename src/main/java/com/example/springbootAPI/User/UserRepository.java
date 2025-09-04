package com.example.springbootAPI.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Verifica si existe un usuario por email (case-insensitive)
    boolean existsByEmailIgnoreCase(String email);


    // Obtiene un usuario por email (case-insensitive)
    Optional<UserEntity> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

}