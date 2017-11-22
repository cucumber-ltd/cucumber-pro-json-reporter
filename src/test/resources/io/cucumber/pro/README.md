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
    <version>1.1.2</version>
    <scope>test</scope>
</dependency>
```

If you're not using Maven, declare a similar dependency. Next, tell Cucumber to use the plugin. 

If you're on Cucumber-JVM 2.0.0 or newer:

```java
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.cucumber.pro.JsonReporter"})
public class RunCucumberTest {
}
```

If you're on Cucumber-JVM 1.2.5 or older, use `io.cucumber.pro.JsonReporter12:full`.
The `full` part is the profile name (see the "Cucumber Profiles" section below). This is mandatory for `io.cucumber.pro.JsonReporter12`.

```java
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.cucumber.pro.JsonReporter12:full"})
public class RunCucumberTest {
}
```

## Configuration

The plugin is configured with environment variables. Most of these can be left undefined - the plugin
provides sensible defaults.

* `CUCUMBER_PRO_TOKEN` (required for `https://app.cucumber.pro/`, should be set per-project)
    * Set it to the project-specific authentication token. Not required for privately hosted Cucumber Pro appliances with 
      unprotected results publishing.
* `CUCUMBER_PRO_BASE_URL` (required for appliance, should be set globally)
    * Set it to Cucumber Pro's base URL. Defaults to `https://app.cucumber.pro/`.
* `CUCUMBER_PRO_GIT_HOST` (required for appliance, should be set globally)
    * Set it to the hostname where Cucumber Pro's git server is. Defaults to `git.cucumber.pro`.
* `CUCUMBER_PRO_GIT_SSH_PORT` (optional, should be set globally)
    * Set it to `2222` if you're publishing to a Cucumber Pro appliance that hasn't been configured
      to use port `22` instead of `2222`. Defaults to `22`.
* `CUCUMBER_PRO_GIT_HOST_KEY` (optional, should be set globally)
    * Only required if the build machine's `~/.ssh/known_hosts` doesn't have an entry for the git host.
      To find the host key, run `ssh git@[git host]` and accept the host key. Then run `ssh-keyscan [git host]`
      and copy the long base64-encoded string at the end of the line. It looks like `AAAAB3NzaC1.....E/Bhw==`.
* `CUCUMBER_PRO_GIT_DEBUG` (optional, should be set per-project)
    * Enables logging for Git SSH traffic, useful for troubleshooting.
* `CUCUMBER_PRO_GIT_PUBLISH` (optional, should be set per-project)
    * Set to `false` or `no` to disable document publishing to git.
* `CUCUMBER_PRO_FETCH_FROM_SOURCE` (optional, should be set per-project)
    * Whether or not to fetch from the source repository before publishing
      to Cucumber Pro. This is needed on CI servers that perform a shallow 
      clone during build.
* `CUCUMBER_PRO_SOURCE_REMOTE_NAME` (optional, should be set per-project)
    * Where the plugin will `git fetch` from before publishing to Cucumber Pro.
      Defaults to `origin`.
* `CUCUMBER_PRO_PUBLISH` (optional, should be set globally)
    * Set it to `true` to enable the plugin. The plugin is enabled by default on preferred CI servers.
* `CUCUMBER_PRO_PROJECT_NAME` (optional, should be set per-project)
    * Not needed if the build is running on a preferred CI server and the Cucumber Pro project name is identical to the 
      CI server project name. Define this environment variable to override the project name.
      Alternatively you can add a `.cucumberpro.yml` file to the root directory of the git repository, with the following
      content:
      
      ```yaml
      project_name: HelloWorld
      ```
* `CUCUMBER_PRO_IGNORE_CONNECTION_ERROR` (optional)
    * Set this to `true` if you wish to treat connection errors to Cucumber Pro as warnings rather than errors.
      This can be useful to prevent build errors in case Cucumber Pro is down. Defaults to `false`.
* `CUCUMBER_PRO_CONNECTION_TIMEOUT` (optional)
    * Set this to `10000` or some other number of milliseconds to specify a custom connection timeout to Cucumber Pro.
      Defaults to `5000`.
* `CUCUMBER_PRO_ENV_MASK` (optional)
    * The plugin sends your local environment variables to Cucumber Pro so it can detect the CI build number, 
      git branch/tag and other information about the build. Set it to a pattern of environment variables that shouldn't 
      be sent to Cucumber Pro. Defaults to `SECRET|KEY|TOKEN|PASSWORD`.

## Git Authentication

### SSH machine user

The plugin will perform a `git push` to Cucumber Pro to publish `.feature` files and Markdown (`.md`) documents.

Cucumber Pro only allows project collaborators to do this, so the plugin must authenticate as a project collaborator.

The `https://app.cucumber.pro/` (SaaS) instance of Cucumber Pro has a machine user with email `devs@cucumber.io`.

For private appliance installations a system administrator should create a "machine account" in the local email server
(for example `cpro@example.com`). Then, sign up to Cucumber Pro with this email and activate the account.

After activating the account on Cucumber Pro, generate an SSH keypair and upload the public key to the machine account's settings page on Cucumber Pro. 
The private key should then be installed on the CI server.

### Make the machine user a collaborator

Every project on Cucumber Pro needs to add the machine user as a collaborator, and a system administrator (or someone
else with access to the machine user's mailbox) needs to accept the collaboration invitation.

## Results authentication

(This configuration can be skipped for private Cucumber Pro appliance installations where results publishing is open).

Results are published to Cucumber Pro using HTTP/HTTPS. Each Cucumber Pro project has a token for this purpose.
You can find it in the project settings (press `?` to display it).

This token should be assigned to a `CUCUMBER_PRO_TOKEN` environment variable on the build server, on a per-project basis.

Consult your CI server's documentation for details about defining per-project environment variables.
Some CI servers such as Travis and Circle CI allow you to define environment variables in a file checked into git.
*DO NOT DO THIS* - as it would allow anyone with read acceess to your repository to publish results.

## Cucumber Profiles

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
