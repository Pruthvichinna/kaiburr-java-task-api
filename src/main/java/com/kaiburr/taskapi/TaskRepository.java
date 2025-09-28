    package com.kaiburr.taskapi;

    import org.springframework.data.mongodb.repository.MongoRepository;
    import java.util.List;

    public interface TaskRepository extends MongoRepository<Task, String> {
        // This is a custom query method. Spring Data MongoDB is smart enough
        // to automatically create a query that finds all Tasks where the
        // 'name' property contains the string passed to the method.
        List<Task> findByNameContaining(String name);
    }
    

