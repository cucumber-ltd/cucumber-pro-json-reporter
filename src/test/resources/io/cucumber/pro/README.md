# Cucumber-JVM plugin for Cucumber Pro

This Cucumber plugin publishes documentation and results to Cucumber Pro.

## Requirements

Your Cucumber project must be stored in a Git repository.

It's recommended (but not required) that you use a CI server to run Cucumber.
Any CI server works. If you use on of the following CI servers, some of the configuration
will be automatic.

* Bamboo
* Circle CI
* Travis

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>pro-plugin</artifactId>
    <version>2.0.0</version>
    <scope>test</scope>
</dependency>
```

If you're not using Maven, declare a similar dependency. Next, tell Cucumber to use the plugin. 

If you're on Cucumber-JVM 2.0.0 or newer:

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

### Profiles

If you run Cucumber several times as part of your build (with different options, perhaps different tags), you can
specify a different *profile name* for each run. This allows Cucumber Pro to show separate results for each profile.

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
  # The plugin sends your local environment variables to Cucumber Pro so it can detect the CI build number, 
  # git branch/tag and other information about the build. This mask is a regular expression for filtering
  # out sensitive values that should not be sent to Cucumber Pro.
  envmask: SECRET|KEY|TOKEN|PASSWORD

  # Sets the log level to one of `DEBUG`, `INFO`, `WARN`, `ERROR` or `FATAL`. Defaults to `WARN`.
  # Setting it to `DEBUG` will also print the current configuration when the plugin runs.
  logging: warn

  # Not needed if the build is running on a preferred CI server and the Cucumber Pro project 
  # name is identical to the CI server project name. Define this environment variable to override 
  # the project name.
  projectname:

  # Override this if you are using a privately hosted Cucumber Pro appliance.
  # We recommend setting this with a CUCUMBERPRO_URL environment variable defined globally on your build server.
  url: https://app.cucumber.pro/

  connection:
    # Set this to false if you want the build to break in case Cucumber Pro is unavailable.
    ignoreerror: true

    # If a http or ssh connection takes longer than this (milliseconds), time out the connection.
    timeout: 5000

  git:
    # Only required if the build machine's `~/.ssh/known_hosts` doesn't have an entry for the git hostname.
    # To find the host key, run `ssh git@[git host]` and accept the host key. Then run `ssh-keyscan [git host]`
    # and copy the long base64-encoded string at the end of the line. It looks like `AAAAB3NzaC1.....E/Bhw==`.
    # We recommend setting this with a global CUCUMBERPRO_GIT_HOSTKEY environment variable.
    hostkey:

    # Override this if you are using a privately hosted Cucumber Pro appliance.
    hostname: git.cucumber.pro

    # Set this to 2222 if you're publishing to a privately hosted Cucumber Pro appliance that hasn't been 
    # configured to use port `22` instead of `2222`. Defaults to `22`.
    sshport: 22

    # Set this to true if you want the plugin to publish documentation with a git push.
    publish: false

    source:
      # Whether or not to fetch from the source repository (GitHub, BitBucket, GitLab etc) 
      # before publishing to Cucumber Pro. This is needed on CI servers that perform a shallow 
      # clone during build.
      fetch: true

      # Where the plugin will `git fetch` from before publishing to Cucumber Pro.
      # Defaults to `origin`.
      remote: origin

  results:
    # (This configuration can be skipped for private Cucumber Pro appliance installations where results publishing is open).
    # Results are published to Cucumber Pro using HTTP/HTTPS. Each Cucumber Pro project has a token for this purpose.
    # You can find it in the project settings (press `?` to display it).
    # This token should be assigned to a `CUCUMBERPRO_RESULTS_TOKEN` environment variable on the build server, on a per-project basis.
    # Consult your CI server's documentation for details about defining per-project environment variables.
    # Some CI servers such as Travis and Circle CI allow you to define environment variables in a file checked into git.
    # *DO NOT DO THIS* - as it would allow anyone with read acceess to your repository to publish results.
    token:

    # Whether or not to publish results to Cucumber Pro. Normally you should *not* provide
    # a value for this setting - the plugin automatically publishes if it detects it is running
    # in a CI environment. We recommend setting this to true only if you are experimenting with
    # the configuration locally.
    publish:
```

Depending on your environment you will have to override some of the defaults, or specify some of the
settings that don't have a default value.

The simplest way to override defaults is to create a file called `./cucumber.yml` at the root of your
repository, paste the above contents and make modifications. Then check it in to source control.
(If you already have this file, just add the content to the bottom).

You can also create a `/usr/local/etc/cucumber/cucumber.yml` file to define global settings.

Every setting can also be overridden with environment variables or Java system properties.

For example, if you want to enable git publishing, you can define an environment variable:

```
# Linux / OS X
export CUCUMBERPRO_GIT_PUBLISH=true

# Windows
SET CUCUMBERPRO_GIT_PUBLISH=true
```

Alternatively, you can specify a Java System property (in Maven, Gradle or other build tool):

```
-Dcucumberpro.git.publish=true
```

## Git Authentication

The plugin can perform a `git push` to Cucumber Pro to publish `.feature` files and Markdown (`.md`) documents.
Cucumber Pro only allows project collaborators to do this, so the plugin must authenticate as a project collaborator.

### SSH machine user

The `https://app.cucumber.pro/` (SaaS) instance of Cucumber Pro has a machine user with email `devs@cucumber.io`.

For private appliance installations a system administrator should create a "machine account" in the local 
user directory or email server (for example `cpro@example.com`). 

Then, sign up to Cucumber Pro with this email and activate the account.

After activating the account on Cucumber Pro, generate an SSH key pair and upload the public key to the 
machine account's settings page on Cucumber Pro. The private key should then be installed on the CI server.

### Make the machine user a collaborator

Every project on Cucumber Pro needs to add the machine user as a collaborator, and a system administrator (or someone
else with access to the machine user's mailbox) needs to accept the collaboration invitation.
