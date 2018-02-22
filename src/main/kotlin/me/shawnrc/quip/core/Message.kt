package me.shawnrc.quip.core

data class Message(
    val source: Int,
    val body: String,
    val quoteId: Int,
    val id: Int,
    val ordinal: Int)
