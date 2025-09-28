    package com.kaiburr.taskapi;

    import lombok.Data;
    import java.util.Date;

    // Lombok annotation generates getters, setters, toString, etc. automatically.
    @Data
    public class TaskExecution {
        private Date startTime;
        private Date endTime;
        private String output;
    }
    

