package com.example.coreweb.domains.configurations.services

import arrow.core.Either
import arrow.core.Option
import com.example.auth.entities.UserAuth
import com.example.common.exceptions.Err
import com.example.common.exceptions.toArrow
import com.example.common.validation.ValidationV2
import com.example.coreweb.domains.base.services.CrudServiceV5
import com.example.coreweb.domains.configurations.models.entities.Configuration
import com.example.coreweb.domains.configurations.repositories.ConfigurationRepository
import com.example.coreweb.utils.PageAttr
import com.example.coreweb.utils.PageableParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import java.time.Instant


interface ConfigurationService : CrudServiceV5<Configuration> {
	fun search(
		username: String?,
		fromDate: Instant, toDate: Instant,
		params: PageableParams
	): Page<Configuration>

	fun upsertAll(
		configurations: List<Configuration>,
		asUser: UserAuth
	): Either<Err.ValidationErr, List<Configuration>>
}

@Service
class ConfigurationServiceBean @Autowired constructor(
	private val configurationRepository: ConfigurationRepository
) : ConfigurationService {

	override fun search(
		username: String?,
		fromDate: Instant,
		toDate: Instant,
		params: PageableParams
	): Page<Configuration> = this.configurationRepository.search(
		query = params.query,
		username = username,
		fromDate = fromDate,
		toDate = toDate,
		pageable = PageAttr.getPageRequest(params)
	)

	override fun upsertAll(
		configurations: List<Configuration>,
		asUser: UserAuth
	): Either<Err.ValidationErr, List<Configuration>> =
		configurations.map { config ->
			this.configurationRepository.findByNamespaceAndTypeAndKey(
				namespace = config.namespace,
				type = config.type,
				key = config.key
			).toArrow().fold(
				{ config }
			) { ex: Configuration ->
				ex.apply {
					this.value = config.value
					this.description = config.description
				}
				ex
			}
		}.let { updatedConfigs ->
			this.saveAll(updatedConfigs, asUser = asUser)
		}

	override fun validations(asUser: UserAuth): Set<ValidationV2<Configuration>> = setOf(
		valueValidation,
		uniqueKeyValidation { namespace, type, key ->
			this.configurationRepository.findByNamespaceAndTypeAndKey(
				namespace = namespace,
				type = type,
				key = key
			).toArrow()
		}
	)

	override fun find(id: Long, asUser: UserAuth): Option<Configuration> =
		this.configurationRepository.find(id).toArrow()

	override fun getRepository(): JpaRepository<Configuration, Long> =
		this.configurationRepository

}
