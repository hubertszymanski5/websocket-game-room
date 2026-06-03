package com.gameroom.model

/**
 * Represents a message between client and server.
 * Uses a flexible structure to support different message types.
 */
data class GameMessage(
    val type: String,          // Message type: CREATE_ROOM, JOIN_ROOM, START_GAME, etc.
    val playerId: String? = null,
    val playerName: String? = null,
    val roomCode: String? = null,
    val role: String? = null,
    val players: List<Map<String, String>>? = null,
    val message: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Message types for communication.
 */
object MessageType {
    // Client -> Server
    const val CREATE_ROOM = "CREATE_ROOM"
    const val JOIN_ROOM = "JOIN_ROOM"
    const val START_GAME = "START_GAME"
    const val LEAVE_ROOM = "LEAVE_ROOM"

    // Server -> Client (broadcast)
    const val ROOM_CREATED = "ROOM_CREATED"
    const val PLAYER_JOINED = "PLAYER_JOINED"
    const val PLAYER_LEFT = "PLAYER_LEFT"
    const val ROLE_ASSIGNED = "ROLE_ASSIGNED"
    const val GAME_STARTED = "GAME_STARTED"
    const val GAME_STATE_UPDATE = "GAME_STATE_UPDATE"
    const val ERROR = "ERROR"
}
