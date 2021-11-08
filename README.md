# Gravatar Java Client

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.hayes-roach/gravatar-java-client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.hayes-roach/gravatar-java-client)

This is a java implementation for the Gravatar XML-RPC API: https://en.gravatar.com/site/implement/xmlrpc/
 
# Getting Started
Gradle:
```groovy
testCompile "io.github.hayes-roach:gravatar-java-client:1.0.2"
```
Maven:
```xml
<dependency>
  <groupId>io.github.hayes-roach</groupId>
  <artifactId>gravatar-java-client</artifactId>
  <version>1.0.2</version>
</dependency>
```

## Initialization
```Java
Authentication authentication = new Authentication("test@email.com", "testpassword123");
GravatarClient gravatarClient = new GravatarClient(authentication);

// then you can use client methods below ...
```

# Examples
1. [ Check whether email hashes have a Gravatar ](#ex1)
2. [ Get map of userimages and associated Address object ](#ex2)
3. [ Get map of userimages and associated metadata array ](#ex3)
4. [ Save Base64 String as userimage  ](#ex4)
5. [ Save URL String as userimage ](#ex5)
6. [ Set current userimage ](#ex6)
7. [ Set current Gravatar to default userimage ](#ex7)
8. [ Delete userimage ](#ex8)
9. [ Get Email Hashes ](#ex9)


<a name="ex1"></a>
## Check whether hashes have a Gravatar
```Java
// put code here
```

<a name="ex2"></a>
## Get map of userimages and associated Address object
```Java
// put code here
```

<a name="ex3"></a>
## Get map of userimages and associated metadata array
- MetadataArray[0] = image rating
- MetadataArray[1] = image url

```Java
// put code here
```

<a name="ex4"></a>
## Save Base64 String as userimage 
Saves binary image data as a userimage for this account. It does not SET the image as the users active Gravatar. Use the [gravSetUserimage()](#ex6) function to do that.
```Java
// put code here
```

<a name="ex5"></a>
## Save URL String as userimage
Reads an image via its URL and saves that as a userimage for this account. It does not SET the image as the users active Gravatar. Use the [gravSetUserimage()](#ex6) function to do that.
```Java
// put code here
```

<a name="ex6"></a>
## Set Current userimage
Sets a userimage as the current gravatar for one of more email addresses on this account
```Java
// put code here
```

<a name="ex7"></a>
## Set current Gravatar to default userimage
Sets Gravatar to default Gravatar
```Java
// put code here
```

<a name="ex8"></a>
## Delete userimage
Deletes the userimage associated with any email addresses on the account
```Java
// put code here
```

<a name="ex9"></a>
## Get email hashes
Returns a map of emails and hashes
```Java
// put code here
```
