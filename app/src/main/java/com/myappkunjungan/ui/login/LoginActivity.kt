package com.myappkunjungan.ui.login

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myappkunjungan.R
import com.myappkunjungan.data.response.DefaultResponse
import com.myappkunjungan.data.retrofit.ApiConfig
import com.myappkunjungan.databinding.ActivityLoginBinding
import com.myappkunjungan.pref.UserPreference
import com.myappkunjungan.ui.main.MainActivity
import com.myappkunjungan.util.setLoading
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LoginActivity : AppCompatActivity() {
    private lateinit var username: String
    private lateinit var dateVisited: String
    private lateinit var userPreference: UserPreference
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userPreference = UserPreference(this)
        val sessionToken = userPreference.getSessionToken()

        if (sessionToken != null) {
            goToMain()
        }

        setListener()
    }

    private fun setListener() {
        binding.apply {
            editDateVisited.setOnClickListener {
                showDatePickerDialog()
            }

            buttonSignIn.setOnClickListener {
                username = binding.editUsername.text.toString()
                dateVisited = binding.editDateVisited.text.toString()

                if (username.isEmpty() || dateVisited.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Username dan tanggal kunjungan tidak boleh kososng", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                login()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateVisited = dateFormat.format(selectedDate.time)

            binding.editDateVisited.setText(dateVisited)
        }, year, month, dayOfMonth)

        datePickerDialog.show()
    }

    private fun login() {
        setLoading(this, true)
        val client = ApiConfig.getApiService().login(username, dateVisited)
        client.enqueue(object : retrofit2.Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                setLoading(this@LoginActivity, false)
                if (response.isSuccessful) {
                    val status = response.body()?.status
                    val message = response.body()?.message

                    if (status == true) {
                        userPreference.saveSessionToken(username)

                        goToMain()
                    } else {
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                setLoading(this@LoginActivity, false)
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }

}