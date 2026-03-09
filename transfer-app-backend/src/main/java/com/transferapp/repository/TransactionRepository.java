package com.transferapp.repository;

import com.transferapp.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository for Transaction entity.
 * Provides both paginated and non-paginated queries for fetching
 * transaction history from sender, receiver, and combined perspectives.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds all transactions involving a given account (sent or received) — unpaginated.
     * Used for profile statistics calculations.
     */
    @Query("""
        SELECT t FROM Transaction t
        WHERE t.senderAccount.id = :accountId
           OR t.receiverAccount.id = :accountId
        ORDER BY t.createdAt DESC
    """)
    List<Transaction> findAllByAccountId(@Param("accountId") Long accountId);

    /**
     * Paginated: all transactions (sent + received) for a given account.
     * Used by the main history page.
     */
    @Query("""
        SELECT t FROM Transaction t
        WHERE t.senderAccount.id = :accountId
           OR t.receiverAccount.id = :accountId
        ORDER BY t.createdAt DESC
    """)
    Page<Transaction> findAllByAccountIdPaged(@Param("accountId") Long accountId, Pageable pageable);

    /**
     * Paginated: only SENT transactions for a given account.
     */
    @Query("""
        SELECT t FROM Transaction t
        WHERE t.senderAccount.id = :accountId
        ORDER BY t.createdAt DESC
    """)
    Page<Transaction> findSentByAccountId(@Param("accountId") Long accountId, Pageable pageable);

    /**
     * Paginated: only RECEIVED transactions for a given account.
     */
    @Query("""
        SELECT t FROM Transaction t
        WHERE t.receiverAccount.id = :accountId
        ORDER BY t.createdAt DESC
    """)
    Page<Transaction> findReceivedByAccountId(@Param("accountId") Long accountId, Pageable pageable);

    /**
     * Calculates total amount sent from a given account (SUCCESS only).
     */
    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.senderAccount.id = :accountId
          AND t.status = 'SUCCESS'
    """)
    BigDecimal sumSentByAccountId(@Param("accountId") Long accountId);

    /**
     * Calculates total amount received by a given account (SUCCESS only).
     */
    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.receiverAccount.id = :accountId
          AND t.status = 'SUCCESS'
    """)
    BigDecimal sumReceivedByAccountId(@Param("accountId") Long accountId);
}
