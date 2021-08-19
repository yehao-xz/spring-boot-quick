package quick.quartz.job;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * 定义Job 打印任意内容
 *
 * @author yehao
 * @date 2021/8/19
 */
@Slf4j
public class PrintWordsJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        log.info("PrintWordsJob start at:{}, name:{}, group:{}.", DateUtil.now(), key.getName(), key.getGroup());
    }
}