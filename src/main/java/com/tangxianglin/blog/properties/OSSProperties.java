package com.tangxianglin.blog.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("tom.aliyunoss")
@Data
public class OSSProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String  bucketName;
}
