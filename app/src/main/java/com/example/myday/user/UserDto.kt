package com.example.myday.user

data class UserDto(
    var name :String? = null,
    var email :String? = null,
    var password :String? = null,
    var gender :Gender? = null,
    var height :Int? = null,
    var weight :Int? = null,
    var timestamp :Long? = null
)
