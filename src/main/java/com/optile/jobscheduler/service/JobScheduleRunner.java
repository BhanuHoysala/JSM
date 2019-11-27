package com.optile.jobscheduler.service;

import com.optile.jobscheduler.repo.JobRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Class to perform the scheduled job execution
 */
@Slf4j
@Service
@AllArgsConstructor
public class JobScheduleRunner {

    @Autowired
    private AsyncTaskExecutor asyncTaskExecutor;

    /**
     * Job processor invokes the method at regular interval
     */
    @Scheduled(cron = "${job.schedule.cron}")
    public void processJobs() {

        log.info("Cron processor started at {}", Instant.now());
        log.info("{} Jobs available to process", JobRepo.JOBS.size());
        while (!JobRepo.JOBS.isEmpty()) {

            asyncTaskExecutor.processJobsInAsync(JobRepo.JOBS.poll());
        }
        log.info("Cron processor ended at {}", Instant.now());
    }

}
