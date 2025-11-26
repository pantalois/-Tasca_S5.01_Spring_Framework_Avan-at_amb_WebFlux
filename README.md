# Blackjack – Card Game Simulation

## 1. Overview

Blackjack is a simple Spring-based web application that simulates the classic blackjack card game.  
The goal is not to implement every rule perfectly, but to provide an asynchronous, database-backed demo where you can:

- Create players
- Start new blackjack games
- Draw cards and update the game state
- Persist game history and statistics

The game logic runs on a reactive stack, and all data is stored in MySQL and MongoDB, orchestrated with Docker.

---

## 2. Tech Stack

- **Java 21**  
  Core language used to implement the game engine, services, and API (Spring WebFlux).

- **Docker**  
  Used to containerize the application and its databases. A `docker-compose` file spins up:
  - The Spring WebFlux application
  - A MySQL instance
  - A MongoDB instance

- **MySQL**  
  Relational database used to store structured data such as players, rankings, and completed game records.

- **MongoDB**  
  Document database used to store the evolving game state (hands, decks, in-progress games, etc.).

- **Postman**  
  Recommended tool to call and test the REST endpoints (create player, start game, hit/stand, list games, etc.).

---

## 3. How to Run the Project

1. **Download or clone the repository**

   - Clone via Git: `git clone https://github.com/pantalois/-Tasca_S5.01_Spring_Framework_Avan-at_amb_WebFlux.git`
   - Or download the ZIP from your Git hosting platform and extract it.

2. **Open Docker Desktop**

3. **Start the application with Docker Compose**

   - Open a terminal in the root folder of the project (where the `docker-compose.yml` file is located).
   - Run: `docker compose up`  
     
   Docker Compose will:
   - Build and start the Java 21 Spring WebFlux application
   - Start a MySQL container
   - Start a MongoDB container

4. **Wait until all services are up**

   - When all containers are running without errors, the API should be available at:  
     `http://localhost:8080`

### 4.1 Game Endpoints (`GameStateController`)

All game-related endpoints are under the base path:

`/game`

#### 4.1.1 Create a new game

- **Method:** `POST`
- **Path:** `/game/new`
- **Description:** Creates a new Blackjack game for the given player.
- **Request body:** JSON matching `CreateGameRequest`.  
  Typically includes player information (e.g. player id or name) required to start the game.
- **Response:**
  - `201 Created`
  - Body: `GameStateResponse` with the initial state of the game (player hand, dealer hand, scores, etc.).

Example (conceptual):

POST /game/new
Content-Type: application/json

{
  "playerName": "Alice"
}

#### 4.1.2 Get game by ID

-   **Method:** `GET`
    
-   **Path:** `/game/{id}`
    
-   **Description:** Returns the current state of a game identified by its MongoDB id.
    
-   **Path variables:**
    
    -   `id` – Game identifier (String).
        
-   **Response:**
    
    -   `200 OK` with `GameStateResponse`.
        
    -   `404 Not Found` if the game does not exist.
        

Example:

`GET /game/675f0b432c1a4b0c9a1f4d12`

#### 4.1.3 Play a move (HIT / STAND)

-   **Method:** `PUT`
    
-   **Path:** `/game/{id}/play`
    
-   **Description:** Applies a move to the indicated game (for example, HIT or STAND).
    
-   **Path variables:**
    
    -   `id` – Game identifier (String).
        
-   **Request body:** JSON matching `PlayMoveRequest`.  
    Typically includes the type of move (`HIT`, `STAND`, etc.).
    
-   **Response:**
    
    -   `200 OK`
        
    -   Body: Updated `GameStateResponse` after the move (new cards, updated scores, game result if finished).
        

Example (conceptual):

PUT /game/675f0b432c1a4b0c9a1f4d12/play 
Content-Type: application/json
{
  "move": "HIT" 
}

#### 4.1.4 Delete a game

-   **Method:** `DELETE`
    
-   **Path:** `/game/{id}/delete`
    
-   **Description:** Deletes the game with the given id. Useful for cleaning up finished or test games.
    
-   **Path variables:**
    
    -   `id` – Game identifier (String).
        
-   **Response:**
    
    -   `204 No Content` when deletion is successful.
        
    -   `404 Not Found` if the game does not exist.
        

Example:

`DELETE /game/675f0b432c1a4b0c9a1f4d12/delete`

* * *

### 4.2 Player Endpoints (`PlayerController`)

Player-related endpoints are exposed at the root (no class-level `@RequestMapping`), so the paths below are absolute.

#### 4.2.1 Get players ranking

-   **Method:** `GET`
    
-   **Path:** `/ranking`
    
-   **Description:** Returns the ranking of players ordered according to the criteria defined in `PlayerService`  
    (e.g. number of wins, win rate, or similar).
    
-   **Response:**
    
    -   `200 OK`
        
    -   Body: `Flux<PlayerResponse>` (JSON array of players with their stats).
        

Example:

`GET /ranking`

Example response (conceptual):

`[   {     "id": 1,     "name": "Alice",     "wins": 10   },   {     "id": 2,     "name": "Bob",     "wins": 7   } ]`

#### 4.2.2 Update player by ID

-   **Method:** `PUT`
    
-   **Path:** `/player/{id}`
    
-   **Description:** Updates an existing player (typically the player name) identified by its MySQL id.
    
-   **Path variables:**
    
    -   `id` – Player identifier (Long).
        
-   **Request body:** JSON matching `PlayerRequest`.  
    Usually contains the new player name or other updatable fields.
    
-   **Response:**
    
    -   `200 OK`
        
    -   Body: `PlayerResponse` with the updated player data.
        
    -   `404 Not Found` if the player does not exist.
        

Example (conceptual):

`PUT /player/1 Content-Type: application/json  {   "name": "New Player Name" }`

Example response (conceptual):

`{   "id": 1,   "name": "New Player Name",   "wins": 10 }`

* * *
