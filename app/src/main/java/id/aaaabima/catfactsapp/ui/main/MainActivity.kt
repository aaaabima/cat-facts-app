package id.aaaabima.catfactsapp.ui.main

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tinder.scarlet.WebSocket
import id.aaaabima.catfactsapp.ChuckNorrisJokesApplication
import id.aaaabima.catfactsapp.data.websocket.WebSocketClient
import id.aaaabima.catfactsapp.data.websocket.WebSocketListener
import id.aaaabima.catfactsapp.databinding.ActivityMainBinding
import id.aaaabima.catfactsapp.di.component.ApplicationComponent
import id.aaaabima.catfactsapp.di.module.ViewModelFactory
import id.aaaabima.catfactsapp.ui.adapter.JokesAdapter
import id.aaaabima.catfactsapp.ui.viewmodel.MainUiState
import id.aaaabima.catfactsapp.ui.viewmodel.MainViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class MainActivity : AppCompatActivity(), WebSocketListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var applicationComponent: ApplicationComponent
    private lateinit var binding: ActivityMainBinding
    private lateinit var webSocketClient: WebSocketClient
    private val viewModel: MainViewModel by viewModels { viewModelFactory }
    private val db = Firebase.firestore
    private val compositeDisposable = CompositeDisposable()

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            lifecycleScope.launch {
                handleDownloadCompleted(context, intent)
            }
        }
    }

    private var jokeAdapter: JokesAdapter? = null
    private var job: Job? = null
    private var downloadId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponent()
        initLifecycleActivity()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(
            this@MainActivity,
            onDownloadComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(onDownloadComplete)
    }

    private fun initComponent() {
        applicationComponent = (applicationContext as ChuckNorrisJokesApplication).getApplicationComponent()
        applicationComponent.inject(this)
        webSocketClient = WebSocketClient("ws://206.189.147.69:6063/ws", application, this)
        observeWebSocketEvents()
        observeMessages()
    }

    private fun initLifecycleActivity() {
        viewModel.uiState.flowWithLifecycle(lifecycle)
            .distinctUntilChanged()
            .onEach { state ->
                when (state) {
                    is MainUiState.None -> {
                        // Nothing
                    }

                    is MainUiState.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.overlay.isVisible = true
                    }

                    is MainUiState.Success -> {
                        binding.progressBar.isVisible = false
                        binding.overlay.isVisible = false
                        binding.tvFact.text = state.jokes.fact
                        if (!isFactExist(state.jokes.fact))
                            db.collection("facts")
                                .add(hashMapOf("facts" to state.jokes.fact))
                                .addOnSuccessListener{ documentReference ->
                                    Log.d("Firestore", documentReference.id)
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Firestore", e.message.toString())
                                }
                    }

                    is MainUiState.Error -> {
                        binding.progressBar.isVisible = false
                        binding.overlay.isVisible = false
                        Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun initListener() {
        binding.ivCat1.setOnClickListener {
//            viewModel.searchJokes()
            webSocketClient.subscribeToDevicesHeartbeat()
        }
        binding.ivCat2.setOnClickListener {
            webSocketClient.connect(this@MainActivity)
        }
        binding.ivCat3.setOnClickListener {
            lifecycleScope.launch {
                webSocketClient.disconnect()
            }
        }
    }

    private fun isFactExist(facts: String): Boolean {
        var exists = false
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Firestore", "${document.id} => ${document.data}")
                    if (document.data["facts"] == facts){
                        exists = true
                        break
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents.", exception)
            }
        return exists
    }

    private fun observeWebSocketEvents() {
        webSocketClient.observeWebSocketEvents()
            .observeOn(Schedulers.single())
            .subscribe { event ->
                when (event) {
                    is WebSocket.Event.OnConnectionOpened<*> -> {
                        Log.d("WebSocket", "Connection opened")
                        showConnectionStatus("Connected")
                        webSocketClient.subscribeToDevicesHeartbeat()
                    }
                    is WebSocket.Event.OnConnectionClosed -> {
                        Log.d("WebSocket", "Connection closed")
                        showConnectionStatus("Disconnected")
                    }
                    is WebSocket.Event.OnConnectionFailed -> {
                        Log.e("WebSocket", "Connection failed: ${event.throwable}")
                        showConnectionStatus("Connection Failed")
                    }
                    is WebSocket.Event.OnMessageReceived -> {
                        Log.d("WebSocket", "Message received: ${event.message}")
                    }
                    else -> {
                        Log.d("WebSocket", "Unknown event: $event")
                    }
                }
            }
            .let { compositeDisposable.add(it) }
    }

    private fun observeMessages() {
        webSocketClient.observeMessages()
            .observeOn(Schedulers.single())
            .subscribe { message ->
                Log.d("WebSocketObserveMessages", "Received message: $message")
            }
            .let { compositeDisposable.add(it) }
    }

    private fun showConnectionStatus(status: String) {
        Log.d("WebSocket","Status: $status")
    }

    private fun downloadApk(context: Context, url: String, fileName: String) {
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setTitle("Updating Application")
            setDescription("Downloading update...")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
            setMimeType("application/vnd.android.package-archive")
        }

        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = manager.enqueue(request)
    }

    private fun installApk(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }

    private fun handleDownloadCompleted(context: Context, intent: Intent) {
        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

        if (id == downloadId) {
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val query = DownloadManager.Query().setFilterById(id)
            val cursor = manager.query(query)

            if (cursor != null && cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    val uriString = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI))
                    val file = File(Uri.parse(uriString).path ?: return)

                    installApk(context, file)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        jokeAdapter = null
        viewModel.onDispose()
    }

    override fun onConnected() {
        println("Connected")
    }

    override fun onMessage(message: String) {
        println("Message: $message")
    }

    override fun onDisconnected() {
        println("Disconnected")
    }
}