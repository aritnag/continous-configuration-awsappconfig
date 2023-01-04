package com.aritra.demo.appconfigdemo;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.services.appconfigdata.AppConfigDataClient;
import software.amazon.awssdk.services.appconfigdata.model.GetLatestConfigurationRequest;
import software.amazon.awssdk.services.appconfigdata.model.GetLatestConfigurationResponse;
import software.amazon.awssdk.services.appconfigdata.model.StartConfigurationSessionRequest;

@Configuration
@ComponentScan
public class AwsAppConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AwsAppConfiguration.class);

    private AppConfigDataClient appConfig;
    private StartConfigurationSessionRequest startConfigurationSessionRequest;

    public JSONObject pollConfigurations() throws UnsupportedEncodingException {

        appConfig = configureAWSConfigAgent();

        startConfigurationSessionRequest = StartConfigurationSessionRequest.builder()
                .applicationIdentifier("ConfigurationTag-Cdk")
                .environmentIdentifier("dev")
                .configurationProfileIdentifier("cfn-conf-profile")
                .build();

        String sessionToken = appConfig.startConfigurationSession(startConfigurationSessionRequest)
                .initialConfigurationToken();
        GetLatestConfigurationRequest latestConfigurationRequest = GetLatestConfigurationRequest.builder()
                .configurationToken(sessionToken)
                .build();
        GetLatestConfigurationResponse latestConfigurationResponse = appConfig
                .getLatestConfiguration(latestConfigurationRequest);
        String response = latestConfigurationResponse.configuration().asUtf8String();
        return new JSONObject(response).getJSONObject("profile");
    }

    public JSONObject getConfiguration(ConfigProps configProps) throws UnsupportedEncodingException {

        appConfig = configureAWSConfigAgent();

        startConfigurationSessionRequest = StartConfigurationSessionRequest.builder()
                .applicationIdentifier(configProps.getApplication())
                .environmentIdentifier(configProps.getApplicationEnvironment())
                .configurationProfileIdentifier(configProps.getApplicatinProfile())
                .build();

        String sessionToken = appConfig.startConfigurationSession(startConfigurationSessionRequest)
                .initialConfigurationToken();
        GetLatestConfigurationRequest latestConfigurationRequest = GetLatestConfigurationRequest.builder()
                .configurationToken(sessionToken)
                .build();
        GetLatestConfigurationResponse latestConfigurationResponse = appConfig
                .getLatestConfiguration(latestConfigurationRequest);
        String response = latestConfigurationResponse.configuration().asUtf8String();
        return new JSONObject(response).getJSONObject("profile");
    }

    private AppConfigDataClient configureAWSConfigAgent() {
        return AppConfigDataClient.builder().build();
    }
}