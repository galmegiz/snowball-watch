package com.fires.common.ampitude.event

import com.fires.common.ampitude.constant.UserEventType
import org.springframework.context.ApplicationEvent

open class UserEvent(
    source: Any,
    val email: String,
    val userEventType: UserEventType
) : ApplicationEvent(source)
