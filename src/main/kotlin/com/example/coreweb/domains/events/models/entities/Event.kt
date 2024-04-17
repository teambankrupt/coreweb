package com.example.coreweb.domains.events.models.entities

import com.example.auth.entities.User
import com.example.coreweb.domains.base.entities.BaseEntityV2
import java.time.Instant
import javax.persistence.*

enum class EventTypes {
    TASK
}

const val EVENT_SCHEDULER_GROUP = "EVENT_SCHEDULER_GROUP"

@Entity
@Table(name = "events", schema = "core_web")
class Event : BaseEntityV2() {

    @Column(name = "title", nullable = false)
    var title: String = ""

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    var description: String = ""

    @Column(name = "image")
    var image: String? = null

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    var type: EventTypes = EventTypes.TASK

    @Column(name = "ref_id")
    var refId: Long? = null

    @Column(name = "active")
    var active: Boolean = true

    @Column(name = "start_at", nullable = false)
    lateinit var startAt: Instant

    @Column(name = "end_at")
    var endAt: Instant? = null

    var repetitive: Boolean = false

    @Column(name = "repeat_interval", nullable = false)
    var repeatInterval: Long = 0

    @Column(name = "repeat_count", nullable = false)
    var repeatCount: Int = 0

    @Column(name = "scheduler_uid", nullable = false)
    lateinit var schedulerUID: String

    @Column(name = "scheduler_group", nullable = false)
    lateinit var schedulerGroup: String

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: User
}