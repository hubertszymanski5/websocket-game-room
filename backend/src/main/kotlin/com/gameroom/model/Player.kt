package com.gameroom.model

import java.util.UUID

/**
 * Represents a player in the game room.
 * 
 * @param id Unique identifier for the player
 * @param name Display name of the player
 * @param role Current role (ADMIN or NORMAL)
 * @param sessionId WebSocket session ID
 */
data class Player(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    var role: PlayerRole = PlayerRole.NORMAL,
    val sessionId: String? = null
)

/**
 * Player role in the game.
 */
enum class PlayerRole {
    ADMIN,      // Room creator, can start game and assign roles
    NORMAL      // Regular player
}
