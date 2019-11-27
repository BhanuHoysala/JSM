package com.optile.jobscheduler.service;

import com.optile.jobscheduler.model.Job;
import com.optile.jobscheduler.repo.JobRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class JobManager {

    @Autowired
    private JobExecutor jobExecutor;

    public Job process(Job job) {

        if (job.isScheduled()) {
            // Scheduling the job to run in Queue
            return scheduleJob(job);
        }

        // If the job is not scheduled process instantly
        return jobExecutor.executeJob(job);
    }

    /**
     * Queues the job for scheduled execution
     *
     * @param job
     * @return job
     */
    private Job scheduleJob(Job job) {

        JobRepo.JOBS.add(job);
        return job;
    }
}
