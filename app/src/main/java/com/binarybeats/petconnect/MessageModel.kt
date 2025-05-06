package com.binarybeats.petconnect


data class MessageModel(
    val senderId: String? = "",
    val receiverId: String = "",
    val message: String? = "",
    val currentTime: String? = "",
    val imageUrl: String? = "",

    val currentDate: String? = "",


    )