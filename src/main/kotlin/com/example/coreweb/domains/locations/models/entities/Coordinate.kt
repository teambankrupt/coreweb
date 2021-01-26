package com.example.coreweb.domains.locations.models.entities

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Coordinate() {
    @Column(name = "latitude", nullable = false)
    var latitude: Double = 0.0

    @Column(name = "longitude", nullable = false)
    var longitude: Double = 0.0

    @Column(name = "altitude", nullable = false)
    var altitude: Double = 0.0

    constructor(latitude: Double, longitude: Double, altitude: Double) : this() {
        this.latitude = latitude
        this.longitude = longitude
        this.altitude = altitude
    }
}