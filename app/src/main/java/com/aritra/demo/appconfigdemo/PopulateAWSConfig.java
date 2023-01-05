package com.aritra.demo.appconfigdemo;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;



@Configuration
public class PopulateAWSConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(PopulateAWSConfig.class);

    @Autowired
    private FeatureProperties featureProperties;

    @Autowired
    private AwsAppConfiguration appConfiguration;

   @Autowired
    private Environment env;

    public void populateValues(ConfigProps configProps) throws UnsupportedEncodingException {
        LOGGER.info("Check for updated Information");

        try {
            JSONObject externalizedConfig = appConfiguration.getConfiguration(configProps);
            featureProperties.setLocation(externalizedConfig.getString("location"));
            featureProperties.setName(externalizedConfig.getString("name"));
        
        } catch (Exception e) {
            featureProperties.setLocation(env.getProperty("profile.location"));
            featureProperties.setName(env.getProperty("profile.name"));
        }
        
        
    }


}