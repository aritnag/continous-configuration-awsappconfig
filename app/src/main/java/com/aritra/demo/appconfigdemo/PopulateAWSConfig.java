package com.aritra.demo.appconfigdemo;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PopulateAWSConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(PopulateAWSConfig.class);

    @Autowired
    private FeatureProperties featureProperties;

    @Autowired
    private AwsAppConfiguration appConfiguration;


    public void populateValues(ConfigProps configProps) throws UnsupportedEncodingException {
        LOGGER.info("Check for updated Information");
        JSONObject externalizedConfig = appConfiguration.getConfiguration(configProps);
        featureProperties.setLocation(externalizedConfig.getString("location"));
        featureProperties.setName(externalizedConfig.getString("name"));
    }


}