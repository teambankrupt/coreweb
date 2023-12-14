package com.example.coreweb.domains.globaladdresss.services.beans

import com.example.coreweb.domains.globaladdresss.models.entities.GlobalAddress
import com.example.coreweb.domains.globaladdresss.repositories.GlobalAddressRepository
import com.example.coreweb.domains.globaladdresss.services.GlobalAddressService
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.utils.PageAttr
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.util.*
import com.example.coreweb.domains.base.models.enums.SortByFields
import org.springframework.data.domain.Sort

@Service
class GlobalAddressServiceBean @Autowired constructor(
        private val globalAddressRepository: GlobalAddressRepository
) : GlobalAddressService {

    override fun search(query: String, page: Int, size: Int, sortBy: SortByFields, direction: Sort.Direction): Page<GlobalAddress> {
        return this.globalAddressRepository.search(query.lowercase(Locale.getDefault()), PageAttr.getPageRequest(page, size, sortBy.fieldName, direction))
    }

    override fun save(entity: GlobalAddress): GlobalAddress {
        this.validate(entity)
        return this.globalAddressRepository.save(entity)
    }

    override fun find(id: Long): Optional<GlobalAddress> {
        return this.globalAddressRepository.find(id)
    }

    override fun delete(id: Long, softDelete: Boolean) {
        if (softDelete) {
            val entity = this.find(id).orElseThrow { ExceptionUtil.notFound("GlobalAddress", id) }
            entity.isDeleted = true
            this.globalAddressRepository.save(entity)
            return
        }
        this.globalAddressRepository.deleteById(id)
    }

    override fun validate(entity: GlobalAddress) {

    }
}
