This formatter sends results as JSON to Cucumber Pro.

By default the formatter will do nothing. To activate the formatter.
the `CUCUMBER_PRO_URL` envrionment variable *must* be defined.

The formatter will then try to detect whether it is running in
a project under Git source control. If it cannot detect that it is
running in Git, the formatter will fail the build.

The formatter needs to detect the Git revision. It will shell out to `git` to do this.
