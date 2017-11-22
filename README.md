# Cucumber Pro Plugin for Cucumber-JVM

[![CircleCI](https://circleci.com/gh/cucumber-ltd/cucumber-pro-plugin-jvm.svg?style=svg)](https://circleci.com/gh/cucumber-ltd/cucumber-pro-plugin-jvm)

[The documentation is here](https://app.cucumber.pro/projects/cucumber-pro-plugin-jvm)

## Release process

Artifacts are signed with GPG. Before you release, verify that you have
GPG configured properly:

    # The GPG key is in 1Password
    gpg --use-agent --local-user devs@cucumber.io -ab README.md

If that works, remove the generated `README.md.asc`. You're ready to release:

### Update the version number

* Update `pom.xml` to `[desired version]-SNAPSHOT`. (It usually has the right number unless you're making a major or minor release).
* Update the version number in `src/test/resources/io/cucumber/pro/README.md`
* Update `CHANGELOG.md`. Remove any empty sections for the released version. Update diff links.
* Commit everything.

    mvn release:clean
    mvn --batch-mode -P release-sign-artifacts release:prepare
    mvn --batch-mode -P release-sign-artifacts release:perform

Log in to https://oss.sonatype.org/, close and release the project.
