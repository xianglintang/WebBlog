package com.tangxianglin.blog.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("tom.jwt")
@Data
public class JWTProperties {

    private String secure;
    private int expire;
}
