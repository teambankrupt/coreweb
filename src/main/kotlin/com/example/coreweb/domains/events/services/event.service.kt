package com.example.coreweb.domains.events.services

import arrow.core.Either
import arrow.core.Option
import com.example.auth.entities.UserAuth
import com.example.common.exceptions.Err
import com.example.common.exceptions.toArrow
import com.example.common.utils.ExceptionUtil
import com.example.common.validation.ValidationV2
import com.example.coreweb.domains.base.services.CrudServiceV5
import com.example.coreweb.domains.events.jobs.EventNotifierJob
import com.example.coreweb.domains.events.models.entities.Event
import com.example.coreweb.domains.events.repositories.EventRepository
import com.example.coreweb.scheduling.service.Action
import com.example.coreweb.scheduling.service.Notifier
import com.example.coreweb.scheduling.service.Schedule
import com.example.coreweb.scheduling.service.SchedulerService
import com.example.coreweb.utils.PageAttr
import com.example.coreweb.utils.PageableParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

interface EventService : CrudServiceV5<Event> {
	fun search(
		username: String?,
		fromDate: Instant, toDate: Instant,
		params: PageableParams
	): Page<Event>
}

@Service
open class EventServiceBean @Autowired constructor(
	private val eventRepository: EventRepository,
	private val schedulerService: SchedulerService
) : EventService {
	override fun search(
		username: String?,
		fromDate: Instant,
		toDate: Instant,
		params: PageableParams
	): Page<Event> = this.eventRepository.search(
		query = params.query,
		username = username,
		fromDate = fromDate,
		toDate = toDate,
		pageable = PageAttr.getPageRequest(params)
	)

	@Transactional
	override fun save(entity: Event, asUser: UserAuth): Either<Err.ValidationErr, Event> {
		return super.save(entity, asUser)
			.onRight { this.scheduleEvent(it) }
			.onLeft { throw ExceptionUtil.invalid(it.throwable.message ?: "") }
	}

	private fun scheduleEvent(event: Event, action: () -> Unit = {}) {
		if (!event.active) {
			schedulerService.removeIfExist(
				uid = event.schedulerUID,
				group = event.schedulerGroup
			)
		} else {
			schedulerService.schedule(
				uid = event.schedulerUID,
				group = event.schedulerGroup,
				schedule = Schedule(
					startAt = event.startAt,
					endAt = event.endAt,
					repeat = event.repetitive,
					repeatInterval = event.repeatInterval,
					repeatCount = event.repeatCount
				),
				action = Action(
					jobClass = EventNotifierJob::class.java,
					notifier = Notifier(
						email = event.notificationStrategy.byEmail,
						phone = event.notificationStrategy.byPhone,
						push = event.notificationStrategy.byPushNotification
					),
					data = mapOf(
						"event_id" to event.id,
						"subject" to event.title,
						"message" to event.description,
						"username" to event.user.username,
						"image" to event.image,
						"phone" to event.user.phone,
						"email" to event.user.email,
						"reference_id" to if (event.refId == null) -1 else event.refId,
					)
				)
			)
		}
	}

	override fun validations(asUser: UserAuth): Set<ValidationV2<Event>> = setOf(
		titleValidation,

		)

	override fun find(id: Long, asUser: UserAuth): Option<Event> =
		this.eventRepository.find(id).toArrow()

	override fun getRepository(): JpaRepository<Event, Long> = this.eventRepository

}
