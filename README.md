# Hexagonal Architecture Platform (Spring)

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

- **Driving ports** in `application.port.driving` (`For*`)
- **Driven ports** in `application.port.driven` (`*DrivenPort`)
- **Inbound adapters** implement driving side (REST + bootstrap)
- **Outbound adapters** implement driven side (JPA + CSV)

### Module Structure
```text
fmp-football/
├── domain/                   # Pure domain model + algorithm (framework-free)
│   ├── model/
│   └── service/
├── application/              # Use cases and port contracts
│   ├── port/
│   │   ├── driving/          # ForTeam, ForMatch, ForFindLongestPlayingPair, ...
│   │   └── driven/           # TeamDrivenPort, MatchDrivenPort, CsvImportDrivenPort, ...
│   └── service/              # Application services implementing driving ports
├── inbound-adapters/         # REST controllers + startup initializer
│   ├── rest/
│   └── bootstrap/
├── outbound-adapters/        # JPA/CSV implementations of driven ports
│   ├── persistence/
│   │   ├── entity/
│   │   └── repository/
│   └── csv/
├── server/                   # Spring Boot runtime/bootstrap module
└── client/                   # Reserved for client contracts/integration
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
1. `GET /api/player-pairs/longest` hits inbound REST controller.
2. Controller calls driving port `ForFindLongestPlayingPair`.
3. Application service loads data via `RecordDrivenPort` and `PlayerDrivenPort`.
4. Domain algorithm (`PlayerPairAnalyzer`) computes overlap totals.
5. Response returns winning pair + per-match minutes.

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
- `toMinutes` values that are null/`NULL` in CSV are treated as `90`.
- CSV parsing is implemented manually (no external CSV library).
