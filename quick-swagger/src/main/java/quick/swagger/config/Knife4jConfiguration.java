package quick.swagger.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Knife4j 配置
 *
 * @author yehao
 * @date 2021/8/10
 */
@Configuration
@EnableKnife4j
@EnableSwagger2
public class Knife4jConfiguration {

    @Bean(value = "api")
    public Docket api() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("swagger-bootstrap-ui-demo API")
                        .description("<div style='font-size:14px;color:red;'>swagger-bootstrap-ui-demo API</div>")
                        .termsOfServiceUrl("http://yehao.dev/")
                        .contact(new Contact("yehao", "yehao.dev", "1042884980@qq.com"))
                        .version("1.0.0")
                        .build())
                //分组名称
                .groupName("测试接口")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
