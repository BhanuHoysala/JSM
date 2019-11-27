package com.optile.jobscheduler.service;

import com.optile.jobscheduler.BaseTest;
import com.optile.jobscheduler.model.Job;
import com.optile.jobscheduler.model.JobPriority;
import com.optile.jobscheduler.model.JobStatus;
import com.optile.jobscheduler.repo.JobRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;

import java.util.stream.Collectors;

class JobScheduleRunnerTest extends BaseTest {

    @Autowired
    private JobScheduleRunner jobScheduleRunner;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {

        JobRepo.JOBS.clear();
        JobRepo.PROCESSED_JOBS.clear();
    }

    @Test
    @Description("A Job can be executed according to a schedule.")
    void jobSchedulingTest() throws InterruptedException {

        JobRepo.JOBS.put(new Job("Job1",
                JobPriority.LOW,
                true) {

            @Override
            public JobStatus doSomething() {
                this.setStatus(JobStatus.RUNNING);
                System.out.println("This is file processing Job");
                return JobStatus.SUCCESS;
            }
        });
        jobScheduleRunner.processJobs();
        Thread.sleep(2000l);
        Assertions.assertEquals(JobRepo.PROCESSED_JOBS.size(), 1);
        Assertions.assertEquals(JobRepo.PROCESSED_JOBS.get("Job1").getStatus(), JobStatus.SUCCESS);
    }

    @Test
    @Description("Reliability: there should be no side-effects created by a Job that fails")
    void jobScheduleReliabilityTest() throws InterruptedException {

        // Job 1 throws the Exception
        JobRepo.JOBS.put(new Job("Job1",
                JobPriority.HIGH,
                true) {

            @Override
            public JobStatus doSomething() {
                this.setStatus(JobStatus.RUNNING);
                if (true) {
                    throw new NullPointerException();
                }
                return JobStatus.SUCCESS;
            }
        });

        // Job 2 will not throw any exception
        JobRepo.JOBS.put(new Job("Job2",
                JobPriority.LOW,
                true) {

            @Override
            public JobStatus doSomething() {
                this.setStatus(JobStatus.RUNNING);
                int num = 2 * 2; // Doing something
                return JobStatus.SUCCESS;
            }
        });
        jobScheduleRunner.processJobs();
        Thread.sleep(1000l);
        Assertions.assertEquals(JobRepo.PROCESSED_JOBS.size(), 2);
        Assertions.assertEquals(JobRepo.PROCESSED_JOBS.get("Job1").getStatus(), JobStatus.FAILED); // Job 1 ended with exception
        Assertions.assertEquals(JobRepo.PROCESSED_JOBS.get("Job2").getStatus(), JobStatus.SUCCESS);
    }

    @Test
    @Description("Priority: Each Job can be executed based on its priority relative to other Jobs")
    void jobSchedulePriorityTest() throws InterruptedException {

        JobRepo.JOBS.put(new Job("Job1", JobPriority.HIGH, true) {
            @Override
            public JobStatus doSomething() {
                return null;
            }
        });

        JobRepo.JOBS.put(new Job("Job2", JobPriority.LOW, true) {
            @Override
            public JobStatus doSomething() {
                return null;
            }
        });

        JobRepo.JOBS.put(new Job("Job3", JobPriority.LOW, true) {
            @Override
            public JobStatus doSomething() {
                return null;
            }
        });

        JobRepo.JOBS.put(new Job("Job4", JobPriority.HIGH, true) {
            @Override
            public JobStatus doSomething() {
                return null;
            }
        });

        String prioritisedJobSequence = JobRepo.JOBS.stream().map(j -> j.getJobName())
                .collect(Collectors.joining(","));

        Assertions.assertEquals(prioritisedJobSequence, "Job4,Job1,Job3,Job2");
    }
}