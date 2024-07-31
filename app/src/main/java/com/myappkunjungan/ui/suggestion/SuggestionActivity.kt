package com.myappkunjungan.ui.suggestion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myappkunjungan.R
import com.myappkunjungan.data.response.DefaultResponse
import com.myappkunjungan.data.response.Suggestion
import com.myappkunjungan.data.response.SuggestionResponse
import com.myappkunjungan.data.retrofit.ApiConfig
import com.myappkunjungan.databinding.ActivitySuggestionBinding
import com.myappkunjungan.ui.main.MainActivity
import com.myappkunjungan.util.setLoading
import retrofit2.Call
import retrofit2.Response

class SuggestionActivity : AppCompatActivity() {
    private lateinit var suggestion: String
    private lateinit var binding: ActivitySuggestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySuggestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setToolbar()
        getSuggestions()
        setListener()
    }

    private fun setToolbar() {
        binding.apply {
            topAppBar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setListener() {
        binding.apply {
            buttonSubmit.setOnClickListener {
                suggestion = binding.editSuggestion.text.toString()

                if (suggestion.isEmpty()) {
                    Toast.makeText(this@SuggestionActivity, "Kritik dan saran tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                submitSuggestion()
            }
        }
    }

    private fun submitSuggestion() {
        setLoading(this, true)
        val client = ApiConfig.getApiService().addSuggestion(suggestion)
        client.enqueue(object : retrofit2.Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                setLoading(this@SuggestionActivity, false)
                if (response.isSuccessful) {
                    val status = response.body()?.status
                    val message = response.body()?.message

                    if (status == true) {
                        goToMain()
                        Toast.makeText(this@SuggestionActivity, message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@SuggestionActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                setLoading(this@SuggestionActivity, false)
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun getSuggestions() {
        setProgressBar(true)
        val client = ApiConfig.getApiService().getSuggestions()
        client.enqueue(object : retrofit2.Callback<SuggestionResponse> {
            override fun onResponse(call: Call<SuggestionResponse>, response: Response<SuggestionResponse>) {
                setProgressBar(false)
                if (response.isSuccessful) {
                    val status = response.body()?.status
                    val data = response.body()?.data

                    if (status == true) {
                        val suggestionAdapter = SuggestionAdapter(data as ArrayList<Suggestion>)
                        binding.rvSuggestion.visibility = View.VISIBLE
                        binding.rvSuggestion.adapter = suggestionAdapter
                        binding.rvSuggestion.setHasFixedSize(true)
                    }
                }
            }

            override fun onFailure(call: Call<SuggestionResponse>, t: Throwable) {
                setProgressBar(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setProgressBar(isVisible: Boolean) {
        binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    companion object {
        private val TAG = SuggestionActivity::class.java.simpleName
    }
}