package com.tms.sportlight.config;

import com.tms.sportlight.util.DataDeleteJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
  @Bean
  public JobDetail dataDeleteJobDetail() {
    return JobBuilder.newJob(DataDeleteJob.class)
        .withIdentity("dataDeleteJob")
        .storeDurably()
        .build();
  }

  @Bean
  public Trigger dataDeleteTrigger() {
    return TriggerBuilder.newTrigger()
        .forJob(dataDeleteJobDetail())
        .withIdentity("dataDeleteTrigger")
        .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))  // 매일 자정에 실행 됨 ( 초 분 시 일 월 요일)
        .build();
  }
}
