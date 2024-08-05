package com.myappkunjungan.ui.main

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myappkunjungan.R
import com.myappkunjungan.data.response.Visitor
import com.myappkunjungan.data.response.VisitorResponse
import com.myappkunjungan.data.retrofit.ApiConfig
import com.myappkunjungan.databinding.ActivityMainBinding
import com.myappkunjungan.databinding.LayoutDialogContactBinding
import com.myappkunjungan.pref.UserPreference
import com.myappkunjungan.ui.addeditvisitor.AddEditVisitorActivity
import com.myappkunjungan.ui.report.ReportActivity
import com.myappkunjungan.ui.login.LoginActivity
import com.myappkunjungan.ui.regulation.RegulationActivity
import com.myappkunjungan.ui.suggestion.SuggestionActivity
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var role: String? = null
    private lateinit var userPreference: UserPreference
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setPref()
        optionMenu()
        getVisitors()
        swipeRefresh()
        setListener()
    }

    private fun setPref() {
        userPreference = UserPreference(this)
        role = userPreference.getUser()?.role
    }

    private fun optionMenu() {
        binding.apply {
            if (role == "admin") {
                topAppBar.menu.setGroupVisible(R.id.optionMenuReport, true)
            } else {
                topAppBar.menu.setGroupVisible(R.id.optionMenuReport, false)
            }

            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menuReport -> {
                        val intent = Intent(this@MainActivity, ReportActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.menuLogout -> {
                        logout()
                        true
                    }
                    R.id.menuRegulation -> {
                        goToRegulation()
                        true
                    }
                    R.id.menuSuggestion -> {
                        goToSuggestion()
                        true
                    }
                    R.id.menuContact -> {
                        contact()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun setListener() {
        binding.apply {
            fabAdd.setOnClickListener {
                val intent = Intent(this@MainActivity, AddEditVisitorActivity::class.java)
                intent.putExtra("activity", "add")
                startActivity(intent)
            }
        }
    }

    private fun getVisitors() {
        setProgressBar(true)
        binding.swipeRefresh.isRefreshing = false
        val client = ApiConfig.getApiService().getVisitors()
        client.enqueue(object : retrofit2.Callback<VisitorResponse> {
            override fun onResponse(call: Call<VisitorResponse>, response: Response<VisitorResponse>) {
                setProgressBar(false)
                binding.swipeRefresh.isRefreshing = false
                if (response.isSuccessful) {
                    val status = response.body()?.status
                    val data = response.body()?.data

                    if (status == true) {
                        val visitorAdapter = VisitorAdapter(data as ArrayList<Visitor>, userPreference)
                        binding.rvVisitor.visibility = View.VISIBLE
                        binding.rvVisitor.adapter = visitorAdapter
                        binding.rvVisitor.setHasFixedSize(true)
                    }
                }
            }

            override fun onFailure(call: Call<VisitorResponse>, t: Throwable) {
                setProgressBar(false)
                binding.swipeRefresh.isRefreshing = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setProgressBar(isVisible: Boolean) {
        binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun swipeRefresh() {
        binding.apply {
            swipeRefresh.setOnRefreshListener {
                getVisitors()
                rvVisitor.visibility = View.GONE
            }
        }
    }

    private fun contact() {
        val binding: LayoutDialogContactBinding = LayoutDialogContactBinding.inflate(
            LayoutInflater.from(this))
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        binding.apply {
            buttonOk.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.setCancelable(true)
        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun logout() {
        userPreference = UserPreference(this)
        userPreference.clearSession()
        goToLogin()
    }

    private fun goToRegulation() {
        val intent = Intent(this, RegulationActivity::class.java)
        startActivity(intent)
    }

    private fun goToSuggestion() {
        val intent = Intent(this, SuggestionActivity::class.java)
        startActivity(intent)
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }
}