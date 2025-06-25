package id.aaaabima.catfactsapp.data.websocket

interface WebSocketListener {
    fun onConnected()
    fun onMessage(message: String)
    fun onDisconnected()
}