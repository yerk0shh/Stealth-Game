# Testing Checklist and Bug Log

## Manual Test Checklist

| Area | Test | Expected Result | Status |
| --- | --- | --- | --- |
| Build | Run `.\gradlew.bat desktop:dist` | Build succeeds and creates `desktop/build/libs/desktop-1.0.jar` | ✅ Passed |
| JAR manifest | Run `java -jar desktop/build/libs/desktop-1.0.jar` | The manifest points to `com.despat.stealth.desktop.DesktopLauncher` | ✅ Passed |
| Main menu | Click Play | First level starts | ✅ Passed |
| Player movement | Press A / D or Arrow Left / Right | Player walks left and right | ✅ Passed |
| Stair climbing | Walk onto stairs, press W / S or Arrow Up / Down | Player climbs up and down stairs | ✅ Passed |
| Hide mechanic | Stand in hiding spot, press H | Player hides; guards and cameras no longer detect player | ✅ Passed |
| Item collection | Walk over all collectible items | Counter increments; exit door activates when all items collected | ✅ Passed |
| Guard patrol & detection | Walk into guard's collision zone while not hiding | Guard detects player; DYING state triggers → Game Over screen | ✅ Passed |
| Guard sound detection | Create a sound wave near a guard | Guard changes patrol destination toward the sound source | ✅ Passed |
| Camera detection | Enter camera's detection zone while not hiding | Camera detects player; DYING state triggers → Game Over screen | ✅ Passed |
| Laser hazard | Touch an active laser | DYING state triggers → Game Over screen | ✅ Passed |
| Switch mechanic | Interact with switch | Laser linked to switch turns OFF | ✅ Passed |
| Checkpoint | Walk through a checkpoint | `currentCheckpoint` updates; player respawns here on death | ✅ Passed |
| Game Over screen | Die to any hazard | Game Over screen appears with restart option | ✅ Passed |
| Win condition | Collect all items, then reach the exit door | Win screen appears | ✅ Passed |
| Pause menu | Press P or Escape during gameplay | Pause screen opens; resuming returns to game correctly | ✅ Passed |
| Clean machine test | Run JAR on a machine without dev tools installed | Game launches and plays without errors | ✅ Passed |

## Fixed Bugs

| Title | Steps | Expected | Actual | Severity | Fix |
| --- | --- | --- | --- | --- | --- |
| Runnable JAR cannot find main class | Build with `desktop:dist`, then run the JAR | Desktop launcher starts | Manifest referenced old `com.patriots.simov` package | High | Updated `desktop/build.gradle` main class to `com.despat.stealth.desktop.DesktopLauncher`. |
| Player can respawn at invalid default point | Get caught before touching a checkpoint | Player respawns at first checkpoint | Default checkpoint was `(0, 0, 0)` | Medium | Initialized `currentCheckpoint` to checkpoint 1. |
| Unsafe item removal during collection | Collect an item while iterating item list | Item is removed safely | Enhanced for-loop removed from list directly | Medium | Replaced with `Iterator.remove()`. |
| Hidden invulnerability toggle left in final build | Hold click on player during gameplay | Hazards always cause Game Over | Debug cheat could toggle `disableGameOver` | High | Removed the cheat toggle and final-build indicator. |
| Resource cleanup incomplete | Leave gameplay state | Textures, atlases, renderers, and fonts are disposed | Several gameplay resources were not released | Low | Added cleanup for `Assets`, renderers, fonts, and exit atlas. |
| Java naming inconsistency in player input controller | Review `PlayerController` API | Public controller methods use Java-style lower camel case | `Update`, `Draw`, and `ispressRight` stood out | Low | Renamed to `update`, `draw`, and `isPressRight`. |
| Diagrams only available as PlantUML source | Submit diagrams to instructor | Diagrams are available as PNG/PDF-style artifacts | Only `.puml` source files were present | Medium | Added `classes.png`, `game-flow.png`, and `level-sketch.png`. |

## Remaining Notes

- Full playthrough tested on a second Windows machine before submission.
- Classmate playtesting session conducted to catch usability issues not visible to developers.
- Game released as GitHub Release v1.0.0 — JAR available at the repository releases page.
