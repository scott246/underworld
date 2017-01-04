# Underworld
The game is located in releases (as an executable jar file).

# Initial Setup
The game has a launcher once it is first started up (by double clicking on the .jar file), which allows you to choose options such as:
- resolution (change the screen resolution of the game)
  - note: the smaller screen size makes the game a little harder, but lets you level up faster
- torch mode (entire screen is black except the three block radius where you are)
  - provides a bit more challenge and strategy
- terrain (add randomly generated rocks throughout the level)
  - otherwise it's an open level

# Gameplay
The game is a retro-style pixel 2D RPG. The point of the game is to move the blue square (you) around and destroy all the red squares (enemy) by attacking (bumping into them or shooting them with arrows or magic). You may visit the green square (store) and buy upgrades with gold. Gold is found through yellow squares (powerups), which produce not only gold but other upgrades as well, marked with a "G" for gold, or another character for another type of powerup (see powerups section). The point of the game is to clear all enemies from a level using whatever means necessary (magic, basic attack, arrows, etc.). Clearing all enemies will take you to a completely different randomly generated level with more and more powerful enemies with greater rewards upon killing them, and more powerful powerups.

# HUD
The game's HUD is located mostly in the left side of the screen, with a few elements in the lower and upper portions. Currently it tracks 
- your level
- the number of enemies remaining
- health (HP) - red bar
- mana - blue bar
- damage range
- gold (GP)
- store menu (if you're on the store square)
- attack and defense magic
- time alive
- enemies killed

During certain interactions, some text may pop up next to your character. These are errors, such as when you're trying to purchase something but don't have enough gold, trying to use magic without enough mana, trying to collect more than 100 HP/mana, etc.

# Your Character
You are a blue square. You have a number on your character which represents your HP level. You can move with WASD, shoot arrows with IJKL (requires a bow before you can use), and use attack and defense magic with N and M, respectively (once you have the magic and enough mana). You can also press R to restart the game and Q to display instructions.

# Enemies
Enemies are a red square. They have a number which represents their respective HP levels. They move randomly; you can attack them by bumping into them (which reduces your HP level as well as theirs) or by shooting arrows at them or by using attack magic. The amount of damage you do attacking them is a randomly generated number between your min and max damage. The amount of damage you take when attacking is a randomly generated number based on the enemy's HP (an enemy with 50HP can do anywhere from 0-50 damage to your character) if you bump into them -- using attack magic or archery will not cause your HP to fall. Once an enemy is killed, you will receive a gold bonus (i.e. if you are level 3 they will drop up to 3 gold upon death) and you will receive a random health bonus (random number * your level * 10).

# Store
You start on a green square, which is the store. Here you can buy HP, mana, arrows, magic, a bow (which you only need to buy once), or an upgrade to your minimum and maximum damage. Enemies cannot enter the store, therefore, you can use this space to get away if you need a temporary hiding spot.

# Powerups
Powerups are randomly generated throughout the map as yellow squares. They can give you upgrades to your:
- health (marked with an H) 
- mana (M)
- minimum damage (-)
- maximum damage (+)
- gold (G)

The amount of upgrade is random, but based on your level.

# Magic
Pressing the attack magic button (N) requires 20 mana and at least 1 attack magic for anything to happen. Fulfilling the requirements, it will attack anything within three square blocks of you for your maximum damage, i.e. having a max damage of 15 will cause anything within three blocks of you to lose 15HP. Defense magic (M) requires 10 mana and at least 1 defense magic; using that will cause your player to gain (30 * your level) in health.

# Good luck!
