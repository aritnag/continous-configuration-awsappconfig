package com.myorg;

import java.util.List;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.appconfig.CfnApplication;
import software.amazon.awscdk.services.appconfig.CfnConfigurationProfile;
import software.amazon.awscdk.services.appconfig.CfnConfigurationProfile.ValidatorsProperty;
import software.amazon.awscdk.services.appconfig.CfnDeployment;
import software.amazon.awscdk.services.appconfig.CfnDeploymentStrategy;
import software.amazon.awscdk.services.appconfig.CfnEnvironment;
import software.amazon.awscdk.services.appconfig.CfnHostedConfigurationVersion;
import software.constructs.Construct;

public class AWSAppConfigDeploy extends Stack {
    public AWSAppConfigDeploy(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AWSAppConfigDeploy(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        final CfnApplication application = CfnApplication.Builder
        .create(this, "cfn-conf-application")
        .description("L1 Construct for AWS AppConfig")
        .name("ConfigurationTag-Cdk")
        .build();

        final CfnConfigurationProfile cfnConfigurationProfile = CfnConfigurationProfile.Builder
        .create(this, "cfn-conf-profile")
        .applicationId(application.getRef())
        .locationUri("hosted")
        .name("cfn-conf-profile")
        .validators(List.of(ValidatorsProperty.builder()
        .content("{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"type\":\"object\",\"properties\":{\"profile\":{\"type\":\"object\",\"properties\":{\"name\":{\"type\":\"string\"},\"location\":{\"type\":\"string\"}},\"required\":[\"name\",\"location\"]}},\"required\":[\"profile\"]}")
        .type("JSON_SCHEMA")
        .build()))
        .description("Testing the Application Configuration Profile")
        .build();

        final CfnHostedConfigurationVersion cfnHostedConfigurationVersion =
        CfnHostedConfigurationVersion.Builder.create(this, "cfn-conf-hostedconfigurtation")
        .applicationId(application.getRef())
        .configurationProfileId(cfnConfigurationProfile.getRef())
        .contentType("application/json")
        .content("{\"profile\":{\"name\":\"Joydip\",\"location\":\"Ireland\"}}")
        .build();

        final CfnEnvironment cfnEnvironment = CfnEnvironment.Builder
        .create(this, "cfn-conf-environment")
        .applicationId(application.getRef())
        .name("dev")
/*          .monitors(List.of(MonitorsProperty.builder()
        .alarmArn(Alarm.Builder.create(this, "appconfigdemo-Alarm")
        .metric(Metric.Builder.create()
        .namespace("AWS/ApplicationELB")
        .metricName("HTTPCode_ELB_5XX_Count")
        .dimensionsMap(java.util.Map.of(  
            "LoadBalancerName", "AWSAppConfigDemo"))
        .build())
        .threshold(1)
        .evaluationPeriods(1)
        .datapointsToAlarm(1).build().getAlarmArn()) 
        .alarmRoleArn(Role.Builder
        .create(this, "appconfigdemo-monitor-role")
        .roleName("AppConfigLabCloudWatchRole")
        .description(" Allows role to call CloudWatch DescribeAlarms.")
        .path("/")
        .inlinePolicies(Map.of("cloudwatch-appconfig-alarm",PolicyDocument.Builder.create()
        .statements(List.of(PolicyStatement.Builder.create()
        .effect(Effect.ALLOW)
        .resources(List.of("*"))
        .actions(List.of("cloudwatch:DescribeAlarms"))
        .build())).build()))
        .assumedBy(new ServicePrincipal("appconfig.amazonaws.com"))
        .build().getRoleArn())
        .build())) */
        .build();

         
        CfnDeployment.Builder
        .create(this, "cfn-conf-deployment")
        .applicationId(application.getRef())
        .configurationProfileId(cfnConfigurationProfile.getRef())
        .configurationVersion(cfnHostedConfigurationVersion.getRef())
        .environmentId(cfnEnvironment.getRef())
        .deploymentStrategyId(CfnDeploymentStrategy.Builder
        .create(this, "cfn-conf-deployment-strategy")
        .deploymentDurationInMinutes(1)
        .finalBakeTimeInMinutes(1)
        .growthType("LINEAR")
        .growthFactor(100)
        .name("cfn-conf-deployment-strategy")
        .replicateTo("SSM_DOCUMENT")
        .build().getRef())
        .build(); 

         
    }
}
