package com.fooddelivery.foodbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Uniform error envelope returned by every exception handler.
 * <p>
 * Shape:
 * <pre>
 * {
 *   "timestamp": "2026-08-18T18:30:00",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "Restaurant not found with id : 5",
 *   "fieldErrors": { "itemName": "Item name is required" }   // only for validation errors
 * }
 * </pre>
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;

    /** Present only for validation errors — maps fieldName → errorMessage. */
    private Map<String, String> fieldErrors;
}
