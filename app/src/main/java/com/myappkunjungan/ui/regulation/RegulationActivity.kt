package com.myappkunjungan.ui.regulation

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myappkunjungan.R
import com.myappkunjungan.databinding.ActivityLoginBinding
import com.myappkunjungan.databinding.ActivityMainBinding
import com.myappkunjungan.databinding.ActivityRegulationBinding

class RegulationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegulationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegulationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setToolbar()
        setWebView()
    }

    private fun setToolbar() {
        binding.apply {
            topAppBar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView() {
        binding.apply {
            webView.settings.javaScriptEnabled = true

            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    setProgressBar(true)
                }

                override fun onPageFinished(view: WebView, url: String) {
                    setProgressBar(false)
                }
            }

            webView.loadUrl("https://polrestasamarinda.id/besuk-tahanan/")
        }
    }

    private fun setProgressBar(condition: Boolean) {
        binding.apply {
            if (condition) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }
}