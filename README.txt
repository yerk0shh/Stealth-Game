================================================================
  STEALTH ESCAPE v1.0.0
  2D stealth platformer — collect items, avoid detection, escape
================================================================

TEAM
  Yerkebulan Bissen
  Yernur Beisenbek
  Akgul Daulet

COURSE
  SDP Group Project, 2nd Year

PLATFORM
  Desktop Java / libGDX

----------------------------------------------------------------
ABOUT THE GAME
----------------------------------------------------------------
Stealth Escape is a 2D stealth platformer. The player infiltrates
a guarded facility, collects all required items while avoiding
guards, lasers, traps, and surveillance cameras, then escapes
through the exit door.

  Win:  Collect all items and reach the exit door
  Lose: Detected by a guard or camera, or touch a laser/trap

----------------------------------------------------------------
CONTROLS
----------------------------------------------------------------
  A / Left Arrow   Move left
  D / Right Arrow  Move right
  W / Up Arrow     Climb stairs up
  S / Down Arrow   Climb stairs down
  H                Hide
  P / Esc          Pause / Resume
  Enter / Space    Continue from Game Over

----------------------------------------------------------------
HOW TO RUN
----------------------------------------------------------------
  java -jar desktop-1.0.jar

  Requires Java 17+. No installation needed.

----------------------------------------------------------------
HOW TO BUILD FROM SOURCE
----------------------------------------------------------------
  .\gradlew.bat clean
  .\gradlew.bat desktop:dist

  Output: desktop/build/libs/desktop-1.0.jar

----------------------------------------------------------------
PROJECT STRUCTURE
----------------------------------------------------------------
  core/       Game logic (player, enemies, screens, states)
  desktop/    Desktop launcher
  assets/     Sprites, texture atlases, animations
  doc/        UML class diagram, game flow, level sketch
  GDD.pdf     Game Design Document
  TESTING.md  Test checklist and bug log
  README.md   Full documentation

----------------------------------------------------------------
DESIGN PATTERNS USED
----------------------------------------------------------------
  State Machine         StateManager + State classes manage
                        Menu, Game, Pause, GameOver, Win screens

  Single Responsibility PlayerController handles only input,
                        Player handles only player state

  Separation of Concerns
                        Assets, Collider, Camera are isolated
                        utility classes

----------------------------------------------------------------
KNOWN ISSUES
----------------------------------------------------------------
  The game currently contains one polished playable level.

================================================================
  GitHub Release: v1.0.0
  Run file: desktop-1.0.jar
================================================================