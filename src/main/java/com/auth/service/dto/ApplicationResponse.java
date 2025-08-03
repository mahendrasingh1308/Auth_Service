package com.auth.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic structure for sending API responses in a standardized format.
 *
 * @param <T> The type of the actual response data (payload).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse<T> {

    /**
     * HTTP status code representing the result of the request (e.g., 200, 400, 500).
     */
    private int statusCode;

    /**
     * A brief message describing the outcome of the request (e.g., "Success", "Invalid credentials").
     */
    private String message;

    /**
     * The actual response data returned to the client. Can be any object type.
     */
    private T payload;
}
