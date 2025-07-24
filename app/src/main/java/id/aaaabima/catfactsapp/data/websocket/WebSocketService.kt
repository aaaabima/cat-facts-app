package id.aaaabima.catfactsapp.data.websocket

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface WebSocketService {
    @Receive
    fun observerWebSocketEvent(): Flowable<WebSocket.Event>

    @Receive
    fun observeMessages(): Flowable<WebSocketResponse>

    @Send
    fun sendMessage(message: WebSocketClient.SubscriptionMessage)
}