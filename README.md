Kaiburr Task 1: Java REST API for Task Management
This repository contains the solution for Task 1 of the Kaiburr recruitment assessment. It is a RESTful API built with Java and Spring Boot that allows for creating, searching, deleting, and executing shell command tasks.

🌟 Features
CRUD Operations: Full support for Creating, Reading, Updating, and Deleting "task" objects.

Database Integration: Uses MongoDB to persist task data.

Shell Command Execution: An endpoint is provided to execute the shell command associated with a task and store its output.

Custom Search: An endpoint to find tasks by their name.

Secure: Includes basic validation to prevent potentially malicious commands.

🏛️ Architecture Overview
This project follows a standard three-tier architecture to ensure a robust and scalable application:

Java Spring Boot (The Application Layer): This is the core of the application. It acts as the "brain," handling all incoming HTTP requests, processing the business logic for each endpoint, and coordinating with the database.

MongoDB (The Data Layer): This is the application's permanent "memory." When a new task is created, the Java application sends it to the MongoDB database for persistent storage. Without the database, all data would be lost when the application stops.

Docker (The Environment Layer): Docker is used to run the MongoDB database inside an isolated container. This is a modern development practice that allows us to run essential services like databases quickly and cleanly, without installing them directly on the machine. It ensures the development environment is consistent and easy to manage.

🛠️ Prerequisites
To run this application, you will need the following installed on your machine:

Java JDK 17 or higher

Apache Maven 3.9+

Docker Desktop (for running the MongoDB database)

Postman (or any other API client for testing)

🚀 How to Run the Application
Clone the repository:

git clone <your-repository-url>
cd kaiburr-java-task-api

Start the MongoDB database using Docker:

docker run -d -p 27017:27017 --name my-mongo mongo

Run the Spring Boot application using Maven:

mvn spring-boot:run

The application will start and be accessible at http://localhost:8081.

⚙️ API Endpoints
The base URL for the API is http://localhost:8081/tasks.

PUT /tasks

Creates a new task. The task object is passed in the request body.

GET /tasks

Returns a list of all tasks.

GET /tasks?id={taskId}

Returns a single task matching the provided ID.

GET /tasks/findByName?name={name}

Finds and returns all tasks whose name contains the search string.

PUT /tasks/{taskId}/execute

Executes the shell command for the specified task.

DELETE /tasks/{taskId}

Deletes the task with the specified ID.

📸 Screenshots of API Tests
Here are the screenshots from Postman proving that all endpoints work as expected.

1. Create a Task (PUT /tasks)
![Image](https://github.com/user-attachments/assets/b2d8a992-ad42-48af-a4ff-0a81fe99ea41)

2. Get All Tasks (GET /tasks)
![Image](https://github.com/user-attachments/assets/a7614b55-3eaf-4665-bb62-cefaa7986702)

3. Get Task by ID (GET /tasks?id=myFirstTask)
![Image](https://github.com/user-attachments/assets/8749058c-de67-40e4-b357-059797f50e45)

4. Find Task by Name (GET /tasks/findByName?name=Print)
![Image](https://github.com/user-attachments/assets/763c8134-0ab1-4ceb-877b-d89b70218b0a)

5. Execute a Task (PUT /tasks/myFirstTask/execute)
![Image](https://github.com/user-attachments/assets/c886b83d-ac0e-42a6-bf1e-c36e3ee095ac)

6. Delete a Task (DELETE /tasks/myFirstTask)
![Image](https://github.com/user-attachments/assets/0d2045b7-cfe9-45ff-b4ab-1b84103ab53c)
