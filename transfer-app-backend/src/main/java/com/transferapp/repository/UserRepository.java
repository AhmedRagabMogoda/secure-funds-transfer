package com.transferapp.repository;

import com.transferapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository for User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     * Used by Spring Security's UserDetailsService during authentication.
     *
     * @param username the username to search for
     * @return an Optional containing the User if found, empty otherwise
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks whether a username already exists in the database.
     * Useful for registration validation.
     *
     * @param username the username to check
     * @return true if the username exists
     */
    boolean existsByUsername(String username);

    /**
     * Update user's last login time
     *
     * @param id the user identifier
     * @param lastLoginAt the last login timestamp
     */
    @Modifying
    @Query("Update User U Set U.lastLoginAt = :lastLoginAt Where U.id = :id ")
    void updateLastLoginAt(@Param("id") Long id, @Param("lastLoginAt") LocalDateTime lastLoginAt);
}
