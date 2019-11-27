package com.optile.jobscheduler.service;

import com.optile.jobscheduler.model.Job;
import com.optile.jobscheduler.model.JobStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * core class for the Job execution
 */
@Slf4j
@Component
public class JobExecutor {

    public Job executeJob(final Job job) {

        job.setStatus(JobStatus.RUNNING);
        try {
            //It is the responsibility to decide the coder to decide SUCCESS or FAILURE
            job.setStatus(job.doSomething());
        } catch (Exception e) {
            log.info("Exception occurred while processing a job", e);
            job.setMessage(e.getMessage());
            job.setStatus(JobStatus.FAILED);
        }
        return job;
    }
}
