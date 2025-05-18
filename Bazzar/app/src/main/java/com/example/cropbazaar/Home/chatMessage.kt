package com.example.cropbazaar.Home
import android.net.Uri

data class ChatNavArgs(
    val postId: String,
    val chatId: String,
    val receiverEmail: String,
    val imgUrl: String
)

fun encodeChatArgs(args: ChatNavArgs): String {
    val combined = "${args.postId}|${args.chatId}|${args.receiverEmail}|${args.imgUrl}"
    return Uri.encode(combined)
}

fun decodeChatArgs(encoded: String): ChatNavArgs {
    val decoded = Uri.decode(encoded)
    val parts = decoded.split("|")
    return ChatNavArgs(
        postId = parts.getOrElse(0) { "" },
        chatId = parts.getOrElse(1) { "" },
        receiverEmail = parts.getOrElse(2) { "" },
        imgUrl = parts.getOrElse(3) { "" }
    )
}
