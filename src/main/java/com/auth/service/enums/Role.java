package com.auth.service.enums;

/**
 * Enum representing the different roles a user can have within the system.
 * These roles determine access levels and permissions for various operations.
 */
public enum Role {

    /** Standard user with limited access */
    USER,

    /** User with permissions to create or manage content */
    CREATOR,

    /** Administrator with full access and management privileges */
    ADMIN
}
