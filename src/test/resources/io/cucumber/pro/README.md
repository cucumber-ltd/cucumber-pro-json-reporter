# Cucumber-JVM plugin for Cucumber Pro

This Cucumber plugin publishes documentation and results to Cucumber Pro.

## Requirements

The plugin only works with projects in git at the moment.

If you use one of the following CI servers, setup will be easier than if you don't.

* Bamboo
* Circle CI
* Travis

## Installation

First, install the library:

```xml
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>pro-plugin</artifactId>
    <version>1.0.1</version>
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

The plugin is configured with environment variables. Most of these can be left undefined - the plugin
provides sensible defaults.

* `CUCUMBER_PRO_TOKEN` (required for `https://app.cucumber.pro`)
    * Set it to the project-specific authentication token. Not required for appliance, where result publishing is open.
* `CUCUMBER_PRO_BASE_URL` (required for appliance)
    * Set it to Cucumber Pro's base URL. Defaults to `https://app.cucumber.pro/`
* `CUCUMBER_PRO_GIT_HOST` (required for appliance)
    * Set it to the hostname where Cucumber Pro's git server is. Defaults to `git.cucumber.pro`.
* `CUCUMBER_PRO_GIT_HOST_KEY` (optional)
    * Only required if the build machine's `~/.ssh/known_hosts` doesn't have an entry for the git host.
* `CUCUMBER_PRO_GIT_DEBUG` (optional)
    * Enables logging for Git SSH traffic, useful for troubleshooting
* `CUCUMBER_PRO_GIT_PUBLISH` (optional)
    * Set to `false` or `no` if you want to disable publishing to git.
* `CUCUMBER_PRO_PUBLISH` (optional)
    * Set it to `true` to explicitly enable the plugin. Not required if the build is running on a supported CI server.
* `CUCUMBER_PRO_PROJECT_NAME` (optional)
    * Set it to the Cucumber Pro project name if the build isn't running on a supported CI server, or if the CI server project name is different 
      from the Cucumber Pro project name.
* `CUCUMBER_PRO_ENV_MASK` (optional)
    * Set it to a pattern of environment variables that shouldn't be sent to Cucumber Pro. Defaults to `SECRET|KEY|TOKEN|PASSWORD`.

## Authentication

Cucumber Pro restricts write access to documentation and results to authenticated users. You need to grant access to 
the plugin so it can publish documentation (using Git and SSH) and results (using HTTP or HTTPS).

### Git

Cucumber `.feature` files and Markdown (`.md`) documents are published on Cucumber Pro using Git. Only users that are
collaborators on a project can publish documents on Cucumber Pro.

We recommend you create a "machine account" on Cucumber Pro for this purpose. This is simply an account that is created 
for the sole purpose of pushing documents, and doesn't have to be associated to a real person.

After creating a machine account and set its SSH public key via the Cucumber Pro user interface, you need to install
the corresponding private key on the CI machine.

If you're using a privately hosted Cucumber Pro appliance, you should define an environment variable with
the host name (or IP address), for example: 

```
export CUCUMBER_PRO_GIT_HOST=cucumberpro.example.com
```

### Results

You need to define a `CUCUMBER_PRO_TOKEN` environment variable on the machine running Cucumber. You can find this token
on your Cucumber Pro project's settings page. Example:

```
export CUCUMBER_PRO_TOKEN=ababababababababababa
```

If you are using the hosted Cucumber Pro on `https://app.cucumber.pro`, one of the preferred CI servers, and CI project 
names identical to your Cucumber Pro project names, no further configuration should be needed. 

If that's not the case, please read on.

## Cucumber Pro URL

If you're using a privately hosted Cucumber Pro appliance, you have to let the plugin know. Simply define the following
environment variable:

```
export CUCUMBER_PRO_URL=http://cucumberpro.example.com/
```

## Project Name configuration

The plugin needs to detect the Cucumber Pro project name so it can send results to the right Cucumber Pro project. 

If the build runs on a supported CI server (see below), you may not have to declare the project name explicitly.
If you need to override the project name detection for supported CI servers, or if you're using a different CI
server or none at all, you have to declare your project name as an environment variable or in a file:

### Environment variable

If the `CUCUMBER_PRO_PROJECT_NAME` environment variable is defined, that will be used as the project name.
This overrides all other project name settings. Example:

```
export CUCUMBER_PRO_PROJECT_NAME="HelloWorld"
```

### Configuration file (`.cucumberpro.yml`)

If there is a `.cucumberpro.yml` file in directory where the build runs (usually the root directory of the repository),
the project name will be picked up from the `project_name` field. Example:

```yaml
project_name: HelloWorld
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