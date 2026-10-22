# Game Character Manager

A Java Swing program where you can create, view, search, update, and remove game characters, then watch them take turns in a mini battle log.

---

## What You Need

- **Java JDK 8 or newer** (the program uses Java Swing, which comes with Java)
- Any Java IDE (Eclipse, IntelliJ, NetBeans, VS Code) **or** just a terminal

---

## Files in This Project

All files belong to the package `juvinne`.

| File | What it does |
|---|---|
| `Main.java` | Starts the program and opens the window |
| `GameFrame.java` | The window: buttons, forms, list, and battle log |
| `AvatarPanel.java` | The animated character card (glow, health bar, emblem) |
| `GameCharacter.java` | The abstract "blueprint" that every character follows |
| `Attacker.java` | Character type: high damage |
| `Defender.java` | Character type: shields |
| `Assassin.java` | Character type: critical hits |
| `Healer.java` | Character type: heals itself |
| `CharacterManager.java` | Stores and manages all characters (List, Stack, Queue) |

---

## How to Run

### Option A: Using an IDE (easiest)

1. Create a Java project.
2. Inside the `src` folder, create a package named **`juvinne`**.
3. Put all 9 `.java` files inside that package.
4. Open `Main.java`, then click **Run**.
   - Eclipse: right-click `Main.java` → *Run As* → *Java Application*
   - IntelliJ: click the green arrow next to `main`

> **If your imports show errors:** check that there is no `module-info.java` file in `src` (delete it, or add `requires java.desktop;` inside it), that the files are inside the `juvinne` package, and that your project has a JDK selected.

### Option B: Using the terminal

Open a terminal in the folder that **contains** the `juvinne` folder, then run:

```
javac juvinne/*.java
java juvinne.Main
```

---

## How to Use the Program

When the window opens, you will see 4 starter characters: **Ghost**, **Urban Tracker**, **Templar**, and **Medic**.

### View a character
Click a name in the **Roster** (left side). The center card shows its type, ability, health, and power.

### Search
Type in the search box above the roster. The list filters as you type. You can search by **name** (`gh`) or by **type** (`assassin`). Click **X** to clear.

### Create a character
1. Type a name in the **Character Editor** (right side).
2. Pick a type.
3. Set Health (1–200) and Power (1–100).
4. Click **Add New Character**.

### Update a character
Click the character, change its name, health, or power, then click **Update Selected**.
*(The type cannot be changed. To get a different type, add a new character and remove the old one.)*

### Remove and undo
Click **Remove Selected** and confirm. Changed your mind? Click **Undo Remove**. The button shows how many removed characters can still be restored, and each click brings back the most recently removed one.

### Battle
- **Attack**: the selected character does a normal attack.
- **Special Move**: the selected character uses its special ability.
- **Next Turn ▶**: the character at the front of the turn queue acts, then goes to the back of the line.

Everything that happens is written in the **Battle Log** at the bottom.

---

## The 4 Character Types

| Type | Attack | Special Move |
|---|---|---|
| **Attacker** (red) | Strong plasma burst | **Barrage**: 3 rapid shots |
| **Defender** (blue) | Weaker shield bash | **Fortress**: raises a shield that absorbs damage |
| **Assassin** (purple) | 30% chance of a triple-damage critical hit | **Shadow Strike**: ignores armor, very high damage |
| **Healer** (green) | Weak light beam | **Renew**: restores its own health |

---

## Added Features (What's New)

**Required concepts**

1. **Abstraction**: `GameCharacter` is an abstract class with abstract methods (`attack`, `specialMove`, `drawEmblem`, and more). Each character type must write its own version.
2. **Abstract Data Types**: `CharacterManager` uses three:
   - **List (ArrayList)**: holds all characters, used for viewing and searching.
   - **Stack**: the recycle bin. The last character removed is the first one restored.
   - **Queue**: the turn order. First in line goes first, then moves to the back.
3. **GUI**: a full Java Swing window (`GameFrame`).
4. **User input**: create, view, search, update, and remove characters.

**Extra enhancements**

- **Four character types** (two were required) with different abilities. The Healer is new, and it actually changes its own health.
- **Dark theme** with a different color for each character type.
- **Animated card**: the glow pulses, flashes when a character acts, and the health bar slides smoothly.
- **Custom emblems**: crosshair, shield, dagger, and plus sign, drawn with code (no image files needed).
- **Live search** that filters as you type.
- **Undo for removals**, plus a confirmation before removing.
- **Turn queue display** showing who acts next.
- **Battle log** that records every action.

---

## Input Validation (Protecting Against Bad Input)

The program shows a warning message if:

- The name is empty
- The name is longer than 20 characters
- The name has symbols (only letters, numbers, spaces, `-` and `_` are allowed)
- The name already exists (not case-sensitive)
- Health or Power is not a whole number, or is outside its allowed range
- You click Update, Remove, Attack, or Special Move without selecting a character first

---

## How the Code Fits Together (Short Version)

```
Main  ──opens──>  GameFrame  ──uses──>  CharacterManager  ──stores──>  GameCharacter
                      │                  (List, Stack, Queue)           ├─ Attacker
                      └──shows──> AvatarPanel                           ├─ Defender
                                                                        ├─ Assassin
                                                                        └─ Healer
```

- `GameFrame` handles what you see and click.
- `CharacterManager` handles the data, with no GUI code.
- The character classes handle their own abilities and how they look.

---

## Ideas for Extending It

- Add a fifth type: create a new class that `extends GameCharacter`, fill in the abstract methods, and add its name to `TYPES` and the `create()` method in `CharacterManager`.
- Add enemy health so attacks actually reduce a target's HP.
- Save and load characters from a file.
