# Cucumber Pro Plugin for Cucumber-JVM

[The documentation is here](https://app.cucumber.pro/projects/cucumber-pro-plugin-jvm)

## Release process

Artifacts are signed with GPG. Before you release, verify that you have
GPG configured properly:

    # The GPG key is in 1Password
    gpg --use-agent --local-user devs@cucumber.io -ab README.md

If that works, remove the generated `README.md.asc`. You're ready to release:

    mvn release:clean
    mvn --batch-mode -P release-sign-artifacts release:prepare
    mvn --batch-mode -P release-sign-artifacts release:perform
    # Log in to https://oss.sonatype.org/, close and release the project
