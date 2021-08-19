package quick.quartz.initializer;

import cn.hutool.core.collection.ListUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import quick.quartz.entity.Task;

import java.util.List;

/**
 * 初始化任务调度器
 * 监听ContextRefreshedEvent事件 当所有的bean都初始化完成并被成功装载后触发
 *
 * @author yehao
 * @date 2021/8/19
 */
@Configuration
public class ApplicationStartQuartzJob implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Scheduler scheduler = null;
        try {
            scheduler = scheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        // 此处可以替换为从数据库获取
        List<Task> taskList = ListUtil.of(new Task("quick.quartz.job.PrintWordsJob", "*/5 * * * * ?"));

        for (Task task : taskList) {
            Class classs = null;
            try {
                classs = Class.forName(task.getJobClass());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            JobDetail jobDetail = JobBuilder.newJob(classs).withIdentity(task.getJobClass(), scheduler.DEFAULT_GROUP).build();
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(task.getJobClass(), scheduler.DEFAULT_GROUP)
                    .withSchedule(cronScheduleBuilder).build();
            try {
                scheduler.scheduleJob(jobDetail, cronTrigger);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始注入scheduler
     *
     * @return
     * @throws SchedulerException
     */
    @Bean
    public Scheduler scheduler() throws SchedulerException {
        SchedulerFactory schedulerFactoryBean = new StdSchedulerFactory();
        return schedulerFactoryBean.getScheduler();
    }
}
