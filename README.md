## MONOPOLY GAME - Android App
A turn-based Monopoly game for mobile devices with simplified mechanics.

---

### GAME SETTINGS
- Players: From 2 to 4 players. Each player can input their own name.

- Board Size: The board does not use the classic fixed size. Its scale depending on the N players in the match.

- Time Limit Mode: Option to set a timer for the match. When the timer reaches zero, the game ends immediately. The controller then calculates the total net worth of each player to declare the winner.

### RULES
- Turn Flow: On their turn, the player rolls the dice, moves across the board, and evaluates the landed square

- Rent and House Building: The restrictive "Color Groups" rule is removed: 
A player can buy houses and place them directly on their property without needing to own all the streets of that specific color.

- When landing on an opponent's property, the rent paid increases based on the number of houses built on that square.

- Jail: If you land on the penalty square, you go straight to jail. It acts as a direct turn penalty, losing 3 turns.

- Bankruptcy: If a player runs out of money to pay a debt and cannot generate liquidity, is eliminated from the game.

---

### PROJECT ARCHITECTURE

- Model: Contains the pure logic of the game (Players, Abstract Boxes, Properties, Dice).

- Controller (GameManager): Manages the turns, the flow of time, purchase/payment validations, and dynamic board generation.

- View (UI): Interfaces designed to react to the state changes triggered by the Controller.