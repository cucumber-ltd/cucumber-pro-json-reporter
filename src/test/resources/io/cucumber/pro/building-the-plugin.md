# Building the plugin

If you've found a bug in the plugin, or need to diagnose why something isn't working
as expected, you can modify the source code and build the plugin yourself.

This is a guide to the steps involved.

## Modify and build the plugin

Get a copy of the latest source code at `https://github.com/cucumber-ltd/cucumber-pro-plugin-jvm`.
Add some debug statements, for example in the `JsonReporter`'s constructor:

```java
System.out.println("ENV:\n" + filteredEnv);
```

Now, build the plugin:

```
mvn clean package
```

The plugin jar should now be in `./target/pro-plugin-1.2.4-SNAPSHOT.jar` (possibly with a higher version number).
Note that the build might fail if you're using a Nexus repo that doesn't have all the jars that are required
to build the plugin. In that case you may have to obtain the jar in a different way.

## Add the built jar to your project

Over in your own project, run the following command:

```
mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file \
  -Dfile=../cucumber-pro-plugin-jvm/target/pro-plugin-1.2.4-SNAPSHOT.jar \
  -DgroupId=io.cucumber \
  -DartifactId=pro-plugin \
  -Dversion=1.2.4-SNAPSHOT \
  -Dpackaging=bundle \
  -DlocalRepositoryPath=./maven
```

You may have to change the `file` and `version` properties if you built a different version.

After running this you should have a copy of the plugin jar in `maven/io/cucumber/pro-plugin/**`.

## Add the jars to source control

```
mvn add -f maven
```

## Add a new Maven repo

Maven won't pick up the jars you added unless you tell it to, so let's do that. Add this to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>local-repo</id>
        <url>file://${basedir}/maven</url>
    </repository>
</repositories>
```

Also change your dependency to match the version of the jar you imported:

```xml
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>pro-plugin</artifactId>
    <version>1.2.4-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```


