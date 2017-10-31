# Cucumber-JVM plugin for Cucumber Pro

This Cucumber plugin sends results to Cucumber Pro. This allows
Cucumber Pro to display results.

## Installation

First, install the library:

```xml
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>pro-reporter</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

Next, tell Cucumber to use the plugin:

```java
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.cucumber.pro.JsonReporter"})
public class RunCucumberTest {
}
```

## Configuration

There are several configuration options for the plugin:

* Project Name
* Cucumber Pro URL
* Activation
* Profile name

If you run Cucumber several times as part of your build (with different options), you can
specify a different profile name for each run. This allows Cucumber Pro to differentiate
between the different runs and give them each a name.

### Cucumber-JVM 2.0.1 and below

With Cucumber-JVM 2.0.2 and below you can specify the profile name with an environment variable:

```
export CUCUMBER_PROFILE_NAME=smoke
```

If you run Cucumber several times, you simply specify a different environment variable before each run.

### Cucumber-JVM 2.0.2 and above

With Cucumber-JVM 2.0.2 and above you can specify the profile by appending a colon and a profile name to the class name:

```java
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.cucumber.pro.JsonReporter:smoke"}, tags = "@ui and @smoke")
public class RunCucumberTest {
}
```

```java
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.cucumber.pro.JsonReporter:full"}, tags = "not @ui and not @smoke")
public class RunCucumberTest {
}
```

This is slightly better than defining `CUCUMBER_PROFILE_NAME`, because it lets you run several Cucumber times in the
same JVM, which can be a little faster and easier to configure.

## Activation

The plugin is activated by defining the `CUCUMBER_PRO_URL` environment variable.
If this environment variable is not defined, the plugin won't do anything.

It's recommended you only configure this environment variable in your CI server:

```
export CUCUMBER_PRO_URL=https://<auth-token>@app.cucumber.pro/tests/results/<project-name>
```

You need to replace `<auth-token>` with your Cucumber Pro project's authentication token.
You also need to replace `<project-name>` with your Cucumber Pro project name.

## Security

The plugin sends your local environment variables to Cucumber Pro so it can detect the CI build number, 
git branch/tag and other information about the build.

Environment variables with `SECRET|KEY|TOKEN|PASSWORD` (case insensitive) are stripped out and not sent to Cucumber Pro.

You can specify a custom pattern of environment variables to filter out with the following environment variable:

```
export CUCUMBER_PRO_ENV_MASK=...
```