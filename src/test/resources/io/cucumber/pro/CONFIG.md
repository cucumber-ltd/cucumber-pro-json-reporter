# Cucumber-Config

This library provides a flexible way for users to specify configuration values in various ways:
- YAML File
- Environment variables
- Java System properties (JVM only)

The library can be used by any program that needs external configuration.

## Keys

Let's assume the program needs to know the URL of a web server serving cat pictures, which can
be anywhere. We define a `key` for this called `album.cat.server`.

The program could then access the value of that key like this:

```java
String catServer = config.getString("album.cat.webserver");
```

## Assigning values to keys

The user has several options for assigning a value to this key:

### YAML file

```yaml
album:
  cat:
    webserver: http://www.infinitecat.com/
```

### Environment variable

```
# Linux / OS X
export ALBUM_CAT_WEBSERVER=http://www.infinitecat.com/

# Windows
SET ALBUM_CAT_WEBSERVER=http://www.infinitecat.com/
```

### Java System property

```
-Dalbum.cat.webserver=http://www.infinitecat.com/
```

## Case insensitivity and special characters

Keys are *case insensitive*. If a key contains a `_` character (common in environment variables),
it is converted to a `.`.

The internals of the `config` object stores keys and values in a tree, where the `.` characters
in the keys define the paths in the tree.

You *may* add additional `_` characters to key segments in your YAML documents, but keep in mind
that they will be removed from the key before the value in added to the config.

```yaml
# A slightly more legible way to define album.cat.webserver
album:
  cat:
    web_server: http://www.infinitecat.com/
```

## Defaults

Your program must define default values for all of the keys you look up. Looking
up the value for a key that doesn't have a (default) value will throw an error. This helps
you make sure you have defined default values for all the keys your program uses.

## Inspection

The `Config#toYAML` method returns a string representation of the config. This is useful for
showing the user what their current configuration is - it can sometimes be hard for them to know
when values are pulled from multiple sources.