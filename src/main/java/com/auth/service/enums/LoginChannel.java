package com.auth.service.enums;

public enum LoginChannel {
    EMAIL,          // email + password
    GOOGLE,         // google oauth
    PHONE,          // phone + otp
    WHATSAPP,       // otpless login via WhatsApp
    SMS,            // otpless login via SMS
    EMAIL_LINK,
}
