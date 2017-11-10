# Cucumber Pro Plugin for Cucumber-JVM

[![CircleCI](https://circleci.com/gh/cucumber-ltd/cucumber-pro-plugin-jvm.svg?style=svg)](https://circleci.com/gh/cucumber-ltd/cucumber-pro-plugin-jvm)

[The documentation is here](https://app.cucumber.pro/projects/cucumber-pro-plugin-jvm)

## Release process

Artifacts are signed with GPG. Before you release, verify that you have
GPG configured properly:

    # The GPG key is in 1Password
    gpg --use-agent --local-user devs@cucumber.io -ab README.md

If that works, remove the generated `README.md.asc`. You're ready to release:

Make sure the version number in `pom.xml` is `[desired version]-SNAPSHOT`. If not,
modify it and commit the change.

Update `CHANGELOG.md`. Remove any empty sections for the released version. Update diff links.

    mvn release:clean
    mvn --batch-mode -P release-sign-artifacts release:prepare
    mvn --batch-mode -P release-sign-artifacts release:perform
    # Log in to https://oss.sonatype.org/, close and release the project
