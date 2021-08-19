package quick.dynamic.datasource.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import quick.dynamic.datasource.config.DynamicDataSourceContextHolder;
import quick.dynamic.datasource.interfaces.TargetDataSource;

/**
 * 动态数据源通知
 *
 * @author yehao
 * @date 2021/8/19
 */
@Aspect
@Order(-1)    //保证在@Transactional之前执行
@Component
@Slf4j
public class DynamicDattaSourceAspect {

    //改变数据源
    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        String dbid = targetDataSource.dataSourceName();
        if (!DynamicDataSourceContextHolder.isContainsDataSource(dbid)) {
            //joinPoint.getSignature() ：获取连接点的方法签名对象
            log.error("数据源 " + dbid + " 不存在使用默认的数据源 -> " + joinPoint.getSignature());
        } else {
            log.info("使用数据源：" + dbid);
            DynamicDataSourceContextHolder.setDataSourceType(dbid);
        }
    }

    @After("@annotation(targetDataSource)")
    public void clearDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        log.info("清除数据源 " + targetDataSource.dataSourceName() + " !");
        DynamicDataSourceContextHolder.clearDataSourceType();
    }
}
