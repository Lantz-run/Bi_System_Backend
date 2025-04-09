package com.yupi.springbootinit.config;

/**
 * <p>Project: yubi-backend
 * <p>Powered by Lantz On 2025/3/29
 *
 * @author Lantz
 * @version 1.0
 * @Description huo
 * @since 1.8
 */

import io.github.briqt.spark4j.SparkClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author leikooo
 * @Description 星火 AI 配置类
 */
@Configuration
@ConfigurationProperties(prefix = "xun-fei.client")
@Data
public class XingHuoConfig {
    private String appId;
    private String apiSecret;
    private String apiKey;

    @Bean
    public SparkClient sparkClient() {
        SparkClient sparkClient = new SparkClient();
        sparkClient.apiKey = apiKey;
        sparkClient.apiSecret = apiSecret;
        sparkClient.appid = appId;
        return sparkClient;
    }
}

