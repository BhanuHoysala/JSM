package com.optile.jobscheduler.service;

import com.optile.jobscheduler.model.Job;
import com.optile.jobscheduler.repo.JobRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * created new class for ASync method since
 * Self-invocation – Calling the async method from within the same class won’t work
 */
@Slf4j
@Component
public class AsyncTaskExecutor {

    @Autowired
    private JobExecutor jobExecutor;

    @Async("threadPoolTaskExecutor")
    public void processJobsInAsync(Job job) {

        log.info("Async task started execute job {}", job.getJobName());
        job = jobExecutor.executeJob(job);
        JobRepo.PROCESSED_JOBS.put(job.getJobName(),job);
    }
}
