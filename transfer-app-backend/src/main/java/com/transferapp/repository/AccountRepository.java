package com.transferapp.repository;

import com.transferapp.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Account entity.
 * Provides lookups by account number and by the owning user's ID,
 * both of which are critical paths in the transfer processing logic.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Finds an account by its account number.
     * Used when identifying the receiver account during a transfer.
     *
     * @param accountNumber the account number to search for
     * @return an Optional containing the Account if found
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Finds the account belonging to a specific user.
     * Used to retrieve the sender's account from the authenticated user's ID.
     *
     * @param userId the ID of the owning user
     * @return an Optional containing the Account if found
     */
    Optional<Account> findByUserId(Long userId);

    /**
     * Checks whether an account number already exists.
     * Useful during account creation to ensure uniqueness.
     *
     * @param accountNumber the account number to check
     * @return true if the account number exists
     */
    boolean existsByAccountNumber(String accountNumber);
}
