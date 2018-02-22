package me.shawnrc.quip.core

data class Quote(
    val id: Int,
    val createdBy: Int,
    val createdAt: Long,
    val messages: MutableList<Message> = ArrayList(),  // TODO make this not mutable
    val upvotes: Int,
    val downvotes: Int)  // TODO convert to score rather than raw votes
