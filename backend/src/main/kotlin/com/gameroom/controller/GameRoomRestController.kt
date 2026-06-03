package com.gameroom.controller

import com.gameroom.model.GameRoom
import com.gameroom.service.GameRoomService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST API endpoints for game room operations.
 * Provides HTTP endpoints for creating and joining rooms.
 */
@RestController
@RequestMapping("/api/rooms")
@CrossOrigin("*")
class GameRoomRestController(
    private val gameRoomService: GameRoomService
) {

    /**
     * Create a new game room.
     * POST /api/rooms/create
     */
    @PostMapping("/create")
    fun createRoom(@RequestBody request: CreateRoomRequest): ResponseEntity<GameRoom> {
        val room = gameRoomService.createRoom(request.playerName)
        return ResponseEntity.ok(room)
    }

    /**
     * Join an existing game room.
     * POST /api/rooms/join
     */
    @PostMapping("/join")
    fun joinRoom(@RequestBody request: JoinRoomRequest): ResponseEntity<GameRoom> {
        val room = gameRoomService.joinRoom(request.roomCode, request.playerName)
        return if (room != null) {
            ResponseEntity.ok(room)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Get room details.
     * GET /api/rooms/{code}
     */
    @GetMapping("/{code}")
    fun getRoom(@PathVariable code: String): ResponseEntity<GameRoom> {
        val room = gameRoomService.getRoom(code)
        return if (room != null) {
            ResponseEntity.ok(room)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Get all players in a room.
     * GET /api/rooms/{code}/players
     */
    @GetMapping("/{code}/players")
    fun getPlayers(@PathVariable code: String): ResponseEntity<Any> {
        val room = gameRoomService.getRoom(code)
        return if (room != null) {
            ResponseEntity.ok(mapOf("players" to gameRoomService.getPlayers(code)))
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

/**
 * Request body for creating a room.
 */
data class CreateRoomRequest(
    val playerName: String
)

/**
 * Request body for joining a room.
 */
data class JoinRoomRequest(
    val roomCode: String,
    val playerName: String
)
