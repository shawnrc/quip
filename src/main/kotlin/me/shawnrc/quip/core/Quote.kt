package me.shawnrc.quip.core

data class Quote(
    val id: Int,
    val createdBy: Int,
    val createdAt: Long,
    var messages: MutableList<Message> = ArrayList())
