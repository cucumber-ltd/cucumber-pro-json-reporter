# Cucumber-JVM plugin for Jam

This Cucumber plugin publishes documentation and results to Jam.

## Requirements

Your Cucumber project must be stored in a Git repository.

It's recommended (but not required) that you use a CI server to run Cucumber.
Any CI server works. If you use on of the following CI servers, some of the configuration
will be automatic.

* Bamboo
* Circle CI
* Jenkins
* Travis

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>pro-plugin</artifactId>
    <version>3.0.0</version>
    <scope>test</scope>
</dependency>
```

If you're not using Maven, declare a similar dependency. Next, tell Cucumber to use the plugin. 

If you're on Cucumber-JVM 4.0.0 or newer:

```java
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.cucumber.pro.JsonReporter:all"})
public class RunCucumberTest {
}
```

If you're on Cucumber-JVM 1.2.5 or older, use `io.cucumber.pro.JsonReporter12:all`.
The `all` part is the profile name (see the "Cucumber Profiles" section below). This is mandatory for `io.cucumber.pro.JsonReporter12`.

```java
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.cucumber.pro.JsonReporter12:all"})
public class RunCucumberTest {
}
```

If you're using Cucumber 2.0.0 - 3.0.2, please use [v2.1.1](https://jam.cucumber.io/projects/cucumber-pro-plugin-jvm/documents/tag/v2.1.1) of this plugin.

### Profiles

If you run Cucumber several times as part of your build (with different options, perhaps different tags), you can
specify a different *profile name* for each run. This allows Jam to show separate results for each profile.

The profile name is specified by appending a colon and a profile name to the class name:

```java
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.cucumber.pro.JsonReporter:smoke"}, tags = "@ui and @smoke")
public class RunCucumberTest {
}
```

```java
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.cucumber.pro.JsonReporter:all"}, tags = "not @ui and not @smoke")
public class RunCucumberTest {
}
```

## Configuration

The default configuration of the plugin is as follows:

```yaml
cucumberpro:
  # The name of the Jam project.
  projectname:

  # The project-specific authentication token. You can find it in the project settings (press `?` to display it).
  #
  # Do not share this token outside of your team or organisation. If you need to keep it out of source control, use
  # a CUCUMBERPRO_TOKEN environment variable instead.
  token:

  # The plugin sends your local environment variables to Jam so it can detect the CI build number, 
  # git branch/tag and other information about the build. This mask is a regular expression for filtering
  # out sensitive values that should not be sent to Jam.
  envmask: SECRET|KEY|TOKEN|PASSWORD|PWD

  # Sets the log level to one of `DEBUG`, `INFO`, `WARN`, `ERROR` or `FATAL`. Defaults to `WARN`.
  # Setting it to `DEBUG` will also print the current configuration when the plugin runs.
  logging: INFO

  # Writes out the log messages to the specified file. 
  logfile:

  # Override this if you are using a privately hosted Jam appliance.
  # We recommend setting this with a CUCUMBERPRO_URL environment variable defined globally on your build server.
  url: https://jam.cucumber.io/

  connection:
    # Set this to false if you want the build to break in case Jam is unavailable.
    ignoreerror: true

    # If a http or ssh connection takes longer than this (milliseconds), time out the connection.
    timeout: 5000
```

Depending on your environment you will have to override some of the defaults, or specify some of the
settings that don't have a default value.

The simplest way to override defaults is to create a file called `./cucumber.yml` at the root of your
repository, paste the above contents and make modifications. Then check it in to source control.
(If you already have this file, just add the content to the bottom).

You can also create a `/usr/local/etc/cucumber/cucumber.yml` file to define global settings.

Every setting can also be overridden with environment variables or Java system properties.

For example, if you want to increase logging:

```
# Linux / OS X
export CUCUMBERPRO_LOGGING=DEBUG

# Windows
SET CUCUMBERPRO_LOGGING=DEBUG
```

Alternatively, you can specify a Java system property (in Maven, Gradle or other build tool):

```
-Dcucumberpro.LOGGING=DEBUG
```

## Activating the plugin

The plugin will only attempt to publish results if it detects that it's running in a CI environment. The plugin
detects a CI environment by checking environment variables for well-known CI servers.

If you want to activate the plugin from a regular work station you can define the following environment variables:

* `GIT_COMMIT` - you can find it by running `git rev-parse HEAD`
* `GIT_BRANCH` - you can find it by running `git rev-parse --abbrev-ref HEAD`
