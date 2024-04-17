package com.example.coreweb.domains.events.models.dtos

import com.example.coreweb.domains.events.models.entities.Event
import com.example.coreweb.domains.events.models.entities.EventTypes
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class EventBriefResponse(
    val id: Long,

    @field:JsonProperty("created_at")
    val createdAt: Instant,

    @field:JsonProperty("updated_at")
    val updatedAt: Instant? = null,

    val title: String,

    @field:JsonProperty("ref_id")
    val refId: Long?,

    val image: String?,

    val type: EventTypes,

    val active: Boolean,

    @field:JsonProperty("start_at")
    val startAt: Instant,

    @field:JsonProperty("end_at")
    val endAt: Instant?,

    val repetitive: Boolean,

    @field:JsonProperty("repeat_interval")
    val repeatInterval: Long,

    @field:JsonProperty("repeat_count")
    val repeatCount: Int,

    @field:JsonProperty("user_id")
    val userId: Long
)

fun Event.toBriefResponse(): EventBriefResponse =
    EventBriefResponse(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        title = this.title,
        refId = this.refId,
        image = this.image,
        type = this.type,
        active = this.active,
        startAt = this.startAt,
        endAt = this.endAt,
        repetitive = this.repetitive,
        repeatInterval = this.repeatInterval,
        repeatCount = this.repeatCount,
        userId = this.user.id
    )

data class EventDetailResponse(
    val id: Long,

    @field:JsonProperty("created_at")
    val createdAt: Instant,

    @field:JsonProperty("updated_at")
    val updatedAt: Instant? = null,

    val title: String,

    @field:JsonProperty("ref_id")
    val refId: Long?,

    val description: String,

    val image: String?,

    val type: EventTypes,

    @field:JsonProperty("start_at")
    val startAt: Instant,

    @field:JsonProperty("end_at")
    val endAt: Instant?,

    val repetitive: Boolean,

    val active: Boolean,

    @field:JsonProperty("repeat_interval")
    val repeatInterval: Long,

    @field:JsonProperty("repeat_count")
    val repeatCount: Int,

    @field:JsonProperty("scheduler_uid")
    val schedulerUID: String,

    @field:JsonProperty("scheduler_group")
    val schedulerGroup: String,

    @field:JsonProperty("user_id")
    val userId: Long
)

fun Event.toDetailResponse(): EventDetailResponse =
    EventDetailResponse(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        title = this.title,
        refId = this.refId,
        description = this.description,
        image = this.image,
        active = this.active,
        type = this.type,
        startAt = this.startAt,
        endAt = this.endAt,
        repetitive = this.repetitive,
        repeatInterval = this.repeatInterval,
        repeatCount = this.repeatCount,
        schedulerUID = this.schedulerUID,
        schedulerGroup = this.schedulerGroup,
        userId = this.user.id
    )