package com.optile.jobscheduler.repo;

import com.optile.jobscheduler.model.Job;
import com.optile.jobscheduler.model.JobPriority;
import com.optile.jobscheduler.model.JobStatus;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

@Data
@Component
public class JobRepo {

    public static final BlockingQueue<Job> JOBS = new PriorityBlockingQueue<>();

    // TODO - save the Job status to DB where Job ID will be the primary key or key value
    public static final Map<String,Job> PROCESSED_JOBS = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {

        JOBS.put(new Job("1", JobPriority.HIGH, false) {
            @Override
            public JobStatus doSomething() {
                return null;
            }
        });

        JOBS.put(new Job("2", JobPriority.LOW, false) {
            @Override
            public JobStatus doSomething() {
                return null;
            }
        });

        JOBS.put(new Job("3", JobPriority.LOW, false) {
            @Override
            public JobStatus doSomething() {
                return null;
            }
        });

        JOBS.put(new Job("4", JobPriority.HIGH, false) {
            @Override
            public JobStatus doSomething() {
                return null;
            }
        });

        JOBS.stream().forEach(j-> System.out.println(j.getJobName()));

    }
}
