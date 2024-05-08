package com.example.coreweb.domains.events.models.dtos

import arrow.core.Option
import arrow.core.getOrElse
import com.example.auth.entities.User
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.events.models.entities.EVENT_SCHEDULER_GROUP
import com.example.coreweb.domains.events.models.entities.Event
import com.example.coreweb.domains.events.models.entities.EventTypes
import com.example.coreweb.domains.events.models.entities.NotificationStrategy
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.util.*
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class EventReq(
    @NotBlank
    val title: String,

    val description: String,

    val image: String?,

    @NotNull(message = "type is required!")
    val type: EventTypes,

    @field:JsonProperty("ref_id")
    val refId: Long?,

    val active: Boolean,

    @NotNull
    @FutureOrPresent
    @field:JsonProperty("start_at")
    val startAt: Instant,

    @field:JsonProperty("end_at")
    val endAt: Instant,

    val repetitive: Boolean,

    @field:JsonProperty("repeat_interval")
    val repeatInterval: Long,

    @field:JsonProperty("repeat_count")
    val repeatCount: Int,

    @field:JsonProperty("notification_strategy")
    val notificationStrategy: NotificationStrategyReq,

    @field:JsonProperty("user_id")
    val userId: Long
) {
    fun asEvent(
        event: Event = Event(),
        getUser: (Long) -> Option<User>
    ): Event =
        this.let { req ->
            event.apply {
                this.title = req.title
                this.description = req.description
                this.image = req.image
                this.type = req.type
                this.active = req.active
                this.refId = req.refId
                this.startAt = req.startAt
                this.endAt = req.endAt
                this.repetitive = req.repetitive
                this.repeatInterval = req.repeatInterval
                this.repeatCount = req.repeatCount
                this.schedulerUID = UUID.randomUUID().toString()
                this.schedulerGroup = EVENT_SCHEDULER_GROUP
                this.notificationStrategy = req.notificationStrategy.asStrategy()
                this.user = getUser(req.userId).getOrElse {
                    throw ExceptionUtil.notFound(User::class.java, req.userId)
                }
            }
        }
}

data class NotificationStrategyReq(
    @field:JsonProperty("by_email")
    val byEmail: Boolean,

    @field:JsonProperty("by_phone")
    val byPhone: Boolean,

    @field:JsonProperty("by_push_notification")
    val byPushNotification: Boolean
) {
    fun asStrategy() = this.let { req ->
        NotificationStrategy().apply {
            this.byEmail = req.byEmail
            this.byPushNotification = req.byPushNotification
            this.byPhone = req.byPhone
        }
    }
}