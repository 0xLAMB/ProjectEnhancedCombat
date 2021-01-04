# Project Enhanced Combat

An attempt at making combat fun.

## Info

Melee directions

```
     UP
   
    STAB
   
LEFT    RIGHT
```

Core ideas for mechanics. Needs play testing.

- target locking
- stamina
    - attacking and blocking drains stamina
        - favor attacks
    - losing all stamina staggers
- sU, sL and sR stance
    - i.e. UP, LEFT and RIGHT stance
- stance automatically blocks attack from that direction
    - eg. UP stance (sU) blocks all attacks from up (aU)
    - ofc enemy LEFT is your RIGHT and vice-versa
- stance also automatically blocks directionless (vanilla) attacks (aX)
- parry
    - briefly after changing stances you parry attacks from the same direction
    - successful parry forces enemy stance to no stance (sX) and immediate attack becomes riposte, which is unblockable (but parriable!)
    - during parry window, you also block projectiles
    - too fast parry is normal blocking
    - too slow parry is considered no stance (you take damage)    
- parry "race" attack multiplier?
    - parry-riposte 1x, parry-parry-riposte 1.5x, parry-parry-parry...-riposte 2x
- stabbing
    - mF + M1 -> aS, ie. moving forward and attacking performs stab (if weapon allows, otherwise perform directional attack)
    - only blockable by shield
    - any perfectly timed stance change is parry
- unblockable attacks
    - can only be parried or dodged
- stop attacks
    - can be used to feign attacks
- backstep

Modifications to base game

- delayed attacks
- increased damage to compensate slower paced combat
- new elite enemies with better combat AI


### Vanilla Weapons

#### Sword

Versatile and fast weapon

a[ULRS]

#### Axe

Fast heavy hitter

a[ULR]

- HACK AWAY (U): aU,aU,aU*
- HACK AWAY (L): aL,aL,aL*
- HACK AWAY (R): aR,aR,aR*

#### Pickaxe

Slow heavy hitter

a[ULR]

- JUST MINING: aU, aU*

#### Spade

Fast blunt weapon

a[ULRS]

#### Hoe

Wtf are you doing

a[ULR]

- REAP WHAT YOU SOW (L): aL, aL, aU*
- REAP WHAT YOU SOW (R): aR, aR, aU* 


### Tinker's Construct Weapons

`TODO`

### Abbreviations

    sU = UP stance
    sL = LEFT stance
    sR = RIGHT stance

    aU = UP attack
    aL = LEFT attack
    aR = RIGHT attack
    aS = STAB attack
    aX = direction-less attack

    * = unblockable


## Development

Forge's original README.txt

```
-------------------------------------------
Source installation information for modders
-------------------------------------------
This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access 
to some of the data and functions you need to build a successful mod.

Note also that the patches are built against "unrenamed" MCP source code (aka
srgnames) - this means that you will not be able to read them directly against
normal code.

Source pack installation information:

Standalone source installation
==============================

See the Forge Documentation online for more detailed instructions:
http://mcforge.readthedocs.io/en/latest/gettingstarted/

Step 1: Open your command-line and browse to the folder where you extracted the zip file.

Step 2: You're left with a choice.
If you prefer to use Eclipse:
1. Run the following command: "gradlew genEclipseRuns" (./gradlew genEclipseRuns if you are on Mac/Linux)
2. Open Eclipse, Import > Existing Gradle Project > Select Folder 
   or run "gradlew eclipse" to generate the project.
(Current Issue)
4. Open Project > Run/Debug Settings > Edit runClient and runServer > Environment
5. Edit MOD_CLASSES to show [modid]%%[Path]; 2 times rather then the generated 4.

If you prefer to use IntelliJ:
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Run the following command: "gradlew genIntellijRuns" (./gradlew genIntellijRuns if you are on Mac/Linux)
4. Refresh the Gradle Project in IDEA if required.

If at any point you are missing libraries in your IDE, or you've run into problems you can run "gradlew --refresh-dependencies" to refresh the local cache. "gradlew clean" to reset everything {this does not affect your code} and then start the processs again.

Should it still not work, 
Refer to #ForgeGradle on EsperNet for more information about the gradle environment.
or the Forge Project Discord discord.gg/UvedJ9m

Forge source installation
=========================
MinecraftForge ships with this code and installs it as part of the forge
installation process, no further action is required on your part.

LexManos' Install Video
=======================
https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be

For more details update more often refer to the Forge Forums:
http://www.minecraftforge.net/forum/index.php/topic,14048.0.html
```

Documentation, guidelines and useful stuff

- <https://mcforge.readthedocs.io/en/1.16.x/>
- <https://forge.yue.moe/javadoc/1.16.3/>
- <https://forums.minecraftforge.net/topic/61757-common-issues-and-recommendations/>
