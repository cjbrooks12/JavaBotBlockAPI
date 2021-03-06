[issues]: https://github.com/botblock/JavaBotBlockAPI/issues
[Code of Conduct]: https://github.com/botblock/JavaBotBlockAPI/blob/master/CODE_OF_CONDUCT.md
[BotBlock API]: https://botblock.org/api/docs

# Contributing Guidelines
We welcome everyone to contribute to the JavaBotBlockAPI project.  
But to keep a certain order and style over the project do you have to follow those guidelines here. But don't worry! They're fairly simple.

# Issues
If you encounter problems with the API feel free to open an issue on the [issues] tab.  
But before doing so make sure to check for the following things.

## Using latest version
We do not support old versions of this API. We release new versions to fix issues, update dependencies or add new methods.  
So if you encounter an issue or want a feature to be added, make sure to use the latest version as it might contain new functions and/or bugfixes.

## Make sure it is not an issue of external (3rd party) libraries
JavaBotBlockAPI uses different 3rd party libraries such as JDA and also depends on the [BotBlock API].  
This means that some errors you encounter could be the cause of said 3rd party libraries or sites not working as intended.  
We can't fix issues related to those. If you encounter errors with those, go to the respective repository/site and report the issue.

## Issue doesn't already exist
Before creating an issue, make sure that you checked the issues page for any existing issue of the same type.  
If one exists, comment on it and provide additional informations.  
The best way for us to fix errors and issues is by gathering informations to find out why those issues happen. And having multiple issues complaining about the same issue won't help anyone here and only slows the process of fixing it down.

## Follow the templates
We have templates in place to make sure we receive the required informations.  
Please follow those templates or else your issue might get ignored and closed.

----
# Pull Requests
We accept pull requests for fixing issues or adding new features, but you have to follow those rules.

## Javadocs
Please add javadocs comments to **all public methods the developer has access to and should use.**  
Javadocs help developers to see what methods they can/should use and what those do.

When adding Javadoc comments, follow this Styling choises.

### Line breaks and paragraphs
Start a new line with `<br>` (Don't close this tag).  
Do the same for new paragraphs, but keep an empty line in between the text (And of course use `<p>` instead).

**Example**:
```java
/**
 * This is a small summary of some method.
 * <br>It uses line breaks like those.
 *
 * <p>But also new paragraphs are used.
 */
```

### Links
Please always use the full path to a class/method when using `{@link}`

Bad example: `{@link BotBlockAPI}`  
Good example: `{@link org.botblock.javabotblockapi.BotBlockAPI BotBlockAPI}`

We want to point out the alternative text used in the Good example, to display "BotBlockAPI" instead of the path.  
When linking to a method that is in a separate class, set the alternative text to `Class.method(option(s))`.

If the method you link to is within the same class, use `{@link #methodName() methodName()}`.

Linking to external Javadoc may be set with the same linking-rules.  
New external javadocs may need to be added to the `javadoc` task in the `build.gradle`.

**Note**:  
Use the `<a href="">` option to link external pages. When doing so also remember to include `target="_blank"`.  
A link could look like this: `<a href="https://google.com" target="_blank">Google it!</a>` (You have to close the a tag.)

### param styling
When the method has parameters, you have to set a description with the `@param` option.  
Note that the description has to be on a new line.

**Example**:
```java
/**
 * @param foo
 *        The param description.
 */
```

### Since
Since annotations are used to mention since what version a method/class is available.  
The syntax is `@since v{major}.{minor}.{build}` where `{major}` is only changed on huge changes (Entire recodes f.e.), `{minor}` is updated on bigger changes of classes and similar and `{build}` is changed on every other noteworthy change.

In most cases will you only update `{build}` and nothing else. If you're unsure if you can/should update the version, ask a maintainer of this repository for assistance.

Please **do not change already existing since annotations**. This also includes adding ones to already existing methods/classes.

### Deprecated methods
If you're deprecating a method will you need to add the `@Deprecated` annotation and also include a `@deprecated` tag in the comment explain why it was deprecated and mention possible replacements.

You also need to add the `@DeprecatedSince` annotation to indicate since when a method is deprecated. This annotation has a required version field and an optional replacements field.  
You may also add the `@PlannedRemoval` annotation if the object is planned to be removed in a future release. This annotation has a required version field, which indicate in what version the object will be removed.  
The specified version has to be at least 2 versions away from the one that the object got deprecated in (i.e. deprecating a method in `5.2.0` will require you to set the version to at least `5.2.2` in the `PlannedRemoval` annotation).

### Other Styling
Please make sure that all text is on the same vertical line (block).  
This means that when f.e. a `@return` is used, that you have to use two spaces after `@param` instead of one.

### Order of the parts
Here is an example of the different parts being used:  
```java
/**
 * Adds "Bar" to the provided text.
 * <br>Will throw an error when not providing text.
 * 
 * @param  foo
 *         The text that should get "Bar" added to it.
 *
 * @return The provided String with "Bar" added to it.
 *
 * @throws IllegalArgumentException
 *         When the provided String is empty/null.
 *
 * @since v1.0.0
 *
 * @deprecated Use {@link #getFooBar() getFooBar()} instead.
 */
@Deprecated
@DeprecatedSince(version = "1.0.0", replacements = {"#getFooBar"}) // If you deprecate a method, add this one too.
@PlannedRemoval(version = "1.0.2") // When the objects gets removed in the future, add this annotation.
public String getFooBar(String foo) throws IllegalArgumentException{
    if(foo.isEmpty())
        throw new IllegalArgumentException("foo may not be empty");
        
    return foo + "Bar";
}

/**
 * Returns "foobar".
 *
 * @return {@code "foobar"}
 *
 * @since v1.0.1
 */
public String getFooBar(){
    return "foobar";
}
```

## [Code of Conduct]
We want to give everyone a chance to contribute to the project.  
So please keep your comments and messages nice. Every message that is considered rasist, insulting or similar will be removed.  
If you continue to send those messages will we permanently remove you from this repository.
