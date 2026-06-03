# WebSocket Game Room Tutorial

A complete tutorial for building a real-time game room application using WebSockets with Kotlin + Spring Boot backend and React frontend.

## Project Overview

This application demonstrates:
- Creating game rooms with random 4-letter codes
- Real-time player joining via WebSocket
- Admin controls for game management
- Real-time role assignment (TROLL vs NORMAL)
- Live game state synchronization across all connected clients

## Tech Stack

**Backend:**
- Kotlin
- Spring Boot 3.x
- Spring WebSocket
- Gradle

**Frontend:**
- React 18
- Vite
- TypeScript
- WebSocket API

## Getting Started

See [TUTORIAL.md](./TUTORIAL.md) for detailed step-by-step instructions.

## Project Structure

```
websocket-game-room/
├── backend/              # Spring Boot + Kotlin
│   ├── src/
│   ├── build.gradle.kts
│   └── gradle/
├── frontend/             # React + Vite
│   ├── src/
│   ├── vite.config.ts
│   ├── package.json
│   └── tsconfig.json
└── TUTORIAL.md          # Step-by-step guide
```

## Quick Start

### Backend
```bash
cd backend
./gradlew bootRun
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

Visit `http://localhost:5173` in your browser.
