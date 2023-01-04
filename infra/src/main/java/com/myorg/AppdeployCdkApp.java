package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

public class AppdeployCdkApp {
    public static void main(final String[] args) {
        App app = new App();

        final Stack appDeploy = new ApplicationDeploy(app, "Appdeploy", StackProps.builder()
                .build());

        final Stack appConfigDeploy = new AWSAppConfigDeploy(app, "AWSAppConfigDeploy", StackProps.builder()
                .build());
        appConfigDeploy.addDependency(appDeploy);
        app.synth();
    }
}
