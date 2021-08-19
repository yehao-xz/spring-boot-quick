package quick.sharding.jdbc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ShardingJdbc 读写分离
 *
 * @author yehao
 * @date 2021/8/19
 */
@SpringBootApplication
@MapperScan("quick.sharding.jdbc.mapper")
public class ShardingJdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcApplication.class, args);
    }
}
