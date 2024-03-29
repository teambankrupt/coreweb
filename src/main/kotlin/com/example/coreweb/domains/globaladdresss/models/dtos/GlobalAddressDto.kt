package com.example.coreweb.domains.globaladdresss.models.dtos

import com.example.coreweb.domains.base.models.dtos.BaseDto
import com.example.coreweb.domains.locations.models.dtos.LocationDto
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import java.util.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import kotlin.collections.HashMap

class GlobalAddressDto : BaseDto() {

    @NotBlank(message = "Address line one is required")
    @JsonProperty("address_line_one")
    lateinit var addressLineOne: String

    @JsonProperty("address_line_two")
    var addressLineTwo: String? = null

    @JsonProperty("zip_code")
    var zipCode: String? = null

    var title: String? = null

    @NotNull(message = "Latitude is required")
    @ApiModelProperty(required = true)
    @JsonProperty("latitude")
    var latitude: Double = 0.0

    @NotNull(message = "Longitude is required")
    @ApiModelProperty(required = true)
    @JsonProperty("longitude")
    var longitude: Double = 0.0

    @NotNull(message = "Altitude is required")
    @ApiModelProperty(required = true)
    @JsonProperty("altitude")
    var altitude: Double = 0.0

    @NotNull(message = "location_id is required")
    @Min(1)
    @JsonProperty("location_id")
    var locationId: Long = 0


    // READONLY ITEMS

    @JsonProperty("locations_tree")
    @ApiModelProperty(readOnly = true)
    val locationsTree: HashMap<String, Long> = HashMap()

    @JsonProperty("full_address")
    @ApiModelProperty(readOnly = true)
    var fullAddress: String? = null

}
