# Cucumber-JVM plugin for Cucumber Pro

This Cucumber plugin sends results to Cucumber Pro. This allows
Cucumber Pro to display results.

## Requirements

* Git

## Installation

First, install the library:

```xml
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>pro-plugin</artifactId>
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

## Project Name configuration

The plugin needs to detect the Cucumber Pro project name so it can send results to the right Cucumber Pro project. 

If the build runs on a supported CI server (see below), you may not have to declare the project name explicitly.
If you need to override the project name detection for supported CI servers, or if you're using a different CI
server or none at all, you have to declare your project name as an environment variable or in a file:

### Environment variable

If the `CUCUMBER_PRO_PROJECT_NAME` environment variable is defined, that will be used as the project name.
This overrides all other project name settings.

### Configuration file (`.cucumberpro.yml`)

If there is a `.cucumberpro.yml` file in directory where the build runs (usually the root directory of the repository),
the project name will be picked up from the `project_name` field. Example:

```yaml
project_name: cucumber-pro-plugin-jvm
```

### Bamboo

If your Bamboo *plan name* is the same as your Cucumber Pro *project name*, no project name configuration is required.
The plugin will pick this up from the `bamboo_shortPlanName` environment variable that Bamboo sets automatically.

### Circle CI

If you use Travis and the name of your git repo (without the org or user prefix) is the same as your Cucumber Pro 
*project name*, no project name configuration is required. The plugin will pick this up from the 
`CIRCLE_PROJECT_REPONAME` environment variable that Circle CI sets automatically.

### Travis

If you use Travis and the name of your git repo (without the org or user prefix) is the same as your Cucumber Pro 
*project name*, no project name configuration is required. The plugin will pick this up from the 
`TRAVIS_REPO_SLUG` environment variable that Travis sets automatically.

## Activation

The plugin will detect if the build is running in a CI environment by checking for the presence of environment
variables defined by supported CI servers. If you want to publish results from an unsupported CI server or
a machine that isn't a CI server, simply define the `CUCUMBER_PRO_PUBLISH` environment variable with any value
different than `false` or `no`.

## Profiles

If you run Cucumber several times as part of your build (with different options), you can
specify a different *profile name* for each run. This allows Cucumber Pro to show separate results for each profile.

### Cucumber-JVM 2.0.1 and below

With Cucumber-JVM 2.0.1 and below you can specify the profile name with an environment variable:

```
export CUCUMBER_PROFILE_NAME=smoke
```

If you run Cucumber several times, you simply specify a different environment variable before each run.

### Cucumber-JVM 2.1.0 and above

With Cucumber-JVM 2.1.0 and above you can specify the profile by appending a colon and a profile name to the class name:

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

## Security

The plugin sends your local environment variables to Cucumber Pro so it can detect the CI build number, 
git branch/tag and other information about the build.

Environment variables with `SECRET|KEY|TOKEN|PASSWORD` (case insensitive) are stripped out and not sent to Cucumber Pro.

You can specify a custom pattern of environment variables to filter out with the following environment variable:

```
export CUCUMBER_PRO_ENV_MASK=...
```