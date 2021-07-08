package com.example.coreweb.domains.profiles.models.mappers

import com.example.coreweb.domains.profiles.models.dtos.ProfileDto
import com.example.coreweb.domains.profiles.models.entities.Profile
import com.example.coreweb.domains.base.models.mappers.BaseMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ProfileMapper @Autowired constructor(
    private val userService: UserService
): BaseMapper<Profile, ProfileDto> {

    override fun map(entity: Profile): ProfileDto {
        val dto = ProfileDto()

        dto.apply {
            this.id = entity.id
            this.createdAt = entity.createdAt
            this.updatedAt = entity.updatedAt

            this.birthday = entity.birthday
            this.photo = entity.photo
            this.gender = entity.gender
            this.bloodGroup = entity.bloodGroup
            this.maritalStatus = entity.maritalStatus
            this.religion = entity.religion
            this.userId = entity.user.id
            this.contactId = entity.contact?.id
        }

        return dto
    }

    override fun map(dto: ProfileDto, exEntity: Profile?): Profile {
        val entity = exEntity ?: Profile()

        entity.apply {
            this.user =
        }

        return entity
    }
}
