# Spigot Plugin Library

## Maven 
````xml
<repositories>
    <repository>
        <id>pierre-nexus</id>
        <url>http://repo.pschwang.eu/nexus/content/repositories/snapshots/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>de.pierreschwang</groupId>
        <artifactId>spigot-lib</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
````

## Getting started
### Create the base plugin

````java
public class ExamplePlugin extends AbstractJavaPlugin<ExampleUser> {

    public void onEnable() {
        super.onEnable(); // Important, the super onEnable initializes the library
        setUserFactory((player) -> new ExampleUser(this, player)); // Set the user factory
    }   
    
    // Test method to show some utilities
    public void sendMessageToUser(Player player) {
        getUser(player).sendMessage("your-locale-key", param1, param2); // Sends a localized message to the user
    } 

}
````

````java
/**
  * You have to create your own implementation of the user for your plugin. 
  * Inside this object you can store custom data which may be needed and also have access to methods from the base user.
  */
@Getter
public class ExampleUser extends User {

    private int customUserData;
    private String evenMoreCustomData;

    public BedwarsPlayer(ExamplePlugin plugin, Player player) {
        super(plugin, player);
    }

}
````

## Localization
This api supports also the use of localized messages - That means, the player is able to retrieve message in the language of his client.
To start, simple create a folder called `lang` under src/main/resources. Inside this folder create your language files, e.g. `en_US.properties` or `de_DE.properties`.
<br />
An example language file might look like this:
````properties
prefix = MyCoolPlugin >
test-message = %prefix% &6Hello {0}
````
The ``%prefix`` is the only placeholder which gets replaced. The `{0}` defines parameters, which are passes in the `sendMessage()` method.


## ToDo
 + More Documentation incl Hosted Javadocs
 + Querybuilder (simple database operations)
 + Achievements
 + ConnectionProvider
 + NPC Library
 + Stats
 + Title, Subtitle, Actionbar