package com.myorg;

import java.util.List;
import java.util.Map;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.IpAddresses;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.VpcProps;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.PolicyDocument;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.constructs.Construct;

public class ApplicationDeploy extends Stack {
    public ApplicationDeploy(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public ApplicationDeploy(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        final Vpc vpc = vpcBuilder();
        final Cluster cluster = ecsCluterBuilder(vpc);
        DeployApplicationViaEcsAlb(cluster);

    }

    private void DeployApplicationViaEcsAlb(final Cluster cluster) {
        ApplicationLoadBalancedFargateService loadBalancedFargateService =ApplicationLoadBalancedFargateService.Builder.create(this, "awsappconfig-demo-loadbalancer")
                .cluster(cluster)
                .cpu(512)
                .desiredCount(1)
                .taskImageOptions(
                        ApplicationLoadBalancedTaskImageOptions.builder()
                                .image(ContainerImage.fromAsset("../app"))
                                .containerPort(8080)
                                .taskRole(Role.Builder
                                .create(this, "appconfigdemo-task-role")
                                .path("/")
                                .inlinePolicies(Map.of("ecs-service",PolicyDocument.Builder.create()
                                .statements(List.of(PolicyStatement.Builder.create()
                                .effect(Effect.ALLOW)
                                .resources(List.of("*"))
                                .actions(List.of("ec2:AttachNetworkInterface"
                                ,"ec2:CreateNetworkInterface"
                                ,"ec2:CreateNetworkInterfacePermission"
                                ,"ec2:DeleteNetworkInterface"
                                ,"ec2:DeleteNetworkInterfacePermission"
                                ,"ec2:Describe*"
                                ,"ec2:DetachNetworkInterface"
                                ,"elasticloadbalancing:DeregisterInstancesFromLoadBalancer"
                                ,"elasticloadbalancing:DeregisterTargets"
                                ,"elasticloadbalancing:Describe*"
                                ,"elasticloadbalancing:RegisterInstancesWithLoadBalancer"
                                ,"elasticloadbalancing:RegisterTargets"
                                ,"appconfig:GetEnvironment"
                                ,"appconfig:GetHostedConfigurationVersion"
                                ,"appconfig:GetConfiguration"
                                ,"appconfig:GetApplication",
                                "appconfig:StartConfigurationSession",
                                "appconfig:GetLatestConfiguration"
                                ,"appconfig:GetConfigurationProfile"))
                                .build())).build()))
                                .managedPolicies(List.of(
                                        ManagedPolicy.fromManagedPolicyArn(this,
                                                "AmazonECSTaskExecutionRolePolicy-appconfigdemo-task-role",
                                                "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy")))
                                .assumedBy(new ServicePrincipal("ecs-tasks.amazonaws.com"))
                                .build())
                                .executionRole(Role.Builder
                                .create(this, "appconfigdemo-execution-role")
                                .path("/")
                                .managedPolicies(List.of(
                                        ManagedPolicy.fromManagedPolicyArn(this,
                                                "AmazonECSTaskExecutionRolePolicy-appconfigdemo-execution-role",
                                                "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy")))
                                .assumedBy(new ServicePrincipal("ecs-tasks.amazonaws.com"))
                                .build())
                                .build())
                
                .memoryLimitMiB(2048)
                .publicLoadBalancer(true)
                .loadBalancerName("AWSAppConfigDemo") 
                .build();
                
        loadBalancedFargateService.getTargetGroup().configureHealthCheck(HealthCheck.builder()
        .path("/profile/details")
        .build());
        
    }

    private Cluster ecsCluterBuilder(final Vpc vpc) {
        return Cluster.Builder.create(this, "awsappconfig-demo-ecs")
                .vpc(vpc)
                .clusterName("awsappconfig-demo")
                .build();
    }

    private Vpc vpcBuilder() {
        return new Vpc(this, "awsappconfig-demo", VpcProps
                .builder()
                .ipAddresses(IpAddresses.cidr("10.215.0.0/16"))
                .maxAzs(2)
                .natGateways(1)
                .build());
    }
}
