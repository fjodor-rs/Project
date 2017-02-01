# Report

## Description
In this game you play as Jaap, trying to free him from the game he was trapped him.
The levels can be solved as fast as possible, with the highest score, or just for fun.
You can check out how well you did compared to your friends or the whole world at the leadeboard.
There is also achievements you can get through out the game, which can also be viewed from the Main Menu.

## Technical Design
The application has two seperate parts: the android and the core.
This is done when you setup a project using LibGDX, so that you can easily design code that can be used for multiple platforms.
Almost all the application's code is  therefore in the core, the android part only contains the assets, values, and the AndroidLauncher class.

AndroidLauncher initializes the main game class Seeker and defines the methods used in the PlayServices interface.
In assets we have the images used for the controller, the textures and pack used for all the sprites, the music and soundeffects and finally a skin used for the button design.
Values contains ids.xml, which hold the unique ID's for the achievements and leaderboards that are used in AndroidLauncher.

In the core part of the application we start at the Seeker class.
This is the class that holds the static bits used for collision detection throughout the rest of the code.
It is also the only place where we make a SpriteBatch, because this is very resource heavy.
The batch is used in all screens that require sprite textures in the application.
We also load in the music used in the game into the AssetManager and finally set the first Screen, the Main Menu.

Main Menu has can show the Leaderboard or Achievement page using the methods in PlayServices, and can set the game to the PlayScreen.
In the PlayScreen we have the actual game. 
