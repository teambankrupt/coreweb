package com.example.coreweb.domains.configurations.models.entities

import com.example.coreweb.domains.base.entities.BaseEntityV2
import com.example.coreweb.domains.configurations.models.enums.ConfigurationType
import com.example.coreweb.domains.configurations.models.enums.ValueType
import javax.persistence.*


@Entity
@Table(name = "configurations", schema = "core_web")
class Configuration : BaseEntityV2() {

    @Column(name = "namespace", nullable = false, length = 20)
    lateinit var namespace: String

    @Column(name = "config_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    lateinit var type: ConfigurationType

    @Column(name = "value_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    lateinit var valueType: ValueType

    @Column(name = "key", nullable = false, length = 50)
    lateinit var key: String

    @Column(name = "value", nullable = false)
    lateinit var value: String

    @Column(name = "description")
    var description: String? = null

}