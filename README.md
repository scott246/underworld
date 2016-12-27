# Underworld
The game is located in releases (as an executable jar file).

# Gameplay
Currently the graphics are very basic.
The game is a retro-style 2D RPG. The point of the game is to move the blue circle (you) around and destroy all the red squares
(enemy) by bumping into them or shooting them with arrows or magic. You may visit the green square (store) and buy upgrades with gold.
Gold is found through yellow squares (powerups), which produce not only gold but other upgrades as well, marked with a "G" for gold, or
another character for another type of powerup (see powerups section).

# HUD
The game's HUD is located mostly in the top of the screen, with a few elements in the lower portion. Currently it tracks the time you've
lived, your characters HP level (health), your attack and defense magic, your mana, your damage range, your number of arrows, and your gold.
On the bottom, there's a notification that says you can press q to display instructions, and tracks the number of enemies killed.

# Your Character
You are a blue circle. You have a gray number on your character which represents your HP level. You can move with WASD, shoot arrows with
IJKL (requires a bow before you can use), and use attack and defense magic with N and M, respectively (once you have it). You can also 
press R to restart the game and Q to display instructions.

# Enemies
Enemies are a red square. They have a black number which represents their respective HP levels. They move randomly and drain your HP when 
you bump into them. You can attack them by bumping into them (which reduces your HP level as well as theirs) or by shooting arrows at them
or by using attack magic. The amount of damage you do attacking them is based on your level, but a randomly generated number. Once killed, 
they will drop your level in gold (i.e. if you are level 3 they will drop 3 gold) and you will receive a random health bonus.

# Store
You start on a green square, which is the store. Here you can buy HP, mana, arrows, magic, a bow (which you only need to buy once), or an 
upgrade to your minimum and maximum damage. Enemies cannot enter the store, therefore, you can use this space to get away if you need a 
temporary hiding spot.

# Powerups
Powerups are randomly generated throughout the map as yellow squares. They can give you upgrades to your health (marked with an H), mana 
(M), minimum damage (-), maximum damage (+), or gold (G). The amount of upgrade is random, but based on your level.

# Magic
Pressing the attack magic button (N) requires 20 mana and at least 1 attack magic for anything to happen. Fulfilling the requirements, it
will attack anything within three square blocks of you for your maximum damage, i.e. having a max damage of 15 will cause anything within 
three blocks of you to lose 15HP. Defense magic (M) requires 10 mana and at least 1 defense magic. Having that will cause your player to 
gain 30 times your level in health.

# Good luck!
