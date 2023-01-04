package com.aritra.demo.appconfigdemo;


import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class FeatureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureController.class);

    @Autowired
    private FeatureProperties featureProperties;

    @Autowired
    private PopulateAWSConfig populateValues;


    private ConfigProps configProps;

    @Autowired
    public FeatureController(Environment env) {
        configProps = new ConfigProps();
        configProps.setApplicatinProfile(env.getProperty("awsappconfig.profile")); 
        configProps.setApplication(env.getProperty("awsappconfig.application"));
        configProps.setApplicationEnvironment(env.getProperty("awsappconfig.environment"));
    }



    @GetMapping("/details")
    public String details() throws UnsupportedEncodingException {

        populateValues.populateValues(configProps);
        String message = "Profile : name is " + featureProperties.getName() + ", Location is "+ featureProperties.getLocation() ;
        LOGGER.info(message);
        return message;
    }
}
