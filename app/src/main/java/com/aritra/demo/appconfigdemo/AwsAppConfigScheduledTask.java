 package com.aritra.demo.appconfigdemo;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class AwsAppConfigScheduledTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AwsAppConfigScheduledTask.class);

    @Autowired
    private FeatureProperties featureProperties;

    @Autowired
    private AwsAppConfiguration appConfiguration;

  @Scheduled(fixedRate = 15000)
    public void pollConfiguration() throws UnsupportedEncodingException {
        LOGGER.info("Check for updated Information");
        JSONObject externalizedConfig = appConfiguration.pollConfigurations();
        featureProperties.setLocation(externalizedConfig.getString("location"));
        featureProperties.setName(externalizedConfig.getString("name"));
    } 

} 