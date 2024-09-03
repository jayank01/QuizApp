# Quiz Management Application

## Overview
The Quiz Management Application is a Spring Boot-based project designed to manage quizzes and questions. It provides RESTful APIs for creating, retrieving, updating, and deleting quizzes and questions.

## Features
- Create, retrieve, update, and delete quizzes.
- Add, retrieve, update, and delete questions within quizzes.
- Categorize quizzes and questions.
- Validate input data using Jakarta Bean Validation.

## Technologies Used
- Java
- Spring Boot
- Maven
- JUnit 5
- Mockito
- Lombok
- Jakarta Persistence API (JPA)
- Hibernate
- Jackson

## Project Structure
```
src
├── main
│   ├── java
│   │   └── com
│   │       └── quiz
│   │           └── management
│   │               ├── application
│   │               │   ├── controller
│   │               │   ├── dto
│   │               │   ├── entity
│   │               │   ├── handler
│   │               │   ├── repository
│   │               │   └── service
│   └── resources
│       └── application.properties
└── test
    └── java
        └── com
            └── quiz
                └── management
                    └── application
                        └── service
```

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6.0 or higher

### Installation
1. Clone the repository:
   ```sh
   git clone git@github.com:jayank01/QuizApp.git
   ```
2. Navigate to the project directory:
   ```sh
   cd quiz-management-application
   ```
3. Build the project using Maven:
   ```sh
   mvn clean install
   ```

### Running the Application
1. Run the Spring Boot application:
   ```sh
   mvn spring-boot:run
   ```
2. The application will start on `http://localhost:8080`.

### Running Tests
Execute the following command to run the tests:
```sh
mvn test
```

## API Endpoints

### Quiz Endpoints
- `POST /quizzes` - Create a new quiz.
- `GET /quizzes/{quizId}` - Retrieve a quiz by ID.
- `GET /quizzes` - Retrieve all quizzes.
- `PUT /quizzes` - Update an existing quiz.
- `DELETE /quizzes/{quizId}` - Delete a quiz by ID.
- `DELETE /quizzes` - Delete all quizzes.

### Question Endpoints
- `POST /questions` - Add a new question.
- `GET /questions/{questionId}` - Retrieve a question by ID.
- `GET /questions/category/{category}` - Retrieve questions by category.
- `PUT /questions` - Update an existing question.
- `DELETE /questions/{questionId}` - Delete a question by ID.
- `DELETE /questions/category/{category}` - Delete questions by category.
