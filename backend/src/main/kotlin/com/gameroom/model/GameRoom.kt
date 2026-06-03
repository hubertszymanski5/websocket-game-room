package com.gameroom.model

import java.time.LocalDateTime

/**
 * Represents a game room.
 * 
 * @param code Unique 4-letter room code
 * @param adminId ID of the player who created the room
 * @param players Map of player IDs to Player objects
 * @param state Current state of the game
 * @param createdAt Timestamp when room was created
 */
data class GameRoom(
    val code: String,
    val adminId: String,
    val players: MutableMap<String, Player> = mutableMapOf(),
    var state: GameState = GameState.WAITING_FOR_PLAYERS,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun addPlayer(player: Player) {
        players[player.id] = player
    }

    fun removePlayer(playerId: String) {
        players.remove(playerId)
    }

    fun getPlayerList(): List<Player> {
        return players.values.toList()
    }

    fun isAdmin(playerId: String): Boolean {
        return adminId == playerId
    }
}

/**
 * State of the game room.
 */
enum class GameState {
    WAITING_FOR_PLAYERS,    // Waiting for players to join
    IN_PROGRESS,            // Game is running
    FINISHED                // Game has ended
}
