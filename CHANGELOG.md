# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

* Added support for Cucumber-JVM 1.2.4 and 1.2.5 (#13, #15)
* Fetch from source repository before publishing to Cucumber Pro (#9)

### Changed

* On Bamboo, get project name from `bamboo_planRepository_name` rather than `bamboo_shortPlanName` (#14)

### Deprecated

### Removed

### Fixed

## [1.1.2] - 2017-11-03

### Added

* Added `CUCUMBER_PRO_GIT_SSH_PORT` so the port can be overridden (#12).

## [1.1.1] - 2017-11-03

### Added

* Added `CUCUMBER_PRO_IGNORE_CONNECTION_ERROR` to prevent build failures (#4).
* Added `CUCUMBER_PRO_CONNECTION_TIMEOUT` to set custom connection timeout.
* Plugin suggests defining `CUCUMBER_PRO_TOKEN` on auth failure (#6).
* Added a changelog.

### Fixed

* Don't fail when `CUCUMBER_PRO_GIT_HOST_KEY` isn't specified (#10).

## 1.1.0 - 2017-11-02

First proper release!

[Unreleased]: https://github.com/cucumber-ltd/cucumber-pro-plugin-jvm/compare/v1.1.2...HEAD
[1.1.2]: https://github.com/cucumber-ltd/cucumber-pro-plugin-jvm/compare/v1.1.2...1.1.2
[1.1.1]: https://github.com/cucumber-ltd/cucumber-pro-plugin-jvm/compare/v1.1.0...1.1.1
