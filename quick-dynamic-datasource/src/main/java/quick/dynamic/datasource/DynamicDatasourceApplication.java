package quick.dynamic.datasource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import quick.dynamic.datasource.config.DynamicDataSourceRegister;

/**
 * 动态切换数据源
 *
 * @author yehao
 * @date 2021/8/19
 */
@SpringBootApplication
@MapperScan("quick.dynamic.datasource.mapper")
@Import(DynamicDataSourceRegister.class)
public class DynamicDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicDatasourceApplication.class, args);
    }
}
