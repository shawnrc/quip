package me.shawnrc.quip.core

data class QuoteRow(
    val id: Int,
    val messageId: Int,
    val createdBy: Int,
    val ordinal: Int,
    val source: Int,
    val createdAt: Long,
    val body: String)
