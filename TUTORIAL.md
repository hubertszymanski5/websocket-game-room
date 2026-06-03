# WebSocket Game Room Tutorial - Step by Step Guide

Welcome! This tutorial will guide you through building a real-time game room application from scratch.

## Table of Contents

1. [Backend Setup](#backend-setup)
2. [WebSocket Configuration](#websocket-configuration)
3. [Game Room Model & Services](#game-room-model--services)
4. [WebSocket Controllers](#websocket-controllers)
5. [Frontend Setup](#frontend-setup)
6. [React Components](#react-components)
7. [WebSocket Client Integration](#websocket-client-integration)
8. [Running the Application](#running-the-application)

---

## Backend Setup

### Step 1: Create Gradle Project Structure

The backend is a Spring Boot application using Kotlin. We'll use Gradle for dependency management.

**File Structure:**
```
backend/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── com/gameroom/
│   │   │       ├── GameRoomApplication.kt
│   │   │       ├── config/
│   │   │       │   └── WebSocketConfig.kt
│   │   │       ├── model/
│   │   │       │   ├── GameRoom.kt
│   │   │       │   ├── Player.kt
│   │   │       │   └── GameMessage.kt
│   │   │       ├── service/
│   │   │       │   └── GameRoomService.kt
│   │   │       ├── controller/
│   │   │       │   └── GameWebSocketController.kt
│   │   │       └── util/
│   │   │           └── CodeGenerator.kt
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── build.gradle.kts
└── gradle/
```

### Step 2: Understanding the Architecture

Our application will use:
- **STOMP Protocol** over WebSocket for message handling (simpler than raw WebSocket)
- **Game Rooms** identified by 4-letter codes
- **Two Player Roles**: ADMIN (room creator) and NORMAL (joiners)
- **Real-time Broadcasting** of game state changes

### Step 3: Key Concepts

#### Game Room
Each room has:
- A unique 4-letter code
- An admin player
- Multiple normal players
- A current game state (WAITING, IN_PROGRESS, etc.)
- Role assignments

#### Messages
We'll use these message types:
- `CREATE_ROOM` - Create a new game room
- `JOIN_ROOM` - Join existing room
- `START_GAME` - Admin starts the game
- `ROLE_ASSIGNED` - Server broadcasts assigned role
- `PLAYER_JOINED` - Notify all players of new member
- `PLAYER_LEFT` - Notify all players when someone leaves

---

## WebSocket Configuration

### What is STOMP?

STOMP (Simple Text Oriented Messaging Protocol) is a messaging protocol that works over WebSocket. It's simpler than raw WebSocket and provides:
- Pub/Sub messaging pattern
- Message destinations
- Automatic JSON serialization
- Built-in error handling

### Spring WebSocket with STOMP

Spring abstracts away the complexity, allowing us to:
- Define message endpoints
- Subscribe to topics
- Send to destinations
- Handle connections/disconnections

---

## Game Room Model & Services

### Data Models

We'll create several Kotlin data classes:

1. **Player** - Represents a connected user
2. **GameRoom** - Represents a game session
3. **GameMessage** - Represents messages between client and server
4. **GameState** - Represents current game state

### Services

The `GameRoomService` will handle:
- Creating new rooms
- Managing players joining/leaving
- Role assignment logic
- Broadcasting state updates

---

## WebSocket Controllers

### Message Handling

We'll use `@MessageMapping` to handle incoming messages and `SimpMessagingTemplate` to broadcast updates.

**Flow:**
1. Client connects to `/ws`
2. Client sends message to `/app/create-room` or `/app/join-room`
3. Server processes and broadcasts to `/topic/room/{code}`
4. All subscribed clients receive update

---

## Frontend Setup

### React + Vite + TypeScript

The frontend is a modern React application using:
- **Vite** - Fast build tool
- **TypeScript** - Type safety
- **React Hooks** - State management
- **WebSocket API** - Real-time communication

### Key Screens

1. **Home Screen** - Create or join room
2. **Room Screen** - View players, admin controls
3. **Role Assignment Screen** - Display assigned role

---

## React Components

### Component Structure

```
src/
├── components/
│   ├── RoomCreation.tsx - Create room form
│   ├── RoomJoin.tsx - Join room form
│   ├── GameRoom.tsx - Main game room display
│   ├── PlayerList.tsx - List of players
│   ├── RoleDisplay.tsx - Show assigned role
│   └── AdminControls.tsx - Start game button
├── hooks/
│   └── useWebSocket.ts - WebSocket connection hook
├── types/
│   └── game.ts - TypeScript interfaces
├── App.tsx
└── main.tsx
```

---

## WebSocket Client Integration

### Custom Hook: useWebSocket

We'll create a custom React hook that handles:
- Connection/disconnection
- Message sending
- Event subscriptions
- Error handling

### Message Format

Messages use JSON with consistent structure:
```json
{
  "type": "CREATE_ROOM",
  "playerId": "uuid-here",
  "playerName": "John",
  "roomCode": "ABCD"
}
```

---

## Running the Application

### Prerequisites
- Java 17+
- Node.js 18+
- npm or yarn

### Backend
```bash
cd backend
./gradlew bootRun
# Server runs on http://localhost:8080
```

### Frontend
```bash
cd frontend
npm install
npm run dev
# App runs on http://localhost:5173
```

### Testing

1. Open `http://localhost:5173`
2. Create room (generates code like "ABCD")
3. Open new tab to same URL
4. Join room using code
5. In first tab, click "Start Game"
6. Both tabs should see role assignments

---

## Next Steps

Each commit in this repository represents a complete, working step. You can:
1. Read the explanation above
2. View the committed code
3. Understand how pieces fit together
4. Code along locally

Let's begin! Check out the individual commits for detailed implementations.
