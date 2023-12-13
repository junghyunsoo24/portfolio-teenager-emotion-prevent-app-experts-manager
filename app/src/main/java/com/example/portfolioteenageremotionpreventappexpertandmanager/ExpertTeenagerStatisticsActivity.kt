package com.example.portfolioteenageremotionpreventappexpertandmanager

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portfolioteenageremotionpreventappexpertandmanager.adapter.TeenagerStatisticsAdapter
import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityExpertTeenagerStatisticsBinding
import com.example.portfolioteenageremotionpreventappexpertandmanager.teenagerStatistics.ExpertTeenagerStatisticsApi
import com.example.portfolioteenageremotionpreventappexpertandmanager.teenagerStatistics.StatisticsData
import com.example.portfolioteenageremotionpreventappexpertandmanager.teenagerStatistics.Statistics
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ExpertTeenagerStatisticsActivity : AppCompatActivity() {
    private lateinit var viewModel: AppViewModel
    private lateinit var binding: ActivityExpertTeenagerStatisticsBinding
    private lateinit var result: List<Statistics>
    private lateinit var id: String
    private lateinit var baseUrl: String

    private lateinit var teenID: String
    private lateinit var startDate: String
    private lateinit var endDate: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExpertTeenagerStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar

        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.actionbar_all)

        val actionBarTitle = actionBar?.customView?.findViewById<TextView>(R.id.actionBarAll)
        actionBarTitle?.text = "감정 통계 조회"

        actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = AppViewModel.getInstance()

        id = viewModel.getUserId().value.toString()
        teenID = viewModel.getTeenagerId().value.toString()

        viewModel.setCurrentDate(getCurrentDate())
        startDate = getLastWeekDate()
        endDate = getNextDayDate()

        val layoutManager = LinearLayoutManager(this)
        binding.expertTeenagerStatisticsRecyclerView.layoutManager = layoutManager
        val adapter = TeenagerStatisticsAdapter(emptyList()) {
            onChildChatButtonClicked()
        }
        binding.expertTeenagerStatisticsRecyclerView.adapter = adapter

        val showCalendarButton: Button = findViewById(R.id.showCalendarButton)
        showCalendarButton.setOnClickListener {
            showCalendar()
        }

        baseUrl = resources.getString(R.string.api_ip_server)
        mobileToServer()
    }

    private fun showCalendar() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selection ->
            val startDateMillis = selection.first
            val endDateMillis = selection.second

            val startsDate = Date(startDateMillis)
            val endsDate = Date(endDateMillis)

            val startCalendar = Calendar.getInstance()
            startCalendar.time = startsDate

            val endCalendar = Calendar.getInstance()
            endCalendar.time = endsDate
            endCalendar.add(Calendar.DAY_OF_MONTH, 1)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            startDate = dateFormat.format(startCalendar.time)
            endDate = dateFormat.format(endCalendar.time)

            mobileToServer()
        }

        picker.addOnNegativeButtonClickListener {
        }

        picker.show(supportFragmentManager, picker.toString())
    }

    private fun onChildChatButtonClicked() {
        val intent = Intent(this, ExpertTeenagerChatActivity::class.java)
        startActivity(intent)
    }

    private fun mobileToServer() {
        lifecycleScope.launch {
            try {
                val message = StatisticsData(teenID, startDate, endDate)
                val response = ExpertTeenagerStatisticsApi.retrofitService(baseUrl).sendsMessage(message)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val responseData = responseBody.statistics
                        result = responseData

                        val sortedResult = responseData.sortedByDescending { it.date }
                        result = sortedResult

                        val adapter = binding.expertTeenagerStatisticsRecyclerView.adapter as TeenagerStatisticsAdapter
                        adapter.teenagerStatistics = result
                        adapter.notifyDataSetChanged()

                    } else {
                        Log.e("@@@@Error3", "Response body is null")
                    }
                } else {
                    Log.e("@@@@Error2", "Response not successful: ${response.code()}")
                }
            } catch (Ex: Exception) {
                Log.e("@@@@Error1", Ex.stackTraceToString())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.myPage_btn -> {
                val intent = Intent(this, ExpertInfoListActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date(System.currentTimeMillis())
        return dateFormat.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getLastWeekDate(): String {
        val currentDate = LocalDate.now()
        val lastWeekDate = currentDate.minusWeeks(1)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())

        return lastWeekDate.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNextDayDate(): String {
        val currentDate = LocalDate.now()
        val nextDayDate = currentDate.plusDays(1)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())

        return nextDayDate.format(formatter)
    }
}