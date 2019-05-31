package com.fithian.dan.blackmagic

import java.io.Serializable
import java.util.UUID

class Teammate : Serializable {
    var name: String
    var email: String
    var include: Boolean
    var uuid: String
    constructor(name: String, email: String, include: Boolean) {
        this.name = name
        this.email = email
        this.include = include
        this.uuid = UUID.randomUUID().toString()
    }
}