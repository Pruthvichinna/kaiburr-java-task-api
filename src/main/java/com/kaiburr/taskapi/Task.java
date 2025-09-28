    package com.kaiburr.taskapi;

    import lombok.Data;
    import org.springframework.data.annotation.Id;
    import org.springframework.data.mongodb.core.mapping.Document;
    import java.util.List;

    @Data
    @Document(collection = "tasks") // Tells Spring this class maps to the "tasks" collection in MongoDB
    public class Task {
        @Id // Marks this field as the unique identifier for documents in the collection
        private String id;
        private String name;
        private String owner;
        private String command;
        private List<TaskExecution> taskExecutions;
    }