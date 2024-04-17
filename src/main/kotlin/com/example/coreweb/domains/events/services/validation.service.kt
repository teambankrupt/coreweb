package com.example.coreweb.domains.events.services

import com.example.common.validation.ValidationScope
import com.example.common.validation.genericValidation
import com.example.coreweb.domains.events.models.entities.Event

val titleValidation = genericValidation<Event>(
    message = "Title must be at least 3 characters long!",
    instruction = "Please provide a title with at least 3 characters!",
    scopes = setOf(ValidationScope.Write, ValidationScope.Modify)
) {
    it.title.length >= 3
}