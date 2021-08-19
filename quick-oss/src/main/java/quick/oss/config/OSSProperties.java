package quick.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OSS 配置信息
 *
 * @author yehao
 * @date 2021/8/17
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss")
public class OSSProperties {

    /**
     * 内或外网域名
     */
    private String endpoint;

    /**
     * 密钥Access Key ID
     */
    private String accessKeyId;

    /**
     * 密钥Access Key Secret
     */
    private String accessKeySecret;
}
