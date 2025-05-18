package com.example.cropbazaar.Home

fun sanitizeEmail(email: String): String {
    return email.trim().lowercase().replace(".", "_")
}

fun unsanitizeEmail(safeEmail: String): String {
    return safeEmail.replace("_", ".")
}

fun sanitizePostId(postId: String): String {
    return postId.trim()
        .replace(" ", "_")
        .replace(":", "-")
        .replace("/", "-")
}

fun generateChatId(postId: String, email1: String, email2: String): String {
    val safePostId = sanitizePostId(postId)
    val user1 = sanitizeEmail(email1)
    val user2 = sanitizeEmail(email2)
    val (sorted1, sorted2) = listOf(user1, user2).sorted()
    return "chat_${safePostId}_${sorted1}_$sorted2"
}