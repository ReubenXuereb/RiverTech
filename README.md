# Game Service

## Endpoints
* If you are using Postman go to ./docs -> Rivertech.postman_collection.json and import the endpoints.

| Method | Endpoint                     | Description                                                          |
|--------|------------------------------|----------------------------------------------------------------------|
| POST   | /game/play                   | Plays the game(needs raw json body to place betAmount and betNumber) |
| GET    | /game/leaderboard            | Shows top 10 players ranked by total winnings                        |

* Start game raw JSON body:
  ```java
  {
    "player": {
        "playerId": "1",
        "username": "reu117",
        "name": "Reuben",
        "surname": "Xuereb"
    },
    "betNumber": "5",
    "betAmount": "100.00"
  }
  ```

# Player Service

## Endpoints
* If you are using Postman go to ./docs -> Rivertech.postman_collection.json and import the endpoints.

| Method | Endpoint                  | Description                                   |
|--------|---------------------------|-----------------------------------------------|
| POST   | /players/register         | Registers a player                            |
| GET    | /players/{playerId}/wallet | Gets all transactions of that specific player |
| GET    | /players                  | Gets all players registered                   |

* Register Player raw JSON body:
  ```java
  {
    "username": "reu117",
    "name": "Reuben",
    "surname": "Xuereb"
  }
  ```
## Prerequisites
- Java 23
- Maven (latest version)
- Kotlin 1.9.25
- Spring boot 3.3.5
- H2 database. To access tables paste in a browser http://localhost:8080/h2-console
```java
  JDBC URL: jdbc:h2:mem:OddsBasedDB
  User Name: rivertech
  Password: password