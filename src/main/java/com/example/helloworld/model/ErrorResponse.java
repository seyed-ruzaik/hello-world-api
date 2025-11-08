package com.example.helloworld.model;

/**
 * Data Transfer Object (DTO) representing an error response body.
 *
 * Example JSON:
 * {
 *   "error": "Invalid Input"
 * }
 */
public class ErrorResponse {

    // JSON field holding the error message
    private String error;

    /**
     * Default constructor required by Jackson.
     */
    public ErrorResponse() {
    }

    /**
     * Convenience constructor to quickly create an error response.
     *
     * @param error the error message to send to the client
     */
    public ErrorResponse(String error) {
        this.error = error;
    }

    /**
     * Getter used by Jackson when serializing to JSON.
     *
     * @return the error message
     */
    public String getError() {
        return error;
    }

    /**
     * Setter used by Jackson when deserializing from JSON (not used directly here).
     *
     * @param error the error message
     */
    public void setError(String error) {
        this.error = error;
    }
}
