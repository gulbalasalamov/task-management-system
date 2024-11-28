# Task Management System - Effective Mobile Java Developer Backend Challenge

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


## Test Coverage

**Class**   : %84

**Methods** : %74

![test-coverage](https://github.com/user-attachments/assets/dfb632f1-5cd1-46fc-86c8-4634981bc7b9)


## API Documentation

To access Swagger doc, click live link below.

Swagger / OpenAPI : http://localhost:8080/swagger-ui/swagger-ui/index.html


![image](https://github.com/user-attachments/assets/04ec4e84-9afa-4c7a-a5d0-762c82590f15)

![image](https://github.com/user-attachments/assets/2edff95c-d060-4e97-8511-ac8963398f4f)

**Postman Collection** To run the collection in your postman:

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/5231798-3e05d92a-0637-4dd2-8d54-9e5ad335aab0?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D5231798-3e05d92a-0637-4dd2-8d54-9e5ad335aab0%26entityType%3Dcollection%26workspaceId%3D314f7299-ad9d-425b-a0ea-09e317913a0f)


## API Step by Step Guide 

### User API

**1. Sign Up as Admin or User**

![image](https://github.com/user-attachments/assets/27c1c64e-8733-4295-b47a-b6d96341b9eb)

**2. Using the email as username and password to sign in to receive token.**

![image](https://github.com/user-attachments/assets/c88a704b-1a4d-48ad-bbd2-28e4bc5bcd1b)

**3. Copy the generated token and save it as variable. Variables are defined in parent level so all API's will inherit them.**

![image](https://github.com/user-attachments/assets/8ff4afd4-53c1-42e5-b6e7-225bfa4f6310)

**4. API Authorization must be Bearer Token and the token value reference the token variable**

![image](https://github.com/user-attachments/assets/9216b538-b8a4-491d-828c-fe85187e7f95)

**5. User API getAllUsers method retrieves all the registered users. Note that user id 51 has no task for now.**

![image](https://github.com/user-attachments/assets/a5ba34b0-047b-4204-8b7d-5d68ac99886b)

### Task API

**6. As an admin we can create task and assign it to user with any role type. Notice that the new task is assigned to user with id 51 - which previously didnt have a task.** 

![image](https://github.com/user-attachments/assets/110fa331-de9c-44ea-95be-b06b8cf26d05)

**But if you try to create a task as a user, you will receive a warning**

![image](https://github.com/user-attachments/assets/7aa2840f-c97c-455e-9c4a-97b852ef5cd4)


**7. You can customize task search by using following dynamic search criterias: ``taskId``, ``taskStatus``, ``taskPriority``,``authorId``,``assigneeId``,``page``,``size``. You can use any of the filter. By default per page it shows 10 elements. If there are more than 10 elements, it will load second page**

![image](https://github.com/user-attachments/assets/e3ac159b-2483-4adf-9026-b9ec951bb41d)
![image](https://github.com/user-attachments/assets/aea90cd8-0bab-4cf4-b47d-eb35be4cff7e)

**8. Admin can delete tasks but users are not allowed.**

![image](https://github.com/user-attachments/assets/e476fa17-361f-4bf9-883f-f3d7bca6adc1)

![image](https://github.com/user-attachments/assets/a05476c2-9de0-436c-b027-dd47cb94422d)

### Comment API

**9. Admins can add comments to any task. Users can leave comment if only the task is assigned to them. For example, admin will add a new comment to task ``1`` of userId ``50``**

![image](https://github.com/user-attachments/assets/17bbbec7-3cda-4fce-b697-e616477708e8)

![image](https://github.com/user-attachments/assets/8a93c318-2141-4cca-84c1-8e19a1faacaf)

**If you try to add comment as a user with id different than 50, you will receive a warning.** 

![image](https://github.com/user-attachments/assets/36e96bbd-5ae3-44e6-ab3f-56fb458b5de6)




 


 
## Technologies
- **Java 17+**
- **Spring Boot**
- **PostgreSQL**
- **Spring Security JWT**
- **Docker**
