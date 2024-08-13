package id.aaaabima.chucknorrisjokes.ui.main

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import id.aaaabima.chucknorrisjokes.ChuckNorrisJokesApplication
import id.aaaabima.chucknorrisjokes.databinding.ActivityMainBinding
import id.aaaabima.chucknorrisjokes.di.component.ApplicationComponent
import id.aaaabima.chucknorrisjokes.di.module.ViewModelFactory
import id.aaaabima.chucknorrisjokes.ui.adapter.JokesAdapter
import id.aaaabima.chucknorrisjokes.ui.viewmodel.MainUiState
import id.aaaabima.chucknorrisjokes.ui.viewmodel.MainViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var applicationComponent: ApplicationComponent
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels { viewModelFactory }
    private var jokeAdapter: JokesAdapter? = null

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
        initAdapter()
        initListener()
    }

    private fun initComponent() {
        applicationComponent = (applicationContext as ChuckNorrisJokesApplication).getApplicationComponent()
        applicationComponent.inject(this)
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
                    }

                    is MainUiState.Success -> {
                        binding.progressBar.isVisible = false
                        jokeAdapter?.submitList(state.jokes)
                    }

                    is MainUiState.Error -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun initAdapter() {
        jokeAdapter = JokesAdapter()
        binding.rvJokes.apply {
            adapter = jokeAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun initListener() {
        binding.edtQuery.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchJokes(v.text.toString())
                hideKeyboard()
                true
            }
            else
                false
        }

        binding.ivSearch.setOnClickListener {
            hideKeyboard()
            viewModel.searchJokes(binding.edtQuery.text.toString())
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.edtQuery.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        jokeAdapter = null
        viewModel.onDispose()
    }
}