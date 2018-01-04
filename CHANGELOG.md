# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

### Changed

* Document publishing is off by default. It needs to be explicitly enabled with the environment variable `CUCUMBER_PRO_GIT_PUBLISH=true`.

### Deprecated

### Removed

### Fixed

## [1.2.4] - 2017-12-18

### Added

* Always print filtered environment variables. When plugin runs. This was added to diagnose
  why the plugin behaves surprisingly in some environments, and will be removed in a future
  release.

## [1.2.3] - 2017-12-14

### Added

* Allow using Bamboo build server variable to get git revision.

## [1.2.2] - 2017-12-13

### Changed

* The published jar bundles all dependencies so there are no transitive maven dependencies. This simplifies installation into
  local nexus repositories, as there are fewer jars to manage.

## [1.2.1] - 2017-12-04

### Added
* A new `CUCUMBER_PRO_LOG_LEVEL` environment variable sets log level to one of `DEBUG`, `INFO`, `WARN`, `ERROR` or `FATAL`. Defaults to `WARN`.

### Removed
* The `CUCUMBER_PRO_GIT_DEBUG` environment variable no longer has any effect. Replaced by `CUCUMBER_PRO_LOG_LEVEL`.

### Fixed

* Added missing newline to logging statements

## [1.2.0] - 2017-11-22

### Added

* Added support for Cucumber-JVM 1.2.4 and 1.2.5 (#13, #15)
* Fetch from source repository before publishing to Cucumber Pro (#9)

### Changed

* On Bamboo, get project name from `bamboo_planRepository_name` rather than `bamboo_shortPlanName` (#14)

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

[Unreleased]: https://github.com/cucumber-ltd/cucumber-pro-plugin-jvm/compare/v1.2.4...HEAD
[1.2.4]: https://github.com/cucumber-ltd/cucumber-pro-plugin-jvm/compare/v1.2.3...v1.2.4
[1.2.3]: https://github.com/cucumber-ltd/cucumber-pro-plugin-jvm/compare/v1.2.2...v1.2.3
[1.2.2]: https://github.com/cucumber-ltd/cucumber-pro-plugin-jvm/compare/v1.2.1...v1.2.2
[1.2.1]: https://github.com/cucumber-ltd/cucumber-pro-plugin-jvm/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/cucumber-ltd/cucumber-pro-plugin-jvm/compare/v1.1.2...v1.2.0
[1.1.2]: https://github.com/cucumber-ltd/cucumber-pro-plugin-jvm/compare/v1.1.2...v1.1.2
[1.1.1]: https://github.com/cucumber-ltd/cucumber-pro-plugin-jvm/compare/v1.1.0...v1.1.1
