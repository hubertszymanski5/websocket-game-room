package com.gameroom.util

import kotlin.random.Random

/**
 * Utility class for generating random room codes.
 */
object CodeGenerator {
    private const val CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val CODE_LENGTH = 4

    /**
     * Generates a random 4-letter code for game rooms.
     * 
     * Examples: "ABCD", "WXYZ", "JKMP"
     * 
     * @return A random 4-letter string
     */
    fun generateRoomCode(): String {
        return (1..CODE_LENGTH)
            .map { CHARACTERS[Random.nextInt(CHARACTERS.length)] }
            .joinToString("")
    }
}
