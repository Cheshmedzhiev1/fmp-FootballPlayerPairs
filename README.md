Final Major Project - Sirma Academy

## Project Description
This application identifies pairs of football players who have played together in common matches for the longest total time. It analyzes CSV data containing match records and calculates overlapping playing time for each player pair across all matches.

## Features

- Hexagonal Architecture - Clean separation of domain, data, and API layers
- CSV Parsing - Manual CSV parser (no external libraries)
- Multiple Date Format Support - Handles various date formats automatically
- NULL Handling - Treats NULL `toMinutes` as 90 (end of match)
- PostgreSQL Database - Persistent data storage
- REST API - Full CRUD operations + player pair analysis
- Algorithm - Finds longest-playing pairs with match-by-match breakdown

##  Technology Stack

- Java 21
- Spring Boot 3.3.5
- Spring Data JPA
- PostgreSQL
- Gradle

## Architecture
```
fmp-football/
├── domain/          # Business logic and domain models
│   ├── model/       # Team, Player, Match, Record, PlayerPairResult
│   └── service/     # PlayerPairAnalyzer (core algorithm)
├── data/            # Data access layer
│   ├── entity/      # JPA entities
│   ├── repository/  # Spring Data repositories
│   ├── csv/         # CSV parsers (no external libraries)
│   └── service/     # Data services
├── server/          # REST API layer
│   ├── controller/  # REST controllers
│   ├── config/      # Configuration classes
│   └── resources/   # Application properties, CSV files
└── client/          # API contracts (DTOs)
```

##  Getting Started

## What you will need

- Java 21
- PostgreSQL database
- Gradle

### Database Setup

1. Create PostgreSQL database:
bash
docker run -d --name postgres \
  -e POSTGRES_PASSWORD=your_password \
  -e POSTGRES_DB=football_db \
  -p 5432:5432 \
  postgres:15


2. Update `server/src/main/resources/application.properties`:
`properties
spring.datasource.url=jdbc:postgresql://localhost:5432/football_db
spring.datasource.username=postgres
spring.datasource.password=your_password


### Running the Application
bash
./gradlew clean build
./gradlew :server:bootRun


Application starts on: `http://localhost:8080`

##  API Endpoints

### Player Pair Analysis
```
GET /api/player-pairs/longest
```

Returns the pair of players with the longest total playing time together.

Example Response:
json
{
  "player1Id": 112,
  "player2Id": 128,
  "totalMinutes": 630,
  "matchDurations": [
    {"matchId": 3, "minutes": 90},
    {"matchId": 17, "minutes": 90},
    {"matchId": 27, "minutes": 90},
    {"matchId": 40, "minutes": 90},
    {"matchId": 45, "minutes": 90},
    {"matchId": 49, "minutes": 90},
    {"matchId": 51, "minutes": 90}
  ]
}


### CRUD Operations

Teams:
- GET /api/teams - Get all teams
- GET /api/teams/{id} - Get team by ID
- POST /api/teams - Create team
- PUT /api/teams/{id} - Update team
- DELETE /api/teams/{id} - Delete team

Players:
- GET /api/players - Get all players
- GET /api/players/{id} - Get player by ID
- POST /api/players-  Create player
- PUT /api/players/{id} - Update player
- DELETE /api/players/{id} - Delete player

Matches:
- GET /api/matches - Get all matches
- GET /api/matches/{id} - Get match by ID
- POST /api/matches - Create match
- PUT /api/matches/{id} - Update match
- DELETE /api/matches/{id} - Delete match

Records:
- GET /api/records - Get all records
- GET /api/records/{id} - Get record by ID
- POST /api/records - Create record
- PUT /api/records/{id} - Update record
- DELETE /api/records/{id} - Delete record

##  Algorithm

The algorithm works in the following steps:

1. Load Data - Fetch all records and player-team mappings
2. Group by Match - Organize records by match ID
3. Group by Team - Within each match, group players by team
4. Calculate Overlaps - For each pair of teammates, calculate overlapping playing time
5. Aggregate Results - Sum total time per pair across all matches
6. Find Maximum - Return the pair with the longest total time

Time Complexity: O(m × p²) where m = matches, p = players per match

##  Data Model

### CSV Files

- teams.csv - Team information
- players.csv - Player details and team assignments
- matches.csv - Match information
- records.csv - Player participation records (who played when)

### Database Schema

- teams - id, name, managerFullName, groupName
- players - id, teamNumber, position, fullName, teamId
- matches - id, aTeamId, bTeamId, date, score
- records - id, playerId, matchId, fromMinutes, toMinutes

##  Key Implementation Details

- Hexagonal Architecture** - Domain logic independent of frameworks
- Ports & Adapters - Repositories as ports, JDBC/JPA as adapters
- Manual CSV Parsing - No external libraries (requirement)
- Date Format Support - Multiple formatters with fallback
- NULL Handling - toMinutes NULL treated as 90 minutes

## Author
Martin Cheshmedzhiev
