package com.example.coreweb.domains.menus.models.enums

import arrow.core.Option
import arrow.core.none
import arrow.core.toOption

enum class MenuTypes {
	BLOG, WEBSITE;

	companion object {
		fun from(str: String?): Option<MenuTypes> = try {
			str.toOption().map { MenuTypes.valueOf(it) }
		} catch (e: Exception) {
			none()
		}
	}
}
