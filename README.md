# Hello World API (Java / Spring Boot)

Simple backend service for the take-home assignment.  
The API exposes a single endpoint that validates a name and returns a greeting **only** if the name starts with a letter in the range **A–M** (case-insensitive). All other inputs are rejected with a clear JSON error.

---

## 1. Overview

**Endpoint**

- `GET /hello-world?name=<name>`

**Rules**

1. `name` is read from the **query string**.
2. If the first character (after trimming spaces) is a letter between **A** and **M** (inclusive):
   - Respond with `200 OK`
   - Body:  
     ```json
     { "message": "Hello <FormattedName>" }
     ```
3. Otherwise (N–Z, missing, empty, only spaces, or starts with a non-letter):
   - Respond with `400 Bad Request`
   - Body:  
     ```json
     { "error": "Invalid Input" }
     ```

The goal is to show clean separation between controller, service (business logic), and models, plus basic tests.

---

## 2. Tech Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3
- **Build Tool**: Maven
- **Testing**: JUnit 5 + Spring Boot Test + MockMvc

---

## 3. Getting Started

### Prerequisites

- JDK **17+**
- Maven **3+**
- Git (optional, for cloning the repo)

### Clone the project

```bash
git clone <your-repo-url>.git
cd hello-world-api
```

### Build the project

```bash
mvn clean package
```

If the build finishes with `BUILD SUCCESS`, all dependencies and tests are fine.

---

## 4. Running the Application

You can run it in two common ways.

### Option A – Using Maven

```bash
mvn spring-boot:run
```

### Option B – Using the JAR

After `mvn clean package`, run:

```bash
java -jar target/hello-world-api-0.0.1-SNAPSHOT.jar
```

By default, the app starts on:

- **Base URL**: `http://localhost:8080`

---

## 5. API Details

### 5.1 Endpoint

`GET /hello-world?name=<name>`

#### Query Parameters

| Name | Type   | Required | Description                          |
|------|--------|----------|--------------------------------------|
| name | string | yes      | Name to validate and greet          |

> Note: the parameter is **required from a business perspective**; if it is missing/empty/invalid we return a 400 with an error JSON.

---

### 5.2 Success Response

**Condition**

- First letter of `name` (after trimming) is in **A–M** (case-insensitive).

**Example Request**

```http
GET /hello-world?name=alice
```

**Example Response**

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "message": "Hello Alice"
}
```

- Input: `"alice"`
- Output name is formatted to `"Alice"` (first character upper-case, rest lower-case).

---

### 5.3 Error Response

**Condition**

Any of the following:

- `name` is missing
- `name` is an empty string
- `name` is only spaces
- First character is not a letter (e.g. `1alice`, `_bob`)
- First character is a letter in **N–Z** (e.g. `zed`, `Noah`, `Yasmin`)

**Example Request**

```http
GET /hello-world?name=zed
```

**Example Response**

```http
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "error": "Invalid Input"
}
```

Same error body for all invalid cases to keep the behavior simple and predictable.

---

## 6. Validation & Formatting Rules

**Validation (`GreetingService.isFirstLetterInFirstHalf`)**

1. Trim the input.
2. If null or empty → invalid.
3. Look at the **first character**:
   - If it’s **not a letter** → invalid.
   - Convert to upper-case.
   - If between `'A'` and `'M'` → valid.
   - Otherwise → invalid.

**Formatting (`GreetingService.formatNameForMessage`)**

- Trim spaces.
- If length is 1 → return that character upper-cased.
- Otherwise:
  - First character: upper-case.
  - Remaining characters: lower-case.

Examples:

| Raw input         | Valid? | Formatted name | Output message          |
|-------------------|--------|----------------|-------------------------|
| `"alice"`         | ✅     | `Alice`        | `"Hello Alice"`         |
| `"  ALICE  "`     | ✅     | `Alice`        | `"Hello Alice"`         |
| `"bob"`           | ✅     | `Bob`          | `"Hello Bob"`           |
| `"zed"`           | ❌     | —              | `"Invalid Input"`       |
| `"1alice"`        | ❌     | —              | `"Invalid Input"`       |
| `""`              | ❌     | —              | `"Invalid Input"`       |
| `"   "`           | ❌     | —              | `"Invalid Input"`       |

---

## 7. Project Structure

```text
hello-world-api/
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   └── com.example.helloworld
    │   │       ├── HelloWorldApplication.java
    │   │       ├── controller
    │   │       │   └── HelloWorldController.java
    │   │       ├── service
    │   │       │   └── GreetingService.java
    │   │       └── model
    │   │           ├── HelloResponse.java
    │   │           └── ErrorResponse.java
    │   └── resources
    │       └── (empty – defaults used)
    └── test
        └── java
            └── com.example.helloworld
                └── HelloWorldControllerTest.java
```

- **`controller`** – HTTP endpoint(s) and request/response handling.
- **`service`** – business rules and validation logic (unit-testable).
- **`model`** – simple DTO classes representing JSON payloads.
- **`test`** – integration-style tests using Spring Boot Test and MockMvc.

---

## 8. Testing

### 8.1 Run All Tests

```bash
mvn test
```

Or in IntelliJ:

- Right-click `src/test/java` → **Run 'Tests in …'**

### 8.2 What the tests cover

`HelloWorldControllerTest` verifies:

1. **Valid A–M name → 200 OK**

   ```java
   GET /hello-world?name=alice
   -> status 200
   -> body.message = "Hello Alice"
   ```

2. **N–Z name → 400 Bad Request**

   ```java
   GET /hello-world?name=zed
   -> status 400
   -> body.error = "Invalid Input"
   ```

3. **Missing name → 400 Bad Request**

   ```java
   GET /hello-world
   -> status 400
   -> body.error = "Invalid Input"
   ```

These tests act as an executable specification of the required behavior.

---

## 9. Design Decisions

- **Spring Boot**:  
  Chosen to match standard Java backend stacks and to keep bootstrapping minimal (embedded server, JSON, etc. out of the box).

- **Controller–Service–Model separation**:  
  - Controller: only HTTP concerns and mapping.
  - Service: pure business logic, reusable and easier to test.
  - Model: simple POJOs representing JSON payloads.

- **Consistent error contract**:  
  All invalid cases return the same `{ "error": "Invalid Input" }` body so clients don’t need to handle many formats.

- **Input normalization**:  
  Names are trimmed and case-normalized, so `"  ALICE  "` and `"alice"` produce the same result.

---

## 10. Edge Cases & Assumptions

- Only **English alphabet A–Z** is considered.
- Non-letter first characters (digits, symbols, etc.) are treated as **invalid**.
- The service is stateless – no persistence, sessions, or authentication.
- `resources` folder is intentionally left empty; Spring Boot defaults are sufficient for this task.
- Only a single endpoint is required by the assignment; any additional endpoints (health, docs, etc.) are out of scope.

---

## 11. Possible Future Improvements

If this were to evolve beyond the assignment, potential next steps could include:

- Adding a `/health` endpoint for monitoring.
- Adding request logging and basic metrics.
- Exposing a small OpenAPI/Swagger specification.
- Extending validation rules (e.g., max length, allowed characters).
- Internationalization/localization for the greeting message.

---

## 12. How to Demo (Suggested Flow)

1. Start the app: `mvn spring-boot:run`.
2. Show success:
   - `GET /hello-world?name=alice` → `{"message":"Hello Alice"}`
3. Show failure:
   - `GET /hello-world?name=zed` → `{"error":"Invalid Input"}`
4. Show missing param:
   - `GET /hello-world` → `{"error":"Invalid Input"}`
5. Run tests:
   - `mvn test` (or run `HelloWorldControllerTest` in the IDE).

This demonstrates the requirements, structure, and test coverage clearly and quickly.
