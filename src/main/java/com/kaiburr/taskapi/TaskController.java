    package com.kaiburr.taskapi;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.io.BufferedReader;
    import java.io.InputStreamReader;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;
    import java.util.Optional;
    import java.util.concurrent.TimeUnit;

    @RestController
    @RequestMapping("/tasks") // All endpoints in this class will start with /tasks
    public class TaskController {

        // Spring automatically provides an instance of TaskRepository
        @Autowired
        private TaskRepository taskRepository;

        // Endpoint to GET all tasks or a single task by ID
        // Handles GET requests to /tasks and /tasks?id=...
        @GetMapping
        public ResponseEntity<List<Task>> getTasks(@RequestParam(required = false) String id) {
            if (id != null) {
                Optional<Task> taskOptional = taskRepository.findById(id);
                if (taskOptional.isPresent()) {
                    List<Task> result = new ArrayList<>();
                    result.add(taskOptional.get());
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if not found
                }
            } else {
                List<Task> tasks = taskRepository.findAll();
                return new ResponseEntity<>(tasks, HttpStatus.OK);
            }
        }

        // Endpoint to PUT (create or update) a task
        // Handles PUT requests to /tasks
        @PutMapping
        public ResponseEntity<Task> createTask(@RequestBody Task task) {
            // Simple validation to prevent potentially harmful commands
            String command = task.getCommand();
            if (command == null || command.contains("rm ") || command.contains("mv ") || command.contains("sudo")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return 400 for bad requests
            }
            Task savedTask = taskRepository.save(task);
            return new ResponseEntity<>(savedTask, HttpStatus.CREATED); // Return 201 when created
        }

        // Endpoint to DELETE a task by ID
        // Handles DELETE requests to /tasks/{id}
        @DeleteMapping("/{id}")
        public ResponseEntity<HttpStatus> deleteTask(@PathVariable String id) {
            try {
                taskRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 on successful deletion
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Endpoint to GET (find) tasks by name
        // Handles GET requests to /tasks/findByName?name=...
        @GetMapping("/findByName")
        public ResponseEntity<List<Task>> findTasksByName(@RequestParam String name) {
            List<Task> tasks = taskRepository.findByNameContaining(name);
            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        }

        // Endpoint to PUT a TaskExecution (execute a command for a task)
        // Handles PUT requests to /tasks/{id}/execute
        @PutMapping("/{id}/execute")
        public ResponseEntity<Task> executeTask(@PathVariable String id) {
            Optional<Task> taskOptional = taskRepository.findById(id);
            if (taskOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Task task = taskOptional.get();
            TaskExecution execution = new TaskExecution();
            execution.setStartTime(new Date());

            try {
                ProcessBuilder processBuilder = new ProcessBuilder();
                // Check the operating system to run commands correctly
                String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("win")) {
                    processBuilder.command("cmd.exe", "/c", task.getCommand());
                } else {
                    processBuilder.command("/bin/sh", "-c", task.getCommand());
                }

                Process process = processBuilder.start();

                // Capture the command's output
                StringBuilder output = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                // Set a timeout to prevent long-running commands
                boolean exited = process.waitFor(10, TimeUnit.SECONDS);
                if (!exited) {
                    process.destroy();
                    output.append("\nTimeout: Command took too long to execute.");
                }
                
                execution.setOutput(output.toString().trim());

            } catch (Exception e) {
                execution.setOutput("Error executing command: " + e.getMessage());
            } finally {
                execution.setEndTime(new Date());
                if (task.getTaskExecutions() == null) {
                    task.setTaskExecutions(new ArrayList<>());
                }
                task.getTaskExecutions().add(execution);
                taskRepository.save(task); // Save the task with the new execution record
            }

            return new ResponseEntity<>(task, HttpStatus.OK);
        }
    }
    

