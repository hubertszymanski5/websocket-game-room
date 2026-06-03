package com.gameroom.service

import com.gameroom.model.GameRoom
import com.gameroom.model.GameState
import com.gameroom.model.Player
import com.gameroom.model.PlayerRole
import com.gameroom.util.CodeGenerator
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

/**
 * Service for managing game rooms and players.
 * Handles room creation, player joining/leaving, and game state management.
 */
@Service
class GameRoomService {
    
    // In-memory storage of game rooms (code -> GameRoom)
    private val rooms: MutableMap<String, GameRoom> = ConcurrentHashMap()
    
    // Track which room each player is in (playerId -> roomCode)
    private val playerRooms: MutableMap<String, String> = ConcurrentHashMap()

    /**
     * Creates a new game room with the given admin player.
     * 
     * @param adminName Name of the admin/room creator
     * @return The created GameRoom
     */
    fun createRoom(adminName: String): GameRoom {
        val code = CodeGenerator.generateRoomCode()
        val admin = Player(name = adminName, role = PlayerRole.ADMIN)
        
        val room = GameRoom(
            code = code,
            adminId = admin.id,
            players = mutableMapOf(admin.id to admin)
        )
        
        rooms[code] = room
        playerRooms[admin.id] = code
        
        return room
    }

    /**
     * Joins an existing game room.
     * 
     * @param roomCode The 4-letter room code
     * @param playerName Name of the player joining
     * @return The GameRoom if successful, null if room not found
     */
    fun joinRoom(roomCode: String, playerName: String): GameRoom? {
        val room = rooms[roomCode] ?: return null
        
        val player = Player(name = playerName, role = PlayerRole.NORMAL)
        room.addPlayer(player)
        playerRooms[player.id] = roomCode
        
        return room
    }

    /**
     * Gets a room by its code.
     * 
     * @param roomCode The 4-letter room code
     * @return The GameRoom or null if not found
     */
    fun getRoom(roomCode: String): GameRoom? {
        return rooms[roomCode]
    }

    /**
     * Removes a player from their room.
     * If room becomes empty, delete it.
     * 
     * @param playerId The player to remove
     */
    fun removePlayer(playerId: String) {
        val roomCode = playerRooms[playerId] ?: return
        val room = rooms[roomCode] ?: return
        
        room.removePlayer(playerId)
        playerRooms.remove(playerId)
        
        // Delete room if empty
        if (room.players.isEmpty()) {
            rooms.remove(roomCode)
        }
    }

    /**
     * Starts the game - assigns roles to players.
     * Only admin can start the game.
     * 
     * @param roomCode The room code
     * @param playerId The player requesting to start (must be admin)
     * @return true if game started, false if not admin or room not found
     */
    fun startGame(roomCode: String, playerId: String): Boolean {
        val room = rooms[roomCode] ?: return false
        
        // Only admin can start game
        if (!room.isAdmin(playerId)) return false
        
        room.state = GameState.IN_PROGRESS
        
        // Assign roles: keep admin as ADMIN, rest as NORMAL or TROLL
        val players = room.getPlayerList()
        players.forEach { player ->
            if (player.id != room.adminId) {
                // For this tutorial, everyone is NORMAL
                // In a real game, you might assign TROLL role to random players
                player.role = PlayerRole.NORMAL
            }
        }
        
        return true
    }

    /**
     * Gets all players in a room.
     * 
     * @param roomCode The room code
     * @return List of players or empty list if room not found
     */
    fun getPlayers(roomCode: String): List<Player> {
        return rooms[roomCode]?.getPlayerList() ?: emptyList()
    }

    /**
     * Gets a specific player's role in their room.
     * 
     * @param playerId The player ID
     * @return The player's role or null if player not found
     */
    fun getPlayerRole(playerId: String): PlayerRole? {
        val roomCode = playerRooms[playerId] ?: return null
        val room = rooms[roomCode] ?: return null
        return room.players[playerId]?.role
    }
}
