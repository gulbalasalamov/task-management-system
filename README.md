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

## Technologies
- **Java 17+**
- **Spring Boot**
- **PostgreSQL**
- **Spring Security - JWT **
- **Docker**

## Use-Case Diagram

```plaintext
                            +--------------------+                    
                            |       Admin        |
                            +--------------------+
                                     |
                                     |
                    +----------------+----------------+----------------+
                    |                |                |                |
                    |                |                |                |                   
             +------------+    +-------------+  +-----------+  +-------------+                           
             | View Tasks |    | Create Task |  | Edit Task |  | Delete Task |
             +------------+    +-------------+  +-----------+  +-------------+
                    |
                    |
       +--------------------+------------------+---------------+
       |                    |                  |               |
       |                    |                  |               | 
+---------------+  +---------------+  +---------------+ +---------------+
| Change Status |  |Change Priority|  | Assign Task   | | Leave Comment |
+---------------+  +---------------+  +---------------+ +---------------+

```
```plaintext

                             +---------------------+
                             |       User          |
                             +---------------------+
                                       |         
                                       |   
                             +---------------------+  
                             | View Assigned Tasks |  
                             +---------------------+  
                                       |
                                       |
                       +-----------------------------------+
                       |                                   |
                       |                                   |
               +---------------+                +----------------+
               | Change Status |                | Leave Comments |
               +---------------+                +----------------+
```

## Database Design

```plaintext

┌──────────────────┐                 ┌──────────────────────┐                 ┌──────────────────┐
│      Roles       │                 │        Users         │                 │      Tasks       │
├──────────────────┤      1:N        ├──────────────────────┤                 ├──────────────────┤
│ role_id (PK)     ├─────────────────┤ user_id (PK)         │       1:N       │ task_id (PK)     │
│ role_name        │                 │ username             ├─────────────────┤ title            │
└──────────────────┘                 │ email                │       1:N       │ description      │
                                     │ password             ├─────────────────┤ status           │
                                     │ role_id (FK)         │                 │ priority         │
                                     │ created_at           │                 │ author_id (FK)   │
                                     │ updated_at           │                 │ assignee_id (FK) │
                                     └───────┬──────────────┘                 │ created_at       │
                                             │                                │ updated_at       │
                                             │                                └────────┬─────────┘
                                             │                                         │
                                             │                                         │
                                             │                                         │
                                             │                                         │
                                             │                                         │
┌──────────────────┐                         │                                         │
│     Comments     │                         │                                         │
├──────────────────┤           1:N           │                                         │
│ comment_id (PK)  │                         │                                         │
│ content          ├─────────────────────────┘                                         │
│ task_id (FK)     │←──────────────────────────────────────────────────────────────────┘
│ user_id (FK)     │                                                    1:N
│ created_at       │
│ updated_at       │
└──────────────────┘
```
### Relationships:

- **Users and Roles:**

   - Each user can have one role (role_id).
   - Each role can be assigned to many users.


- **Users and Tasks:** 
  - author_id: Only admin users can create tasks. (1:N)
  - assignee_id: Each user can be assigned to many tasks. (1:N)


- **Tasks and Comments:**
  - task_id: Each task can have many comments. (1:N)


- **Users and Comments:**
  - user_id: Each user can leave many comments. (1:N)