package com.example.coreweb.domains.globaladdresss.models.dtos

import com.example.coreweb.domains.globaladdresss.models.entities.GlobalAddress
import com.example.coreweb.domains.locations.models.dtos.LocationResponse
import com.example.coreweb.domains.locations.models.dtos.toResponse
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.util.*

data class GlobalAddressResponse(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("created_at")
    val createdAt: Instant,
    @JsonProperty("updated_at")
    val updatedAt: Instant?,
    @JsonProperty("created_by")
    val createdBy: String?,
    @JsonProperty("address_line_one")
    val addressLineOne: String,
    @JsonProperty("address_line_two")
    val addressLineTwo: String?,
    @JsonProperty("zip_code")
    val zipCode: String?,
    @JsonProperty("title")
    val title: String?,
    @JsonProperty("latitude")
    val latitude: Double,
    @JsonProperty("longitude")
    val longitude: Double,
    @JsonProperty("altitude")
    val altitude: Double,
    @JsonProperty("location_id")
    val locationId: Long,
    @JsonProperty("locations_tree")
    val locationsTree: Map<String, LocationResponse>,
    @JsonProperty("full_address")
    val fullAddress: String?
)

fun GlobalAddress.toResponse() = GlobalAddressResponse(
    id = this.id!!,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    createdBy = this.createdBy,
    addressLineOne = this.addressLineOne,
    addressLineTwo = this.addressLineTwo,
    zipCode = this.zipCode,
    title = this.title,
    latitude = this.coordinate.latitude,
    longitude = this.coordinate.longitude,
    altitude = this.coordinate.altitude,
    locationId = this.location.id,
    locationsTree = this.flattenLocation(this.location, Stack())
        .toList()
        .associate { it.type.code to it.toResponse() },
    fullAddress = this.buildAddress()
)