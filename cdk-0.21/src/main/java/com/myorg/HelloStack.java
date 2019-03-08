package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Condition;
import software.amazon.awscdk.ConditionProps;
import software.amazon.awscdk.FnEquals;
import software.amazon.awscdk.FnIf;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.User;
import software.amazon.awscdk.services.iam.UserProps;
import software.amazon.awscdk.services.s3.CfnBucket;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.TopicProps;
import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.sqs.QueueProps;

public class HelloStack extends Stack {
    public HelloStack(final App parent, final String name) {
        this(parent, name, null);
    }

    public HelloStack(final App parent, final String name, final StackProps props) {
        super(parent, name, props);

        Condition testCondition = new Condition(this, "Test", ConditionProps.builder()
                .withExpression(new FnEquals("Hello", "world"))
                .build());

        Queue queue = new Queue(this, "MyFirstQueue", QueueProps.builder()
                .withQueueName(new FnIf("Test", "Hello", "Goodbye").toString())
                .build());
    }
}
