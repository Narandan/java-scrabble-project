# java-scrabble-project

**Authors:** Tyler Keller and Narandan Miller  
**Description:** A Java implementation of Scrabble with a graphical user interface (GUI) and standard game rules.

---

## Interface Instructions / Game Description

### Title Menu

The title menu has:

- A **Play** button that leads to the **Game Creation Menu**
- A **Credits** button that leads to the **Credits Menu**

### Credits Menu

The Credits Menu simply lists the project credits.

---

### Game Creation Menu

The Game Creation Menu contains:

- A **Back** button in the top-left to return to the Title Menu.
- A **Load Game** button on the right that allows the player to load a previously saved game file.
- A central area for **player configuration**:
  - Each player has a customizable name.
- A **player count slider** in the bottom-left to choose the number of players.
- A **Start Game** button in the bottom-right that begins the game and leads to the **Game Menu**.

---

### Game Menu

This is the main Scrabble portion of the application, where the core rules are implemented.

- **Player List & Scores (Left):**  
  All players are displayed along the left side with their current scores.

- **Board (Center):**  
  The Scrabble board is displayed in the middle. During their turn, players can drag and drop their tiles onto valid board squares.

- **Player Rack (Bottom):**  
  Once the **Begin Turn** button is pressed, the current player’s rack appears at the bottom, allowing them to drag tiles from their rack onto the board.

- **Control Buttons (Bottom-Right):**
  - Initially, a **Begin Turn** button is shown.
  - After pressing **Begin Turn**, the following buttons appear:
    - **Pass** – Skip the turn without playing a word.
    - **End Turn** – Submit the currently placed word and end the turn.
    - **Resign** – Forfeit the game.
    - **Exchange** – Open a dialog to exchange selected tiles.

- **Exchange Dialog:**  
  The **Exchange** button opens a `JDialog` that allows the user to select which tiles to exchange. The dialog includes **Confirm** and **Cancel** buttons.

- **Save Game (Top-Right):**  
  A **Save Game** button allows the user to save the **current game state** to a file using the full save system described below.

- **Blank Tile Handling:**  
  When dragging a blank tile (`"_"`) onto the board, a `JDialog` pops up allowing the user to choose which letter the blank tile represents, using left and right arrow controls.

- **End of Game / Results:**  
  When the game ends (e.g., tiles exhausted and passes/resigns as applicable), the application switches to the **Results Menu**.

---

### Results Menu

The Results Menu displays:

- The **first place winner** at the top.
- A **ranking of all players** in the middle, with their respective scores.
- A **Back** button in the top-left that returns to the Title Menu so that a new game can be started.

---

## Rules & Variations

- The game implements the **standard Scrabble rules**:
  - Players place words horizontally or vertically.
  - Words must connect to existing tiles after the first move.
  - Scoring uses standard letter values and board multipliers.
- **No custom rule variations** are currently implemented.  
  Gameplay follows the standard rules of Scrabble as closely as possible within the scope of this project.

---

## Extra Features (Beyond Assignment Requirements)

### Full Save & Load System (Extra Credit)

This project includes an advanced save/load feature that preserves the entire game state, going beyond the minimum assignment requirements.

The system stores:

1. Complete **board state**
2. **Tile bag** contents and order
3. All **player racks**
4. Assigned letters for **blank tiles**
5. **Turn order** and current player
6. **Consecutive pass count**

Saved games can be restored from the **Game Creation Menu**, allowing long games to be paused and resumed later.

---

## Running the Program

### Runnable .jar

The provided .jar file is runnable and can be used with:

bash
java -jar hwx.jar

This will launch the game directly.

## Compiling & Running from Source (Unpacked)

If the project is unpacked, it can be compiled and run with:

bash
javac Main.java
java Main

(Adjust the main class name if necessary, depending on your exact project structure.)

## External Resources / Sources

### Word List Source

This Scrabble project uses the **SOWPODS** word list for validating words.

- Source: https://www.freescrabbledictionary.com/sowpods/download/sowpods.txt
- Author / Maintainer: FreeScrabbleDictionary.com
- License / Usage: Freely available for educational use

### Font Source

The project uses the **Clear Sans** font.

- Source: https://www.1001fonts.com/clear-sans-font.html
- Author / Maintainer: Intel
- License / Usage: Free for commercial use
