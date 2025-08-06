package com.auth.service.enums;

/**
 * Enum representing the various login channels supported by the authentication service.
 * Used to track how a user has authenticated.
 */
public enum LoginChannel {

    /** Login using email and password */
    EMAIL,

    /** Login via Google OAuth */
    GOOGLE,

    /** Login using phone number and OTP */
    PHONE,

    /** OTP-less login via WhatsApp */
    WHATSAPP,

    /** Login via Facebook OAuth */
    FACEBOOK,

    /** OTP-less login via SMS */
    SMS,

    /** Login via a magic link sent to email */
    EMAIL_LINK,
}
