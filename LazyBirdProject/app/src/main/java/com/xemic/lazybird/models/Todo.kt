package com.xemic.lazybird.models

data class Todo(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
) {
    override fun toString(): String {
        return "${userId}, ${id}, ${title}, ${completed}"
    }
}
