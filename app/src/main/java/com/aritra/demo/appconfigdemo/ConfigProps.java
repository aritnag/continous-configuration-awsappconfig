package com.aritra.demo.appconfigdemo;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class ConfigProps {
    
    private String application;
    private String applicationEnvironment;
    private String applicatinProfile;

}
