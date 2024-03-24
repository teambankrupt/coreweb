package com.example.coreweb.domains.alloweddomains.services

import arrow.core.Option
import com.example.common.utils.Validator
import com.example.coreweb.domains.alloweddomains.models.entities.AllowedDomain
import com.example.common.validation.ValidationScope
import com.example.common.validation.genericValidation
import com.example.coreweb.domains.base.services.validateUniqueOperation

val domainNameValidation = genericValidation<AllowedDomain>(
    message = "Invalid domain name!",
    instruction = "Please provide a valid domain!",
    scopes = setOf(ValidationScope.Write, ValidationScope.Modify)
) {
    Validator.isValidDomain(it.domain)
}

fun domainShouldBeUnique(getDomain: (domain: String) -> Option<AllowedDomain>) = genericValidation<AllowedDomain>(
    message = "Domain must be unique!",
    instruction = "Please provide a unique domain!",
    scopes = setOf(ValidationScope.Write)
) {
    getDomain(it.domain).validateUniqueOperation(it)
}