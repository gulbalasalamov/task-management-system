# Task Management System Development

## Functional Requirements

1. **Task Management**
    - Creating tasks
    - Editing tasks
    - Deleting tasks
    - Viewing tasks
    - Each task must include the following information:
        - Title
        - Description
        - Status (e.g., "pending", "in progress", "completed")
        - Priority (e.g., "high", "medium", "low")
        - Comments
        - Task author (Reporter) and task executor (Assignee)

2. **Authentication and Authorization**
    - User authentication and authorization should be done with email and password.
    - API access should be authenticated using JWT tokens.

3. **Role System**
    - Admin and user roles should be created.
    - **Admin permissions:**
        - Manage all tasks (create, edit, view, delete)
        - Change task status and priority
        - Assign tasks to executors
        - Leave comments
    - **User permissions:**
        - Manage only tasks assigned to them (change status, leave comments)

4. **Filtering and Pagination of Tasks**
    - Tasks should be filtered by author (Reporter) or executor (Assignee).
    - Tasks should be paginated.

5. **Error Handling and Data Validation**
    - System should handle errors properly and return understandable messages to users.
    - Incoming data should be validated.

6. **Documentation**
    - API should be documented using OpenAPI and Swagger.
    - Swagger UI should be configured.
    - A README file should be created with local setup instructions.

7. **Development Environment**
    - Development environment should be created using docker-compose.
    - Basic unit tests should be written for core functionalities.

## Technologies to be Used
- **Java 17+**
- **Spring, Spring Boot**
- **PostgreSQL or MySQL**
- **Spring Security**
- **Additional tools (if needed): For example, cache**
