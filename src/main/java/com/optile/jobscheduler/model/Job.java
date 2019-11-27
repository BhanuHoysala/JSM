package com.optile.jobscheduler.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public abstract class Job implements Comparable<Job> {

    /**
     * TO identify a Job, TODO - Ideally we should have a Job ID as well
     */
    private String jobName;

    /**
     * Determines the status of a Job at any given time
     * default value will be QUEUED
     */
    private JobStatus status = JobStatus.QUEUED;

    private JobPriority jobPriority;

    /**
     * Flag to determine the job is scheduled or run instantly
     */
    private boolean scheduled;

    /**
     * In case of Job failed it will have the reason of Job failure
     */
    private String message;

    public Job(String jobName, JobPriority jobPriority, boolean scheduled) {

        this.jobName = jobName;
        this.jobPriority = jobPriority;
        this.scheduled = scheduled;
    }

    @Override
    public int compareTo(Job job) {
        return this.jobPriority == JobPriority.LOW ? 0 : -1;
    }

    public abstract JobStatus doSomething() throws Exception;

}