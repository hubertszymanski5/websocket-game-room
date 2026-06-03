package com.gameroom.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.config.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    /**
     * Configure the message broker for routing messages.
     * We use a simple in-memory broker for this tutorial.
     * 
     * - enableSimpleBroker: Sets up a simple message broker for /topic and /queue destinations
     * - setApplicationDestinationPrefixes: Sets /app as the prefix for client messages
     */
    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic", "/queue")
        config.setApplicationDestinationPrefixes("/app")
    }

    /**
     * Register STOMP endpoints for WebSocket connections.
     * 
     * - "/ws": The endpoint where clients connect to establish WebSocket connection
     * - setAllowedOrigins("*"): Allow connections from any origin (development only!)
     * - withSockJS(): Enable SockJS fallback for browsers without WebSocket support
     */
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/ws")
            .setAllowedOrigins("*")
            .withSockJS()
    }
}
