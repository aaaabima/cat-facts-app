package id.aaaabima.catfactsapp.data.websocket

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import com.google.gson.Gson
import com.tinder.scarlet.Message
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.websocket.DefaultClientWebSocketSession
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.reactivex.Flowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class WebSocketClient(
    private val baseUrl: String,
    private val application: Application,
    private val lifecycleOwner: LifecycleOwner
) {
    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    private var session: DefaultClientWebSocketSession? = null
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun connect(listener: WebSocketListener) {
        job = scope.launch {
            try {
                client.webSocketSession { url(baseUrl) }.let { wsSession ->
                    session = wsSession
                    listener.onConnected()

                    try {
                        for (frame in wsSession.incoming) {
                            if (frame is Frame.Text) {
                                listener.onMessage(frame.readText())
                            }
                        }
                    } catch (e: Exception) {
                        listener.onDisconnected()
                    }
                }
            } catch (e: Exception) {
                listener.onDisconnected()
            }
        }
    }

    fun sendMessage() {
        scope.launch {
            session?.send(Frame.Text("{channel:\"update_application_version\"}"))
        }
    }

    suspend fun disconnect() {
        job?.cancel()  // cancel the coroutine
        scope.coroutineContext.cancelChildren()  // cancel all child jobs
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Client disconnect"))
        session = null
        job = null
    }

    private val okHttpClient = OkHttpClient.Builder().build()

    private val scarletInstance = Scarlet.Builder()
        .webSocketFactory(okHttpClient.newWebSocketFactory("ws://206.189.147.69:6063/ws"))
        .addMessageAdapterFactory(GsonMessageAdapter.Factory())
        .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
        .lifecycle(AndroidLifecycle.ofLifecycleOwnerForeground(application, lifecycleOwner))
        .build()

    private val webSocketService = scarletInstance.create<WebSocketService>()

    data class SubscriptionMessage(
        val channel: String
    )

    fun subscribeToChannel(channel: String) {
        val subscriptionMessage = SubscriptionMessage(
            channel = channel
        )
        webSocketService.sendMessage(subscriptionMessage)
    }

    fun subscribeToDevicesHeartbeat() {
        subscribeToChannel("update_application_version")
    }

    fun observeWebSocketEvents(): Flowable<WebSocket.Event> =
        webSocketService.observerWebSocketEvent()

    fun observeMessages(): Flowable<String> =
        webSocketService.observeMessages()

}