package com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.stereotype.Service;

@Service
public class BatchJobService {
  private static final Logger logger = LoggerFactory.getLogger(BatchJobService.class);

  private final JobLauncher jobLauncher;
  private final Job importAllJob;

  public BatchJobService(JobLauncher jobLauncher, Job importAllJob) {
    this.jobLauncher = jobLauncher;
    this.importAllJob = importAllJob;
  }

  public String runImportAllJob() {
    try {
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addLong("startAt", System.currentTimeMillis())
              .toJobParameters();

      JobExecution jobExecution = jobLauncher.run(importAllJob, jobParameters);
      return "Job started. Status: " + jobExecution.getStatus();
    } catch (JobExecutionAlreadyRunningException e) {
      return "Job is already running.";
    } catch (Exception e) {
      logger.error("Exception while running job. {}", e.getMessage());
      return "Failed to start job: " + e.getMessage();
    }
  }
}
