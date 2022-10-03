# Gravatar Java Client

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.hayes-roach/gravatar-java-client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.hayes-roach/gravatar-java-client)

This is a java implementation for the Gravatar XML-RPC API: https://en.gravatar.com/site/implement/xmlrpc/
 
# Getting Started
Gradle:
```groovy
testCompile "io.github.hayes-roach:gravatar-java-client:1.0.3"
```
Maven:
```xml
<dependency>
  <groupId>io.github.hayes-roach</groupId>
  <artifactId>gravatar-java-client</artifactId>
  <version>1.0.3</version>
</dependency>
```

## Initialization
```Java
GravatarClient gravatarClient = new GravatarClient("gravatar@email.com", "password");
```

# Example Usage
```java
GravatarClient client = new GravatarClient("gravatar@email.com", "gravatar_password");

// checks if an email address has a Gravatar
Boolean exists = client.gravatarExists("grav@mail.com");

// gets the list of emails associated with the account
List<String> emails = client.getEmails();

// gets the current active Gravatar for the account
Gravatar gravatar = client.getCurrentGravatar("gravatar@email.com");

// returns a list of all of the available Gravatars associated with the account
List<Gravatar> allGravatars = client.getAllGravatars();

// Uploads a Gravatar to the account. Can upload using a Base64 String, File, InputStream or URL
Gravatar uploadedGravatar = client.upload(new URL("https://play-lh.googleusercontent.com/ZvMvaLTdYMrD6U1B3wPKL6siMYG8nSTEnzhLiMsH7QHwQXs3ZzSZuYh3_PTxoU5nKqU"), Rating.PG);

// Sets a Gravatar on the account as the active gravatar
Boolean gravatarIsSet = client.setGravatar(uploadedGravatar);

// Removes the active Gravatar for the account, and a default image will be used instead.
client.removeActiveGravatar();

// Deletes a Gravatar from an account
client.deleteGravatar(uploadedGravatar);
```
