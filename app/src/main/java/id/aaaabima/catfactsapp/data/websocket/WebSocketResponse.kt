package id.aaaabima.catfactsapp.data.websocket

import com.google.gson.JsonObject

data class WebSocketResponse(
    val channel: String,
    val message: String,
    val status: Boolean?,
    val timestamp: String?,
    val data: JsonObject? // or JsonElement
)
