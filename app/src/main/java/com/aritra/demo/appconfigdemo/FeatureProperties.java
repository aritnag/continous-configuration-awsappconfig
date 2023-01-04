package com.aritra.demo.appconfigdemo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "feature")
@Data
public class FeatureProperties {

    private String name;
    private String location;
    
}