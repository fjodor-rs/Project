# Report

## Description
In this game you play as Jaap, trying to free him from the game he was trapped him.
The levels can be solved as fast as possible, with the highest score, or just for fun.
You can check out how well you did compared to your friends or the whole world at the leadeboard.
There is also achievements you can get through out the game, which can also be viewed from the Main Menu.

## Technical Design

### Main Parts
The application has two seperate parts: the android and the core.
This is done when you setup a project using LibGDX, so that you can easily design code that can be used for multiple platforms.
Almost all the application's code is  therefore in the core, the android part only contains the assets, values, and the AndroidLauncher class.

### Android
AndroidLauncher initializes the main game class Seeker and defines the methods used in the PlayServices interface.
In assets we have the images used for the controller, the textures and pack used for all the sprites, the music and soundeffects and finally a skin used for the button design.
Values contains ids.xml, which hold the unique ID's for the achievements and leaderboards that are used in AndroidLauncher.

### Main Game Class
In the core part of the application we start at the Seeker class.
This is the class that holds the static bits used for collision detection throughout the rest of the code.
It is also the only place where we make a SpriteBatch, because this is very resource heavy.
The batch is used in all screens that require sprite textures in the application.
We also load in the music used in the game into the AssetManager and finally set the first Screen, the Main Menu.

### Screens
In the screen package we start with Main Menu. Main Menu has can show the Leaderboard or Achievement page using the methods in PlayServices, and can set the game to the PlayScreen.
In the PlayScreen all the textures and sprites are loaded in, updated and rendered. it follows the main sprite and handles all input that controls the sprite's movement. It also handles the calls to other screens in situations that the game is won or lost.
The screens that display when this happens simply let you replay, return to Main Menu or show the Leaderboard.

### Tools
In the tools package we start with the B2WorldCreator which creates all the objects made in Tiled. for every object layer made in Tiled a body and fixture is created. The object is also assigned a bit for collision purposes.
the Controller class creates button images and inputlisteners to check if a button is pressed, which we can then use in the PlayScreen to move our sprite. It uses tables and a stage to show the buttons on the PlayScreen.
The WorldContactListener is called when two objects come in to contact. It uses the bites assigned to the objects to decide what happens when they come into contact. It is also used to implement what happens when to objects end their contact.

### Sprites
All sprites have a body created in the world and a fixture attached to that body which is assigned either a texture or an animation.
A sprite can only have a single texture or a whole scala of animations and textures depending on its fucntion and uses. When the body is created it is also assigned a position, but this can be overwritten in another method if neccessary. The fixture has a specific and size and assigns the sprites bit and which bits the sprite can collide with. One sprite can have multiple fixtures with different purposes. For example to detect if Jaap has hit something with his head there is a specific fixture at his heads location. This fixture has its own bit and will therefore execute a method that only happens when Jaap hits certain things with his head.

#### Enemies
The sprites package contains three different packages, starting with the enemies sprite package.
All enemies use the abstract class Enemy as a base for their structure, to make it easier to add new enemies.
The Goomba and Turtle class both make a specific enemy that can be added as an object in Tiled and will then be created at that position by the B2WorldCreator class. They use the methods of their abstract class, but specific to their function and design.

#### Tile Objects
in the tileobjects we have all our tiles that have unique interaction with the player, instead of being just a static body with which the player can collide. We start off again with a abstract class; InteractiveTileObject is the base for all tiles that have different interactions. Brick, CoinBlock and EndBlock all extend InteractiveTileObject and have their own bit and onHeadHit method.

#### Items
Items also has an abstract class, in this case called Item, that sets the basics for what an item should have. Items also have an ItemDef that is used to decide the position and type of an item when it is spawned. This spawning is done in PlayScreen and makes sure the correct item is spawned at the correct position. Coin and Hulkifier are the two items in the game right now. Where hulkifier has its own velocity, coin does not. Hulkifier also hulks out Jaap when it is used, but more on that in the Jaap section.

#### Jaap
The sprite the player controls is called Jaap, which is by far the most complex sprite. 



