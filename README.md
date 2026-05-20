# Stealth Escape

> 2D stealth platformer built with Java and libGDX — collect items, avoid detection, escape the level.

**SDP Group Project · 2nd Year · Desktop (Java)**

---

## Team

| Name | GitHub                  |
|---|-------------------------|
| Yerkebulan Bissen | @yerk0shh               |
| Yernur Beisenbek | @Takahashi0021 |
| Akgul Daulet | @dauletovaakgul213-stack                        |

---

## About the Game

**Stealth Escape** is a 2D side-scrolling stealth platformer. The player infiltrates a guarded facility, collects all required items while avoiding guards, lasers, traps, and surveillance cameras, then escapes through the exit door.

- **Win:** Collect all items → reach the exit door
- **Lose:** Detected by a guard or camera, or touch a laser/trap

---

## Controls

| Key | Action |
|---|---|
| `A` / `←` | Move left |
| `D` / `→` | Move right |
| `W` / `↑` | Climb stairs up |
| `S` / `↓` | Climb stairs down |
| `H` | Hide |
| `P` / `Esc` | Pause / Resume |
| `Enter` / `Space` | Continue from Game Over |

---

## How to Run

Download `desktop-1.0.jar` from [Releases](../../releases/tag/v1.0.0) and run:

```bash
java -jar desktop-1.0.jar
```

> Requires Java 17+. No installation needed.

---

## How to Build from Source

```powershell
.\gradlew.bat clean
.\gradlew.bat desktop:dist
```

Output: `desktop/build/libs/desktop-1.0.jar`

---

## Project Structure

- `core/` — Game logic (player, enemies, screens, states)
- `desktop/` — Desktop launcher
- `assets/` — Sprites, texture atlases, animations
- `doc/` — UML class diagram, game flow, level sketch
- `GDD.pdf` — Game Design Document
- `TESTING.md` — Test checklist and bug log

---

## Documentation

| File | Description |
|---|---|
| [`GDD.pdf`](GDD.pdf) | Game Design Document |
| [`TESTING.md`](TESTING.md) | Manual test checklist + bug log |
| [`doc/classes.png`](doc/classes.png) | UML Class Diagram |
| [`doc/game-flow.png`](doc/game-flow.png) | Game Flow Diagram |
| [`doc/level-sketch.png`](doc/level-sketch.png) | Level Sketch |

---

## Design Patterns Used

| Pattern | Where |
|---|---|
| **State Machine** | `StateManager` + `State` — manages Menu, Game, Pause, GameOver, Win screens |
| **Single Responsibility** | `PlayerController` handles only input; `Player` handles only state |
| **Separation of Concerns** | `Assets`, `Collider`, `Camera` are isolated utility classes |

---

## Known Issues

- The game contains one playable level.