# About The Project
This Java 2D spaceship shooter game was developed by myself only for purpose of education and was written using Java only without the aid of a games engine. All assets were created by myself as shown in this readme.

# Game Description

There is no main menu in the game and will start straight away once launched. You can fly off the screen with a camera following you and can wrap around the game world once you reach the edges of the map which will put you on the opposite side. The map size is roughly 4 times the size of a 1920x1080 resolution screen and the camera will stop following the player once they come close to the edges.

A levelling system has been added which is shown on the user interface referred to as “waves” where enemy ships spawn quicker at the start of each wave and grow in numbers. There are two types of enemy ships: red and green. Red enemy ships only have 1 hit point and fire 1 bullet in one direction, however they are quicker and can shoot slightly faster compared to the green ships. Green ships maybe slower but they have 2 hit points and fire in 3 different directions. They also have a smaller spawn rate.

The player starts with 10 lives and when they have lost them all, the game will end. The player gains 10 points for destroying a red enemy ship and 20 for a green enemy ship. They will also lose 60 points if they lose a life.

## Key Bindings
- SPACE = Shoot
- 1 = Uses a Bomb pickup 
- 2 = Uses a Shield Pick up
- 3 = Uses a Speed Pick up
- 4 = Uses a Repair Pick u
- UP Arrow or W = Thrusts the player forward in the direction the player ship is facing.
- LEFT Arrow or A = Rotates the player ship anti-clockwise
- RIGHT Arrow or D = Rotates the player ship clockwise

# What are Pickups?
You start off with no pickups in the player inventory and must explore the game map in order to find and pick them up. Once the player moves over a pickup, it will get added to the player’s inventory. This is clearly displayed at the bottom of the screen view and displays the key bind to activate it and the total number of each pick up the player has. The maximum total per pickup type a player can hold at any time is 5.

# What each Pickup does
A pick up gives the player many special abilities or benefits as listed below.

## Bomb Pickups:
Creates a yellow barrier around the player that expands from the player’s location. It will follow the player while expanding and destroy any enemy ship or enemy bullet in its path. The player’s bullets and pickups do not get destroyed in the collision.

## Shield Pickup:
Creates a shield with 4 additional hit points which must be destroyed before the enemy can inflict damage directly to the player’s hit points. Shields do not stack; using a Shield pickup with a shield already on the player has no extra effect. When the player dies, they are automatically given a free shield for survival upon re-spawning which is activated immediately.

## Speed Pickup:
Temporary allows the player to move faster for a short duration. This is useful for escaping enemies in dangerous situations.

## Repair Pickup:
Replenishes the player ship’s hit points by three. However this should be used carefully because if used and the new player’s hit points crosses the maximum hit points limit of 5 (meaning the ship is at full health), then any other additional points gained after that will be lost. For example, if the player had 4 hit points and used it, their new hit points remaining would be 5 and not 7.

# Design Choices
I decided to control the game levelling system with three main classes: LevelManager, SpawnManager, and Spawner. This had to be tuned carefully in order to get a smooth transition from level to level. The SpawnManager class controls how many objects of each different type are allowed to be in the game at any given time and runs an update after pre-defined timer had been reached which controls the level and the limits of each object type. By handling the limits of game objects allowed on the game map, I could integrate this into a levelling system where the limits could increase as the game progresses in levels.

I chose to use a strong object orientated hierarchy in my class design for all visible game objects that can be interacted with by having a GameObject class where all objects were stored and a large depth of other sub classes extend it to become more specific. 

These include:
-	Ship (Abstract class)
  - EnemyShip  (Abstract class)
    -	Tier1Ship
    -	Tier2Ship
  -	PlayerShip
-	PickUp  (Abstract class)
  -	BombPickUp
  -	ShieldPickUp
  -	SpeedPickUp
  -	RepairPickUp
-	Bullet

## Ship Assets
![PlayerShip2](https://github.com/Mayron/Java-2D-Space-Shooter-Game/assets/5854995/a2f2ded2-6c70-48f6-b5dc-804a626028f7)
![T1EnemyShip2](https://github.com/Mayron/Java-2D-Space-Shooter-Game/assets/5854995/df06b480-ce65-44e1-9c8b-e11a0f47820a)
![T2EnemyShip2](https://github.com/Mayron/Java-2D-Space-Shooter-Game/assets/5854995/ae9f12f8-f0e7-41be-ac29-3a46f7263883)

This allowed me to re-use a lot of code and use abstract methods to avoid any errors which proved to save me a lot of time while debugging. I linked together (in a “has” relationship) an inventory object with the player so that when the player receives an object from the game world through the collision detection system, they could store it in the player’s inventory.

I chose to do this because it made storing all pickup’s easier as when a pickup was marked as “dead” and removed from the game object list, preventing it from appearing on the game map, I still wanted to keep a reference to it so that I knew its instance variables and use its “use” method. It seemed very unnecessary to make a whole new class from the same type of object that took care of what happens after the object was picked up. I implemented the inventory system with a HashMap which stored a String as a key used to identify the Pickup type, and a Stack for the number of Pickups of that type stored in the inventory. A Stack seemed like the most useful and appropriate type of Java collection available to use as I could pop the Pickup when the player wanted to use one and then push a Pickup when the player received one.

The camera stops following the player when they reach close to the map edge so I could still use a wrapped game world where the player leaves from one side and comes out the opposite side. This decision was made only to avoid having to reflect the game objects to create the illusion of a continuous map. The other reason was due to using Photoshop for the background which would not have looked consistent if the player left from one side and saw the other side of the map instantly afterwards because one edge of an image would not align well with another. There are other methods I could have done to prevent this issue but I felt the one I picked works very nicely. I used 4 separate background images joined together and controlled in the View class because originally I had 1 massive image (the size of 4 1920x1080 screens) and placed it in the game world. This was simpler but I noticed a big drop in image quality so I used 4 smaller images instead.

# Tuning the Game
I spent many hours testing parameters to get the levelling flow enjoyable and smooth. I wanted to make sure the difficulty increased at a steady pace per level by making sure pickups would increase in number with the enemy ships per level but also have a game limit so that if the player reached a high level, they would not flood the screen. Giving pickups a maximum limit ensured that eventually the game would get harder as enemy ships could overtake the number of possible pickup spawns. I also tested increasing ship speeds and bullet firing rates. Each type of ship, including the player ship, has different speeds and firing styles. The player has the advantage of being able to fire a lot quicker as I chose to have the firing rate shorter to make the player feel more powerful. AI enemies should always be less powerful to make the game playable while also offering a challenge. 

I added some random number generation to make the ships move in less predictable directions. The enemy ships will chase the player only when in the detection range and will strafe in different directions when they reach the maximum distance from the player to avoid unnecessary enemy to player ship collision. This avoids the problem of making the enemy ships feel as though they were tired to the player with a rope. 
I added an array to represent a pattern for the number of spawns per spawn cool down reset controlled by the Spawner objects. This allowed me to spawn 5 objects at the start of a wave/level and less as the wave/level progressed. This added a noticeable change in levels to make the player excited for the next wave. I added a voice sound effect which counts down in a robot voice for the last 5 seconds before a new wave begins to complement this exciting transition.

# Future Improvements
There are many things I wish I had more time to implement and I will do in the future because I have enjoyed this project a lot. I would like to have made a key binding menu for users to alter to their preference as well as the ability to tweak game variables in a menu. I would also like to include a pause option and add other interesting game features such as a worm hole to transport the user to other areas on the map and a boss enemy ship. I was hoping to create a Player health bar to represent the player’s hit points rather than a small number which is hard to read on top of the player ship. I could also do the same for enemy ships by anchoring their health bars above the ship as they move. 
