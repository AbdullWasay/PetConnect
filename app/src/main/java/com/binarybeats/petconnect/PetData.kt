package com.binarybeats.petconnect

data class PetData(
    var id: String ?= null,
    val breed: String ?= null,
    val description: String ?= null,
    val imageUrl: String ?= null,
    val name: String ?= null,
    val owner: String ?= null,
    val price: String ?= null,
    val size: String ?= null,
    val species: String ?= null,
    val type: String ?= null,
    val weight: String ?= null,
    val startDate: String ?= null,
    val endDate: String ?= null
)

