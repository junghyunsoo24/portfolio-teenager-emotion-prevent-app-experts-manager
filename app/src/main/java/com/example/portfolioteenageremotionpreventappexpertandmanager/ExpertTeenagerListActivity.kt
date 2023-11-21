package com.example.portfolioteenageremotionpreventappexpertandmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portfolioteenageremotionpreventappexpertandmanager.adapter.ExpertTeenagerListAdapter
import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityExpertTeenagerlistBinding
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertTeenagerList.AllocatedTeenager
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertTeenagerList.ExpertTeenagerListApi
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertTeenagerList.ExpertTeenagerListData
import kotlinx.coroutines.launch

class ExpertTeenagerListActivity : AppCompatActivity() {
    private lateinit var viewModel: AppViewModel
    private lateinit var binding: ActivityExpertTeenagerlistBinding
    private lateinit var result: List<AllocatedTeenager>
    private lateinit var id: String
    private lateinit var baseUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExpertTeenagerlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar

        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.actionbar_all)

        val actionBarTitle = actionBar?.customView?.findViewById<TextView>(R.id.actionBarAll)
        actionBarTitle?.text = "할당된청소년 목록조회"

        actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = AppViewModel.getInstance()

        id = viewModel.getUserId().value.toString()

        val layoutManager = LinearLayoutManager(this)
        binding.expertTeenagerListRecyclerView.layoutManager = layoutManager
        val adapter = ExpertTeenagerListAdapter(emptyList()) { allocatedTeenager ->
            viewModel.setTeenagerId(allocatedTeenager.id)
            onTeenagerChatButtonClicked()
        }
        binding.expertTeenagerListRecyclerView.adapter = adapter

        baseUrl = resources.getString(R.string.api_ip_server)
        mobileToServer()
    }

    private fun onTeenagerChatButtonClicked() {
        val intent = Intent(this, ExpertTeenagerStatisticsActivity::class.java)
        startActivity(intent)
    }

    private fun mobileToServer() {
        lifecycleScope.launch {
            try {
                val message = ExpertTeenagerListData(id)
                val response = ExpertTeenagerListApi.retrofitService(baseUrl).sendsMessage(message)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        // 서버 응답을 확인하는 작업 수행
                        val responseData = responseBody.result
                        result = responseData

                        val adapter = binding.expertTeenagerListRecyclerView.adapter as ExpertTeenagerListAdapter
                        adapter.expertTeenagerList = result // 어댑터에 데이터 설정
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
}