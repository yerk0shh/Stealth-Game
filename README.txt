Stealth Escape - Quick Start

Team:
  Yerkebulan Bissen
  Yernur Beisenbek
  Akgul Daulet

Course:
  SDP Group Project, 2nd Year

Platform:
  Desktop Java / libGDX

Game Summary:
  Stealth Escape is a 2D stealth platformer where the player must collect all
  items, avoid guards, lasers, traps, and surveillance cameras, then reach the
  exit door to escape the level.

Win Condition:
  Collect all required items and reach the exit door.

Lose Condition:
  The player loses if detected by guards or cameras, or if they touch lasers
  or traps.

Controls:
  A / Left Arrow  - Move left
  D / Right Arrow - Move right
  W / Up Arrow    - Climb stairs up
  S / Down Arrow  - Climb stairs down
  H               - Hide
  P / Esc         - Pause or resume
  Enter / Space   - Continue from Game Over

Build:
  .\gradlew.bat clean
  .\gradlew.bat desktop:dist

Run:
  java -jar .\desktop\build\libs\desktop-1.0.jar

Project Structure:
  core/    - Main game logic
  desktop/ - Desktop launcher
  assets/  - Sprites and texture atlases
  doc/     - UML diagrams and level sketches

Included Documentation:
  README.md
  README.txt
  GDD.md
  GDD.pdf
  TESTING.md
  doc/classes.png
  doc/game-flow.png
  doc/level-sketch.png

Testing:
  The project was built with Gradle and tested as a runnable desktop JAR.

  Manually tested features:
  - Main menu
  - Player movement
  - Stair climbing
  - Hide mechanic
  - Pause menu
  - Item collection
  - Guard patrol system
  - Lasers
  - Surveillance cameras
  - Traps
  - Checkpoints
  - Game Over screen
  - Win screen

Known Issues:
  The game currently contains one polished playable level.

Submission Notes:
  Recommended runnable file:
  desktop/build/libs/desktop-1.0.jar

  Recommended GitHub Release tag:
  v1.0.0
