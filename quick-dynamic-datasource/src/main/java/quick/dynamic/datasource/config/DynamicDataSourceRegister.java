package quick.dynamic.datasource.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册动态数据源 初始化数据源和提供了执行动态切换数据源的工具类 EnvironmentAware（获取配置文件配置的属性值）
 *
 * @author yehao
 * @date 2021/8/19
 */
@Slf4j
@Configuration
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    // 默认数据源
    private DataSource defaultDataSource;

    // 用户自定义数据源
    private Map<String, DataSource> slaveDataSources = new HashMap<String, DataSource>();

    @Override
    public void setEnvironment(Environment environment) {
        initDefaultDataSource(environment);
        initSlaveDataSources(environment);
    }

    /**
     * Description: 初始化默认数据源
     *
     * @param
     * @return
     * @data 2019年6月21日
     */
    private void initDefaultDataSource(Environment environment) {
        Map<String, Object> dsMap = new HashMap<>();
        dsMap.put("type", environment.getProperty("spring.datasource.type"));
        dsMap.put("driver", environment.getProperty("spring.datasource.driver-class-name"));
        dsMap.put("url", environment.getProperty("spring.datasource.url"));
        dsMap.put("username", environment.getProperty("spring.datasource.username"));
        dsMap.put("password", environment.getProperty("spring.datasource.password"));
        defaultDataSource = buildDataSource(dsMap);
    }

    /**
     * Description: 初始化自定义数据源
     *
     * @param
     * @return
     * @data 2019年6月21日
     */
    private void initSlaveDataSources(Environment environment) {
        // 读取配置文件获取更多数据源
        String dsPrefixs = environment.getProperty("slave.datasource.names");
        for (String dsPrefix : dsPrefixs.split(",")) {
            // 多个数据源
            Map<String, Object> dsMap = new HashMap<>();
            dsMap.put("type", environment.getProperty("slave.datasource." + dsPrefix + ".type"));
            dsMap.put("driver", environment.getProperty("slave.datasource." + dsPrefix + ".driver-class-name"));
            dsMap.put("url", environment.getProperty("slave.datasource." + dsPrefix + ".url"));
            dsMap.put("username", environment.getProperty("slave.datasource." + dsPrefix + ".username"));
            dsMap.put("password", environment.getProperty("slave.datasource." + dsPrefix + ".password"));
            DataSource ds = buildDataSource(dsMap);
            slaveDataSources.put(dsPrefix, ds);
        }
    }

    @SuppressWarnings("unchecked")
    public DataSource buildDataSource(Map<String, Object> dataSourceMap) {
        try {
            String type = dataSourceMap.get("type").toString();
            Class<? extends DataSource> dataSourceType;
            dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);
            String driverClassName = dataSourceMap.get("driver").toString();
            String url = dataSourceMap.get("url").toString();
            String username = dataSourceMap.get("username").toString();
            String password = dataSourceMap.get("password").toString();
            // 自定义DataSource配置
            DataSourceBuilder<? extends DataSource> dataSourceBuilder = DataSourceBuilder
                    .create()
                    .driverClassName(driverClassName)
                    .url(url)
                    .username(username)
                    .password(password)
                    .type(dataSourceType);
            return dataSourceBuilder.build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 注册数据源
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        //添加默认数据源
        targetDataSources.put("dataSource", this.defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
        //添加自定义数据源
        targetDataSources.putAll(slaveDataSources);
        slaveDataSources.keySet().forEach(key -> {
            DynamicDataSourceContextHolder.dataSourceIds.add(key);
        });

        //创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        //注册 - BeanDefinitionRegistry
        registry.registerBeanDefinition("dataSource", beanDefinition);

        log.info("----------------Dynamic DataSource Registry----------------");
    }
}
