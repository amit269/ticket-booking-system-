package com.ticketbooking.userservice.constant;


import lombok.Getter;

@Getter
public enum ErrorCodeEnum {

    // Generic
    GENERIC_ERROR("10000", "Unable to process request"),
    UNABLE_TO_CONNECT_SERVICE("10002", "Unable to connect to service"),

    // Register
    EMAIL_ALREADY_EXISTS("10003", "Email already exists"),
    NAME_REQUIRED("10004", "Name is required"),
    INVALID_EMAIL_FORMAT("10005", "Invalid email format"),
    PASSWORD_TOO_SHORT("10006", "Password must be at least 6 characters"),
    NAME_TOO_LONG("10007", "Name must be less than 50 characters"),

    // Login
    EMAIL_NOT_FOUND("10001", "No email found, please register first"),
    WRONG_PASSWORD("10008", "Invalid password"),
    ACCOUNT_DISABLED("10009", "Account is disabled"),


    // Token
    TOKEN_MISSING("10010", "Authorization token is missing"),
    TOKEN_EXPIRED("10011", "Token has expired, please login again"),
    TOKEN_INVALID("10012", "Invalid token"),

    // Profile
    USER_NOT_FOUND("10013", "User not found"),

    // Validation
    INVALID_INPUT("10014" , "Validation Failed");

    private final String code;
    private final String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}