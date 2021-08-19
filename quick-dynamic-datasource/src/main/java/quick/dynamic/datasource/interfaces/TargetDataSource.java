package quick.dynamic.datasource.interfaces;

import java.lang.annotation.*;

/**
 * 定义动态数据源注解	作用于类、接口或者方法上
 *
 * @author yehao
 * @date 2021/8/19
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {

    String dataSourceName();
}
