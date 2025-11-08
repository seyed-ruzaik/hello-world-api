package com.example.helloworld.model;

/**
 * Data Transfer Object (DTO) representing a successful response body.
 *
 * Example JSON:
 * {
 *   "message": "Hello Alice"
 * }
 */
public class HelloResponse {

    // JSON field holding the greeting message
    private String message;

    /**
     * Default constructor required by Jackson (JSON serializer/deserializer).
     */
    public HelloResponse() {
    }

    /**
     * Convenience constructor to quickly create a response.
     *
     * @param message the greeting message to send to the client
     */
    public HelloResponse(String message) {
        this.message = message;
    }

    /**
     * Getter used by Jackson when serializing to JSON.
     *
     * @return the greeting message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter used by Jackson when deserializing from JSON (not used directly here).
     *
     * @param message the greeting message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
