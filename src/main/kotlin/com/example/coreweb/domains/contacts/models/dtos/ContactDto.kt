package com.example.coreweb.domains.contacts.models.dtos

import com.example.coreweb.commons.Constants
import com.example.coreweb.domains.base.models.dtos.BaseDto
import com.example.coreweb.domains.globaladdresss.models.entities.GlobalAddress
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

@ApiModel(
    value = Constants.Swagger.CONTACT,
    description = "Api to Create/Read/Update/Delete " + Constants.Swagger.CONTACT
)
class ContactDto : BaseDto() {

    @NotNull
    @ApiModelProperty(example = "Mr. X", required = true)
    lateinit var name: String

    @NotNull
    @ApiModelProperty(example = "01918283837", required = true)
    lateinit var phone: String

    @Email
    @ApiModelProperty(example = "alom@gmal.com")
    var email: String? = null

    var address: MutableList<Long>? = null
}
