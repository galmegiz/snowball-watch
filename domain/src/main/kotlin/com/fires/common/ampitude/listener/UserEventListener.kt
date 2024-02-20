package com.fires.common.ampitude.listener

import com.fires.common.ampitude.client.AmplitudeClient
import com.fires.common.ampitude.event.UserEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class UserEventListener(
    val amplitudeClient: AmplitudeClient
) {
    val log: Logger get() = LoggerFactory.getLogger(this::class.java.simpleName)

    @Async(value = "threadPoolTaskExecutor")
    @EventListener(UserEvent::class)
    fun sendSignInEvent(event: UserEvent) {
        val result = amplitudeClient.sendEvent(event.email, event.userEventType)
        log.info("[Amplitude] amplitude user event. type : {}, response {}", event.userEventType, result)
    }
}