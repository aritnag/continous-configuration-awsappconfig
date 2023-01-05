#### Continuous Configuration using AWS AppConfig for Microservices 

Solution Design of the AWS Config Agent with Microservices: ![Alt text](solution_design/solution_design.png?raw=true "Solution-Design")


### Deployment Framework

Change the account number and put in the relevant details to deploy the framework.
For End to end implementation using CD-CI, Please check [CDK end to end ](https://medium.com/nordcloud-engineering/enterprise-implementation-of-infra-as-code-using-cdk-5d229e08b414)

 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation
 * `cdk deploy`      deploy this stack to your default AWS account/region


### Cleanup

 `cdk destroy`      Cleans up the stack

