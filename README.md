# 🎮 WebSocket Game Room

A real-time multiplayer game room application built with **Spring Boot** backend and **vanilla JavaScript** frontend using WebSocket technology for instant communication.

## Features

✨ **Real-Time Communication**
- WebSocket support with STOMP protocol
- SockJS fallback for browser compatibility
- Bidirectional messaging between client and server

👥 **Room Management**
- Create unique game rooms with 4-letter codes
- Join existing rooms with room codes
- Track players in real-time
- Admin controls for starting games

🎯 **Game Features**
- Player role assignment (ADMIN, NORMAL, TROLL)
- Game state management (WAITING, IN_PROGRESS, FINISHED)
- Real-time player list updates
- Message logging and notifications

🎨 **Modern UI**
- Beautiful gradient design
- Responsive layout (mobile-friendly)
- Real-time status messages
- Event logging and history

## Architecture

### Backend Stack
- **Spring Boot 3.x** - REST API and WebSocket server
- **Kotlin** - Type-safe backend code
- **Spring WebSocket** - STOMP over SockJS
- **In-memory storage** - ConcurrentHashMap for game state

### Frontend Stack
- **HTML5** - Semantic structure
- **CSS3** - Modern styling with gradients and animations
- **Vanilla JavaScript** - No framework dependencies
- **SockJS + Stomp.js** - WebSocket client library

## Project Structure

```
websocket-game-room/
├── backend/
│   └── src/main/kotlin/com/gameroom/
│       ├── config/
│       │   └── WebSocketConfig.kt          # WebSocket configuration
│       ├── controller/
│       │   ├── GameRoomRestController.kt   # REST API endpoints
│       │   └── GameWebSocketController.kt  # WebSocket message handlers
│       ├── model/
│       │   ├── GameRoom.kt                 # Game room entity
│       │   ├── GameMessage.kt              # Message DTO
│       │   ├── GameState.kt                # Game state enum
│       │   ├── Player.kt                   # Player entity
│       │   ├── PlayerRole.kt               # Player role enum
│       │   └── MessageType.kt              # Message type enum
│       ├── service/
│       │   └── GameRoomService.kt          # Business logic
│       ├── util/
│       │   └── CodeGenerator.kt            # Room code generation
│       └── GameRoomApplication.kt          # Spring Boot entry point
├── frontend/
│   └── index.html                          # Single-page application
└── README.md                               # This file
```

## Installation & Setup

### Prerequisites
- Java 17+
- Spring Boot 3.x
- Maven or Gradle
- Modern web browser

### Backend Setup

1. **Build the application:**
   ```bash
   mvn clean package
   ```

2. **Run the Spring Boot server:**
   ```bash
   mvn spring-boot:run
   ```

   The server will start on `http://localhost:8080`

3. **Verify WebSocket endpoint:**
   - WebSocket: `ws://localhost:8080/ws`
   - REST API: `http://localhost:8080/api/rooms`

### Frontend Setup

1. **Open the application:**
   - Open `frontend/index.html` in your web browser
   - Or serve it with a simple HTTP server:
     ```bash
     python -m http.server 8000
     ```

2. **Access the application:**
   - Navigate to `http://localhost:8000/frontend/` (if using Python server)
   - Or open `index.html` directly in your browser

## API Endpoints

### REST API

#### Create Room
```
POST /api/rooms/create
Content-Type: application/json

{
  "playerName": "John"
}

Response:
{
  "code": "ABCD",
  "adminId": "uuid-1",
  "state": "WAITING",
  "players": {
    "uuid-1": {
      "id": "uuid-1",
      "name": "John",
      "role": "ADMIN"
    }
  }
}
```

#### Join Room
```
POST /api/rooms/join
Content-Type: application/json

{
  "roomCode": "ABCD",
  "playerName": "Jane"
}

Response: (GameRoom object)
```

#### Get Room Details
```
GET /api/rooms/{code}

Response: (GameRoom object)
```

#### Get Room Players
```
GET /api/rooms/{code}/players

Response:
{
  "players": [
    {
      "id": "uuid-1",
      "name": "John",
      "role": "ADMIN"
    },
    {
      "id": "uuid-2",
      "name": "Jane",
      "role": "NORMAL"
    }
  ]
}
```

### WebSocket Events

#### Client → Server

**Create Room:**
```
Destination: /app/create-room
{
  "playerName": "John"
}
```

**Join Room:**
```
Destination: /app/join-room
{
  "roomCode": "ABCD",
  "playerName": "Jane"
}
```

**Start Game:**
```
Destination: /app/start-game
{
  "roomCode": "ABCD",
  "playerId": "uuid-1"
}
```

**Leave Room:**
```
Destination: /app/leave-room
{
  "roomCode": "ABCD",
  "playerId": "uuid-1"
}
```

#### Server → Client

**Broadcast to Room:**
```
Topic: /topic/room/{code}
{
  "type": "ROOM_CREATED|PLAYER_JOINED|GAME_STARTED|PLAYER_LEFT",
  "roomCode": "ABCD",
  "message": "...",
  "players": [...]
}
```

**Individual Messages:**
```
Topic: /user/queue/role-assignment
{
  "type": "ROLE_ASSIGNED",
  "playerId": "uuid-1",
  "role": "ADMIN|NORMAL|TROLL"
}
```

## Usage

### Creating a Game Room

1. Open the application
2. Enter your name
3. Click "Create Room"
4. Share the room code with other players
5. Wait for players to join

### Joining a Room

1. Open the application
2. Click "Join Room"
3. Enter your name
4. Enter the room code (4 letters)
5. Click "Join"

### Starting a Game

1. As room admin, click "Start Game"
2. Roles will be assigned to all players
3. Game state changes to IN_PROGRESS
4. Players see real-time updates

## Message Flow

```
Client                          Server
  |                              |
  |-- POST /api/rooms/create --> |
  |<---- GameRoom Response ------|
  |                              |
  |-- WebSocket Connect -------> |
  |<---- Subscribe Confirmation --|
  |                              |
  |-- /app/join-room ---------> |
  |<---- /topic/room/{code} ----| (broadcast to all)
  |     PLAYER_JOINED event      |
  |                              |
  |-- /app/start-game --------> |
  |<---- /topic/room/{code} ----| (broadcast to all)
  |     GAME_STARTED event       |
  |                              |
  |<---- /user/queue/role ------|
  |     ROLE_ASSIGNED event      |
  |                              |
  |-- /app/leave-room --------> |
  |<---- /topic/room/{code} ----| (broadcast to all)
  |     PLAYER_LEFT event        |
```

## Technologies Used

### Spring Boot Ecosystem
- `spring-boot-starter-web` - REST API support
- `spring-boot-starter-websocket` - WebSocket support
- `spring-boot-starter-messaging` - STOMP messaging

### Frontend Libraries
- **SockJS** - WebSocket emulation with fallbacks
- **Stomp.js** - STOMP protocol client

### Tools & Frameworks
- **Kotlin** - Modern JVM language
- **Maven** - Build automation
- **Git** - Version control

## Game Room Features

### Room Code Generation
- Generates unique 4-letter alphanumeric codes
- Prevents duplicate codes
- Easy to share and remember

### Player Management
- Unique player IDs (UUID)
- Player roles (ADMIN, NORMAL, TROLL)
- Real-time player list tracking
- Automatic cleanup when empty

### Game State
- **WAITING** - Room created, waiting for players
- **IN_PROGRESS** - Game started, roles assigned
- **FINISHED** - Game completed

### Message Types
- `ROOM_CREATED` - Room successfully created
- `PLAYER_JOINED` - New player joined
- `GAME_STARTED` - Game started by admin
- `ROLE_ASSIGNED` - Individual role assignment
- `PLAYER_LEFT` - Player left the room
- `ERROR` - Error occurred

## Configuration

### Default Settings
- Server port: `8080`
- WebSocket endpoint: `/ws`
- API base path: `/api/rooms`
- CORS enabled for all origins

### Customization

Edit `application.properties` or `application.yml`:
```properties
server.port=8080
server.servlet.context-path=/api
spring.application.name=game-room-service
```

## Performance Considerations

- **In-Memory Storage**: All data stored in ConcurrentHashMap (session-based)
- **WebSocket Efficiency**: Bi-directional communication reduces HTTP overhead
- **Scalability**: Current implementation suitable for small groups
- **Future Enhancement**: Consider Redis for distributed deployments

## Browser Support

- Chrome/Chromium (85+)
- Firefox (78+)
- Safari (14+)
- Edge (85+)
- Mobile browsers (with SockJS fallback)

## Troubleshooting

### WebSocket Connection Failed
- Check if server is running on port 8080
- Verify firewall allows WebSocket connections
- Browser console should show connection errors

### Room Not Found
- Verify room code is correct (4 letters)
- Check if room still exists (empty rooms auto-delete)
- Room code is case-insensitive

### Players Not Showing
- Ensure WebSocket is connected
- Check browser console for errors
- Verify CORS is enabled on server

## Future Enhancements

- [ ] Database persistence (PostgreSQL/MongoDB)
- [ ] User authentication and profiles
- [ ] Private/public rooms
- [ ] Message history
- [ ] Game timer and scoring
- [ ] Admin dashboard
- [ ] Voice/video chat integration
- [ ] Mobile app (React Native)
- [ ] Redis support for horizontal scaling
- [ ] Game lobby and matchmaking

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## License

MIT License - feel free to use this project for learning and development.

## Support

For issues, questions, or suggestions:
- Check the troubleshooting section
- Review the code comments
- Open an issue on GitHub

---

**Happy Gaming! 🚀**

Built with ❤️ using Spring Boot and WebSocket technology.
