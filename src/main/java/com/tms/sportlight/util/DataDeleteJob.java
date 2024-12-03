package com.tms.sportlight.util;


import com.tms.sportlight.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataDeleteJob implements Job {

private final NotificationService notificationService;
  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    notificationService.deleteData();
  }
}
