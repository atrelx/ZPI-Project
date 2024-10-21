package com.zpi.amoz.repository;

import com.zpi.amoz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "SELECT * FROM User WHERE Email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);
}
