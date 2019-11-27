package com.optile.jobscheduler.service;

import com.optile.jobscheduler.BaseTest;
import com.optile.jobscheduler.model.Job;
import com.optile.jobscheduler.model.JobPriority;
import com.optile.jobscheduler.model.JobStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;

class JobManagerTest extends BaseTest {

    @Autowired
    private JobManager jobManager;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Execute the job without scheduling it")
    void executeJobInstantly() {

        Job job = new Job("Job1",
                JobPriority.LOW,
                false) {

            @Override
            public JobStatus doSomething() {
                this.setStatus(JobStatus.RUNNING);
                System.out.println("This is file processing Job");
                return JobStatus.SUCCESS;
            }
        };

        job = jobManager.process(job);
        Assertions.assertEquals(JobStatus.SUCCESS, job.getStatus());
    }

    @Test
    @Description("Gracefully handling the Jobs which throws exception")
    void gracefullyHandlingExceptions() {

        Job job = new Job("Job2",
                JobPriority.HIGH,
                false) {

            @Override
            public JobStatus doSomething() {
                this.setStatus(JobStatus.RUNNING);
                throw new ArithmeticException();
            }
        };

        job = jobManager.process(job);
        Assertions.assertEquals(JobStatus.FAILED, job.getStatus());
    }
}