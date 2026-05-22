================================================================
  STEALTH ESCAPE v1.0.0
  2D stealth platformer -- collect items, avoid detection, escape
================================================================

TEAM
  Yerkebulan Bissen
  Yernur Beisenbek
  Akgul Daulet

COURSE
  DP Group Project

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
  H                Hide / Unhide
  P / Esc          Pause / Resume
  Enter / Space    Continue from Game Over

----------------------------------------------------------------
HOW TO RUN
----------------------------------------------------------------
  java -jar desktop-1.0.jar

  No installation needed.

----------------------------------------------------------------
HOW TO BUILD FROM SOURCE
----------------------------------------------------------------
  .\gradlew.bat clean
  .\gradlew.bat desktop:dist

  Output: desktop/build/libs/desktop-1.0.jar

----------------------------------------------------------------
PROJECT STRUCTURE
----------------------------------------------------------------
  core/        Game logic (player, enemies, screens, states)
  desktop/     Desktop launcher
  assets/      Sprites, texture atlases, animations
  doc/         UML class diagram, game flow, level sketch
  screenshots/ Gameplay screenshots
  GDD.pdf      Game Design Document
  TESTING.md   Test checklist and bug log
  README.md    Full documentation (Markdown)

----------------------------------------------------------------
DESIGN PATTERNS USED
----------------------------------------------------------------
  State Machine         StateManager + State classes manage
                        Menu, Game, Pause, GameOver, Win screens

  Single Responsibility PlayerController handles only input;
                        Player handles only player state

  Utility Classes       Assets, Collider, Camera are isolated
                        helper classes

----------------------------------------------------------------
SOLID PRINCIPLES
----------------------------------------------------------------
  S  Single Responsibility
       PlayerController handles input only.
       Player handles state only. Separate classes, separate jobs.

  O  Open / Closed
       State is abstract. New screens extend it without
       modifying existing code.

  L  Liskov Substitution
       StateManager.push(State) accepts any State subclass
       interchangeably.

  I  Interface Segregation
       Renderable and Updatable are separate interfaces.
       Guard implements Renderable only; Player implements both.

  D  Dependency Inversion
       GameManager depends on the State abstraction via
       StateManager, not on concrete screen classes.

----------------------------------------------------------------
DOCUMENTATION
----------------------------------------------------------------
  GDD.pdf             Game Design Document
  TESTING.md          Manual test checklist + bug log
  doc/classes.png     UML Class Diagram
  doc/game-flow.png   Game Flow Diagram
  doc/level-sketch.png Level Sketch

----------------------------------------------------------------
KNOWN ISSUES
----------------------------------------------------------------
  Currently ships with one fully designed playable level;
  additional levels are planned.

================================================================
  GitHub Release: v1.0.0
  Run file:       desktop-1.0.jar
================================================================