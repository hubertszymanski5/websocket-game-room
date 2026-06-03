package com.gameroom

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GameRoomApplication

fun main(args: Array<String>) {
    runApplication<GameRoomApplication>(*args)
}
