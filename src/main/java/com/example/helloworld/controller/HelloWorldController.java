package com.example.helloworld.controller;

import com.example.helloworld.model.ErrorResponse;
import com.example.helloworld.model.HelloResponse;
import com.example.helloworld.service.GreetingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that exposes the HTTP API:
 *
 * GET /hello-world?name=alice
 *
 * Behavior:
 * - If name is missing or empty  -> 400 Bad Request, { "error": "Invalid Input" }
 * - If first letter is N–Z      -> 400 Bad Request, { "error": "Invalid Input" }
 * - If first letter is A–M      -> 200 OK, { "message": "Hello Alice" }
 */
@RestController
public class HelloWorldController {

    // Service holding the validation + formatting logic
    private final GreetingService greetingService;

    /**
     * Constructor-based dependency injection.
     *
     * @param greetingService the service containing business rules
     */
    public HelloWorldController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    /**
     * Handles GET requests to /hello-world.
     *
     * Example:
     *   /hello-world?name=alice
     *
     * @param name the "name" query parameter (can be null if missing)
     * @return HTTP 200 with a greeting JSON body, or HTTP 400 with an error JSON body
     */
    @GetMapping("/hello-world")
    public ResponseEntity<?> sayHello(@RequestParam(name = "name", required = false) String name) {

        // 1. Validate that "name" is present and not just spaces.
        if (name == null || name.trim().isEmpty()) {
            // Missing or empty -> always "Invalid Input" with 400
            return badRequest();
        }

        // 2. Check if the first letter is in A–M (inclusive).
        boolean firstHalf = greetingService.isFirstLetterInFirstHalf(name);

        if (!firstHalf) {
            // First letter is either N–Z or invalid (non-letter) -> 400
            return badRequest();
        }

        // 3. Format the name nicely for the message (e.g., "alice" -> "Alice").
        String formattedName = greetingService.formatNameForMessage(name);

        // 4. Build the successful response: { "message": "Hello Alice" }
        HelloResponse responseBody = new HelloResponse("Hello " + formattedName);

        // Return HTTP 200 OK with the JSON body
        return ResponseEntity.ok(responseBody);
    }

    /**
     * Helper method to consistently return the expected error response:
     *   HTTP 400 Bad Request with body:
     *   { "error": "Invalid Input" }
     *
     * @return ResponseEntity with error payload
     */
    private ResponseEntity<ErrorResponse> badRequest() {
        ErrorResponse errorResponse = new ErrorResponse("Invalid Input");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
