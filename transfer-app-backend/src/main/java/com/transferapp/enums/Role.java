package com.transferapp.enums;

/**
 * Defines the roles available in the system for role-based access control.
 * USER  → standard authenticated user, can transfer funds
 * ADMIN → elevated privileges, can view all accounts and transactions
 */
public enum Role {
    USER,
    ADMIN
}
