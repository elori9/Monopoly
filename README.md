## MONOPOLY GAME - Android App
A turn-based Monopoly game for mobile devices with simplified mechanics.

![Kotlin](https://img.shields.io/badge/Kotlin-100%25-purple?logo=kotlin)
![Android API](https://img.shields.io/badge/Android%20API-24%2B-green?logo=android)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-UI-blue?logo=android)
![JUnit5](https://img.shields.io/badge/JUnit5-5.8.2-blue?logo=junit)

---

### GAME SETTINGS
- Players: From 2 to 4 players. Each player can input their own name.

- Board Size: The board does not use the classic fixed size. Its scale depending on the N players in the match.

- Time Limit Mode: Option to set a timer for the match. When the timer reaches zero, the game ends immediately. The controller then calculates the total net worth of each player to declare the winner.

- Advanced Options: You can configure some rules for the game before start like turns on jail, start money,...

---

### RULES
- Turn Flow: On their turn, the player rolls the dice, moves across the board, and evaluates the landed square

- Rent and House Building: The restrictive "Color Groups" rule is removed: 
A player can buy houses and place them directly on their property without needing to own all the streets of that specific color.

- When landing on an opponent's property, the rent paid increases based on the number of houses built on that square.

- Jail: If you land on the penalty square, you go straight to jail. It acts as a direct turn penalty, losing 3 turns.

- Bankruptcy: If a player runs out of money to pay a debt and cannot generate liquidity, is eliminated from the game.

---

### PROJECT ARCHITECTURE

We  used the MVVM (Model-View-ViewModel) for developing the app.

- Model: Contains the pure logic of the game (Players, Abstract Boxes, Properties, Dice).

- ViewModel/GameManager: Manages the turns, the flow of time, purchase/payment validations, and dynamic board generation.

- View (UI): Interfaces designed to react to the state changes triggered by the View Model.

---

### DESIGN

On the `/docs` folder you can find the designs and diagrams used to develop the app.
We used **Visual Paradigm**.

---

### TESTING

As a good practice we realized testing for the backend. We used **Junit5** for that.
In addition, we used a **Mock** class for helping us to realize testing before the real implementation.

Furthermore, we use **Mockito** for make the dice roll as we wanted.

---

### GAME SCREENS

The game has multiple screens:
- Initial screen: used for start and letting the user either create a new game, go to help or leave the app.
- Help screen: used for giving info about the game to the user. Also has a link to our GitHub project
- Configuration screen: let the user configure the data for the game
- Game screen: the game
- Results screen: the results of the game

---

### INSTALLATION
1. Clone the repo:
   ```bash
   git clone https://github.com/elori9/Monopoly
    ```
2. Sync the Gradle
3. Execute it on your Android Emulator or on your device (Requires an API level 24 or above)

### AUTHORS
- [elori9](https://github.com/elori9)
- [elBerbrexo](https://github.com/elBerbrexo)