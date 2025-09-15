package com.aspiring_creators.aichopaicho.data.dto



data class UserDto(
    val id: String = "",
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val isDeleted: Boolean = false,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)


