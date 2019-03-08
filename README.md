# Example for error found in [aws-cdk-1984](https://github.com/awslabs/aws-cdk/issues/1984)

In CDK for Java 0.21.0, the `FnIf` condition extends `Token`, providing an implementation of `toString()` that performs as I expect: rendering the if-condition as a CloudFormation token string:
```
{
  "Fn::If": [
    "Condition",
    "IfTrue",
    "IfFalse"
  ]
}
```

After v0.21.0, specifically in the latest version 0.25.2, the new `Fn.conditionIf` method returns an `IConditionExpression` which does *not* extend `Token` or implement `toString()`.  As such, when calling `toString()` on the result of `Fn.conditionIf`, which is required to pass the result of the condition to any "props" that expects a `String`, serializes the Java object directly:
```
"software.amazon.awscdk.IConditionExpression$Jsii$Proxy@3bbc39f8"
```

This makes CDK >0.21.0 unusable from Java if your stack expects to use "Fn::If".  I believe that [feat: cloudformation condition chaining (#1494)](https://github.com/awslabs/aws-cdk/commit/216901570d6b3772d98e5b45f9debe61b5da3418) switched from returning an `FnCondition`, which extended `CloudFormationToken`, to returning `IConditionExpression`.  Instead, it should simply have returned `FnConditionBase`, `CloudFormationToken`, or even just `String`.
