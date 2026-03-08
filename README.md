# Football Player Pairs — Sirma Academy Final Major Project


## Project Description
This application finds the pair of football players who played together for the longest total time across all matches.  
It also exposes CRUD APIs for teams, players, matches, and participation records.

## Technology Stack
- Java 21
- Spring Boot 3.3.5
- Spring Data JPA
- PostgreSQL
- Gradle (multi-module)

## Current Architecture (Ports and Adapters)

This project now follows a clear hexagonal architecture with explicit naming:

- **Driving ports** in `application.port.driving` — define what the outside world can ask the app to do
- **Driven ports** in `application.port.driven` — define what the app needs from external systems
- **Inbound adapters** implement the driving side (REST controllers + CSV bootstrap)
- **Outbound adapters** implement the driven side (JPA persistence + CSV parsing)

### Module Structure
```text
fmp-FootballPlayerPairs/
├── domain/                   # Pure Java — domain models and PlayerPairAnalyzer algorithm
│   ├── model/                # Player, Match, Team, Record, PlayerPairResult
│   └── service/              # PlayerPairAnalyzer
├── application/              # Use cases and port contracts
│   ├── port/
│   │   ├── driving/          # ForPlayer, ForMatch, ForTeam, ForRecord,
│   │   │                     # ForFindLongestPlayingPair, ForImportCsvData
│   │   └── driven/           # PlayerDrivenPort, MatchDrivenPort, TeamDrivenPort,
│   │                         # RecordDrivenPort, CsvImportDrivenPort
│   └── service/              # Application services implementing driving ports
├── inbound-adapters/         # REST controllers and DataInitializer (bootstrap)
│   ├── rest/
│   └── bootstrap/
├── outbound-adapters/        # JPA and CSV implementations of driven ports
│   ├── persistence/
│   │   ├── entity/           # JPA entities — RecordEntity, PlayerEntity, etc.
│   │   └── repository/       # Spring Data JPA repositories
│   └── csv/                  # CSV parsers and CsvImportAdapter
└── server/                   # Spring Boot entry point — wires all modules together
```

### Dependency Direction
```text
inbound-adapters -> application <- outbound-adapters
                           ^
                         domain
server composes all runtime modules
```

## Core Flows

### 1) Longest Player Pair
1. `GET /api/players/longest` hits the inbound REST controller.
2. Controller calls driving port `ForFindLongestPlayingPair`.
3. `PlayerPairApplicationService` delegates to `RecordDrivenPort.findLongestPlayingPair()`.
4. A native PostgreSQL query computes all pair overlaps and returns all pairs
   with the highest total minutes.
5. Response returns all winning pairs sorted by player ID.

### 2) CSV Bootstrap Import
1. On startup, inbound bootstrap adapter resolves `resources/csv`.
2. Calls driving port `ForImportCsvData`.
3. Outbound CSV adapter parses and persists through JPA repositories.

## API Endpoints

### Player Pair Analysis
- `GET /api/player-pairs/longest`

Example response:
```json
{
  "player1Id": 112,
  "player2Id": 128,
  "totalMinutes": 630,
  "matchDurations": [
    { "matchId": 3, "minutes": 90 },
    { "matchId": 17, "minutes": 90 }
  ]
}
```

### CRUD Endpoints
- Teams: `GET/POST /api/teams`, `GET/PUT/DELETE /api/teams/{id}`
- Players: `GET/POST /api/players`, `GET/PUT/DELETE /api/players/{id}`
- Matches: `GET/POST /api/matches`, `GET/PUT/DELETE /api/matches/{id}`
- Records: `GET/POST /api/records`, `GET/PUT/DELETE /api/records/{id}`

### Tables

| Table     | Description                                              |
|-----------|----------------------------------------------------------|
| teams     | Football clubs                                           |
| players   | Individual players, each linked to a team                |
| matches   | Games between two teams with a date and score            |
| records   | Who played in which match and for how many minutes       |

### Indexes

The following indexes are defined on the JPA entities for query performance:

| Index                  | Table   | Column    |
|------------------------|---------|-----------|
| idx_records_match_id   | records | match_id  |
| idx_records_player_id  | records | player_id |
| idx_players_team_id    | players | team_id   |
| idx_matches_date       | matches | date      |


## Getting Started

### Prerequisites
- Java 21
- PostgreSQL

### Database
Run PostgreSQL:
```bash
docker run -d --name postgres \
  -e POSTGRES_PASSWORD=your_password \
  -e POSTGRES_DB=football_db \
  -p 5432:5432 \
  postgres:15
```

Update `server/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/football_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### Build and Run
```bash
./gradlew clean build
./gradlew :server:bootRun
```

Application URL: `http://localhost:8080`

## Notes
- `toMinutes` values that are null in CSV are treated as 90 minutes.
- CSV parsing is implemented manually without any external CSV library.
- CSV data is only imported once — if records already exist in the database,
  the import is skipped on restart.
- The domain module has zero Spring or JPA dependencies — it is pure Java
  and can be tested independently of any framework.