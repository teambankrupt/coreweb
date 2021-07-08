package com.example.coreweb.domains.profiles.services

import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.domains.profiles.models.entities.Profile
import com.example.coreweb.domains.base.services.CrudServiceV2
import com.example.coreweb.domains.profiles.models.enums.BloodGroup
import com.example.coreweb.domains.profiles.models.enums.Gender
import com.example.coreweb.domains.profiles.models.enums.MaritalStatus
import com.example.coreweb.domains.profiles.models.enums.Religion
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort

interface ProfileService : CrudServiceV2<Profile> {
    fun search(
        query: String,
        page: Int,
        size: Int,
        gender: Gender?,
        bloodGroup: BloodGroup?,
        maritalStatus: MaritalStatus?,
        religion: Religion,
        userId: Long?,
        username: String?,
        contactId: Long?,
        sortBy: SortByFields,
        direction: Sort.Direction
    ): Page<Profile>
}
