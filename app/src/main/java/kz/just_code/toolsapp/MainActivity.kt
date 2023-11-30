package kz.just_code.toolsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.yariksoffice.lingver.Lingver
import kotlinx.coroutines.launch
import kz.just_code.toolsapp.databinding.ActivityMainBinding
import timber.log.Timber
import java.lang.Exception
import java.time.temporal.Temporal

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val adapter = NameAdapter()
    private val viewModel = MainViewModel()
    private var refreshingMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.en.setOnClickListener {
          //  setTheme(Theme.LIGHT)
            Lingver.getInstance().setLocale(this, "en")
          //  this.recreate()
        }

        binding.de.setOnClickListener {
            //setTheme(Theme.SYSTEM)
            Lingver.getInstance().setLocale(this, "de")
           // this.recreate()
        }

        binding.kz.setOnClickListener {
           // setTheme(Theme.DARK)
            Lingver.getInstance().setLocale(this, "kk")
            //this.recreate()

            binding.refresh.isRefreshing = false
        }
        
        binding.refresh.setOnRefreshListener {
            refreshingMode = true
            adapter.refresh()
            //Toast.makeText(this, "Strart refreshing", Toast.LENGTH_SHORT).show()
        }

        binding.refresh.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.purple),
            ContextCompat.getColor(this, R.color.orange),
            ContextCompat.getColor(this, R.color.blue),
            ContextCompat.getColor(this, R.color.black)
        )

        binding.listView.layoutManager = LinearLayoutManager(this)
        binding.listView.adapter = adapter

        viewModel.names.observe(this) {
            adapter.submitData(lifecycle, it)
        }


        adapter.addLoadStateListener {
            binding.refresh.isRefreshing = it.source.refresh is LoadState.Loading && refreshingMode
        }
    }

    fun setTheme(theme: Theme) {
        AppCompatDelegate.setDefaultNightMode(theme.system)
    }
}