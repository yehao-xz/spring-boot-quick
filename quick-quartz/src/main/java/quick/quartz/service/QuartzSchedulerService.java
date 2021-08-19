package quick.quartz.service;

import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 任务操作
 *
 * @author yehao
 * @date 2021/8/19
 */
@Service
@RequiredArgsConstructor
public class QuartzSchedulerService {

    private final Scheduler scheduler;

    /**
     * 获取Job信息
     *
     * @param name 类名
     * @return
     * @throws SchedulerException
     */
    public String getJobInfo(String name) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, scheduler.DEFAULT_GROUP);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
                scheduler.getTriggerState(triggerKey).name());
    }

    /**
     * 修改某个任务的执行时间
     *
     * @param name 类名
     * @param cron 任务触发时间
     * @return
     * @throws SchedulerException
     */
    public boolean modifyJob(String name, String cron) throws SchedulerException {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(name, scheduler.DEFAULT_GROUP);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(cron)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, scheduler.DEFAULT_GROUP)
                    .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }

    /**
     * 暂停所有任务
     *
     * @throws SchedulerException
     */
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 暂停某个任务
     *
     * @param name 类名
     */
    public void pauseJob(String name) throws SchedulerException {
        JobKey jobKey = new JobKey(name, scheduler.DEFAULT_GROUP);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复所有任务
     *
     * @throws SchedulerException
     */
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 恢复某个任务
     *
     * @param name 类名
     */
    public void resumeJob(String name) throws SchedulerException {
        JobKey jobKey = new JobKey(name, scheduler.DEFAULT_GROUP);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除某个任务
     *
     * @param name 类名
     */
    public void deleteJob(String name) throws SchedulerException {
        JobKey jobKey = new JobKey(name, scheduler.DEFAULT_GROUP);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.deleteJob(jobKey);
    }

    /**
     * 添加定时器
     *
     * @param name 类名
     * @param cron 任务触发时间
     * @throws SchedulerException
     */
    public void addJob(String name, String cron) throws SchedulerException {
        Class classs = null;
        try {
            classs = Class.forName(name);
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        JobDetail jobDetail = JobBuilder.newJob(classs).withIdentity(name, scheduler.DEFAULT_GROUP).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, scheduler.DEFAULT_GROUP)
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }
}