package com.gameroom.controller

import com.gameroom.model.GameMessage
import com.gameroom.model.MessageType
import com.gameroom.service.GameRoomService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin

/**
 * WebSocket controller for handling game room messages.
 * Uses STOMP protocol over WebSocket for real-time communication.
 */
@Controller
@CrossOrigin("*")
class GameWebSocketController(
    private val gameRoomService: GameRoomService,
    private val messagingTemplate: SimpMessagingTemplate
) {

    /**
     * Handles room creation request.
     * Client sends: /app/create-room
     * Server broadcasts to: /topic/room/{code}
     */
    @MessageMapping("/create-room")
    fun createRoom(message: GameMessage) {
        val playerName = message.playerName ?: "Anonymous"
        val room = gameRoomService.createRoom(playerName)
        
        // Broadcast room created event to the creator
        messagingTemplate.convertAndSend(
            "/topic/room/" + room.code,
            GameMessage(
                type = MessageType.ROOM_CREATED,
                roomCode = room.code,
                playerId = room.adminId,
                playerName = playerName,
                message = "Room created with code: " + room.code
            )
        )
    }

    /**
     * Handles player joining a room.
     * Client sends: /app/join-room
     * Server broadcasts to: /topic/room/{code}
     */
    @MessageMapping("/join-room")
    fun joinRoom(message: GameMessage) {
        val roomCode = message.roomCode ?: return
        val playerName = message.playerName ?: "Anonymous"
        
        val room = gameRoomService.joinRoom(roomCode, playerName) ?: run {
            // Room not found
            messagingTemplate.convertAndSend(
                "/topic/error/" + roomCode,
                GameMessage(
                    type = MessageType.ERROR,
                    message = "Room not found"
                )
            )
            return
        }
        
        val players = gameRoomService.getPlayers(roomCode)
        
        // Broadcast to all players in the room
        messagingTemplate.convertAndSend(
            "/topic/room/" + roomCode,
            GameMessage(
                type = MessageType.PLAYER_JOINED,
                roomCode = roomCode,
                playerName = playerName,
                players = players.map { mapOf("id" to it.id, "name" to it.name, "role" to it.role.toString()) },
                message = playerName + " joined the room"
            )
        )
    }

    /**
     * Handles game start request.
     * Only admin can start the game.
     * Client sends: /app/start-game
     * Server broadcasts to: /topic/room/{code}
     */
    @MessageMapping("/start-game")
    fun startGame(message: GameMessage) {
        val roomCode = message.roomCode ?: return
        val playerId = message.playerId ?: return
        
        val started = gameRoomService.startGame(roomCode, playerId)
        
        if (!started) {
            messagingTemplate.convertAndSend(
                "/topic/error/" + roomCode,
                GameMessage(
                    type = MessageType.ERROR,
                    message = "Only admin can start the game"
                )
            )
            return
        }
        
        val players = gameRoomService.getPlayers(roomCode)
        
        // Broadcast game started event
        messagingTemplate.convertAndSend(
            "/topic/room/" + roomCode,
            GameMessage(
                type = MessageType.GAME_STARTED,
                roomCode = roomCode,
                players = players.map { mapOf("id" to it.id, "name" to it.name, "role" to it.role.toString()) },
                message = "Game started!"
            )
        )
        
        // Send individual role assignments to each player
        players.forEach { player ->
            messagingTemplate.convertAndSendToUser(
                player.id,
                "/queue/role-assignment",
                GameMessage(
                    type = MessageType.ROLE_ASSIGNED,
                    playerId = player.id,
                    role = player.role.toString(),
                    message = "You are assigned as: " + player.role.toString()
                )
            )
        }
    }

    /**
     * Handles player leaving a room.
     * Client sends: /app/leave-room
     * Server broadcasts to: /topic/room/{code}
     */
    @MessageMapping("/leave-room")
    fun leaveRoom(message: GameMessage) {
        val playerId = message.playerId ?: return
        val roomCode = message.roomCode ?: return
        
        gameRoomService.removePlayer(playerId)
        
        // Broadcast player left event
        messagingTemplate.convertAndSend(
            "/topic/room/" + roomCode,
            GameMessage(
                type = MessageType.PLAYER_LEFT,
                roomCode = roomCode,
                playerId = playerId,
                message = "Player left the room"
            )
        )
    }
}
