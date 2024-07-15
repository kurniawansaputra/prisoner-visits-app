package com.myappkunjungan.ui.addeditvisitor

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
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
import com.myappkunjungan.data.response.Visitor
import com.myappkunjungan.data.retrofit.ApiConfig
import com.myappkunjungan.databinding.ActivityAddEditVisitorBinding
import com.myappkunjungan.ui.main.MainActivity
import com.myappkunjungan.util.setLoading
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddEditVisitorActivity : AppCompatActivity() {
    private lateinit var activity: String
    private var id: Int = 0
    private lateinit var nik: String
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var dateVisited: String
    private lateinit var binding: ActivityAddEditVisitorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEditVisitorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setPref()
        setToolbar()
        setListener()
        getDetailVisitor()
        optionMenu()
    }

    private fun setPref() {
        activity = intent.getStringExtra("activity").toString()
    }

    private fun setToolbar() {
        binding.apply {
            topAppBar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            topAppBar.title = if (activity == "add") {
                "Registrasi Pengunjung"
            } else {
                "Edit Pengunjung"
            }
        }
    }

    private fun optionMenu() {
        binding.apply {
            topAppBar.menu.findItem(R.id.menuDelete)?.isVisible = activity == "edit"

            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menuDelete -> {
                        deleteVisitor()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setListener() {
        binding.apply {
            editDateVisited.setOnClickListener {
                showDatePickerDialog()
            }

            buttonAddEdit.apply {
                text = if (activity == "add") {
                    "Submit"
                } else {
                    "Edit"
                }

                setOnClickListener {
                    nik = editNik.text.toString()
                    name = editName.text.toString()
                    address = editAddress.text.toString()
                    dateVisited = editDateVisited.text.toString()

                    if (nik.isEmpty() || name.isEmpty() || address.isEmpty() || dateVisited.isEmpty()) {
                        Toast.makeText(
                            this@AddEditVisitorActivity,
                            "Harap lengkapi form terlebih dahulu",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    if (activity == "add") {
                        addVisitor()
                    } else {
                        editVisitor()
                    }
                }
            }
        }
    }

    private fun addVisitor() {
        setLoading(this, true)
        val client = ApiConfig.getApiService().addVisitor(nik, name, address, dateVisited)
        client.enqueue(object : retrofit2.Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                setLoading(this@AddEditVisitorActivity, false)
                if (response.isSuccessful) {
                    val status = response.body()?.status
                    val message = response.body()?.message

                    if (status == true) {
                        goToMain()
                        Toast.makeText(this@AddEditVisitorActivity, message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@AddEditVisitorActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                setLoading(this@AddEditVisitorActivity, false)
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun editVisitor() {
        setLoading(this, true)
        val client = ApiConfig.getApiService().updateVisitor(id, nik, name, address, dateVisited)
        client.enqueue(object : retrofit2.Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                setLoading(this@AddEditVisitorActivity, false)
                if (response.isSuccessful) {
                    val status = response.body()?.status
                    val message = response.body()?.message

                    if (status == true) {
                        goToMain()
                        Toast.makeText(this@AddEditVisitorActivity, message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@AddEditVisitorActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                setLoading(this@AddEditVisitorActivity, false)
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun deleteVisitor() {
        setLoading(this, true)
        val client = ApiConfig.getApiService().deleteVisitor(id)
        client.enqueue(object : retrofit2.Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                setLoading(this@AddEditVisitorActivity, false)
                if (response.isSuccessful) {
                    val status = response.body()?.status
                    val message = response.body()?.message

                    if (status == true) {
                        goToMain()
                        Toast.makeText(this@AddEditVisitorActivity, message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@AddEditVisitorActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                setLoading(this@AddEditVisitorActivity, false)
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun getDetailVisitor() {
        if (activity == "edit") {

            val visitor = if (Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra("EXTRA_VISITOR", Visitor::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("EXTRA_VISITOR")
            }
            if (visitor != null) {
                binding.apply {
                    id = visitor.id

                    editNik.setText(visitor.nik)
                    editName.setText(visitor.name)
                    editAddress.setText(visitor.address)
                    editDateVisited.setText(visitor.dateVisited)
                }
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

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }


    companion object {
        private val TAG = AddEditVisitorActivity::class.java.simpleName
    }
}