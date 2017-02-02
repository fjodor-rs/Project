# **Jaap's Adventure**

## Problem

This app will function as an easy way of relieving stress and easing boredom.
The user will play as Jaap controlling him and moving him across the screen towards the goal,
trying to get there as fast as possible, or gaining as high a score as possible.

## Screenshots

### Main Menu

![](doc/Main Menu.png)

### Leaderboard

![](doc/Leaderboard.png)

### Achievements

![](doc/Achievements.png)

### Game Over

![](doc/Game Over.png)

## Features

Features of this application are a Main Menu screen, the game interface itself, a Leaderboard, achievements section and Game Over or Game Won screen.
The game interface will contain obstacles and a sprite, the user can move the sprite and the screen will follow throughout the level.
Possible additions I wanted to implement in the application will be discussed at the end of the document.

## Sources

The app was build using the LibGDX Java game development framework made by Badlogic Games.
The levels, using sprites and tiles, were created using Tiled.
The app also uses Tiled to make specific tiles into object for collision purposes.
Authentication and the Leaderboard are done using Google Play Developer Console.

For the mario-style tilesheet I used an image downloaded from spriters-resource.com.
I could not find if there was a license to use these tiles.

## Guides

I learned the basic concepts and code documentation by using Marc Aurelie's guide on YouTube on how to create a Super Mario Bros game using LibGDX:
https://www.youtube.com/watch?v=a8MPxzkwBwo

## Separation of Concerns

All the game related code is in the core part of the app, created using libGDX.
Only the AndroidLauncher and the game's assets are in the android part of the app.

In core the app we used a package called scenes for all the classes that contain the labels placed over the game's interface.
Core also contains a package called screens which will contain the different playscreen classes for different levels and other screens we will be using.
The last package, sprites, will contain the classes concerning the player's sprite, enemies and items.
Finally core will contain a class that will extend the game, which will be used to create, render and dispose of the screens used.

The android part of the app has methods used for the Leaderboard and Achievements in the AndroidLauncher class, where it also launches the game.

## Application Programming Interface

The Google Play Services API is used to implement the Leaderboard and Achievements section.
## Problems at the Beginning

A problem that might arise is the lack of tilesets and sprites.
Although I have already founded a great set of tiles, I noticed there is a lack of high quality tilesets and sprites.
This doesn't mean they don't exist or aren't there, it will just be hard to find a very specific style, if I ever wanted to switch or expand.

Secondly while LibGDX does take care of all the low-level details, it takes time and practice to understand its documentation, making it harder to estimate the amount of time necessary to complete certain aspects of the project. Luckily there is a multitude of tutorials on the subject on the internet, which will help solving this problem.

## Minimum Viable Product

The basis of this application will be a start screen and a working first level; playable and bugless.
Expansion of the app would focus on more levels, different difficulties, a high score database and authentication.
If the first level is working and playable, the next implementations would be the authentication and scoreboard.
After that the focus would be on creating more levels to play and creating multiple ways to play a level.

*Copyright (c) 2017 Fjodor van Rijsselberg*

*Made by Fjodor van Rijsselberg*
