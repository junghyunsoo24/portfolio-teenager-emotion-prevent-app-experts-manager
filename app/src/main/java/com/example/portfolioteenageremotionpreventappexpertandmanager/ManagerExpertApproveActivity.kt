package com.example.portfolioteenageremotionpreventappexpertandmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portfolioteenageremotionpreventappexpertandmanager.adapter.ManagerExpertApproveAdapter
import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityManagerExpertapproveBinding
import com.example.portfolioteenageremotionpreventappexpertandmanager.managerExpertApprove.Expert
import com.example.portfolioteenageremotionpreventappexpertandmanager.managerExpertApprove.ManagerExpertApproveApi
import kotlinx.coroutines.launch

class ManagerExpertApproveActivity : AppCompatActivity(){
    private lateinit var viewModel: AppViewModel
    private lateinit var binding: ActivityManagerExpertapproveBinding

    private lateinit var result: List<Expert>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManagerExpertapproveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar

        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.actionbar_all)

        val actionBarTitle = actionBar?.customView?.findViewById<TextView>(R.id.actionBarAll)
        actionBarTitle?.text = "승인받지않은전문가목록"

        actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = AppViewModel.getInstance()

        val layoutManager = LinearLayoutManager(this)
        binding.managerExpertApproveRecyclerView.layoutManager = layoutManager
        val adapter = ManagerExpertApproveAdapter(emptyList()) {expert ->
            viewModel.setExpertId(expert.id)
        }
        binding.managerExpertApproveRecyclerView.adapter = adapter

        viewModel.setUrl(resources.getString(R.string.api_ip_server))
        mobileToServer()
    }

    private fun mobileToServer() {
        lifecycleScope.launch {
            try {
                val response = viewModel.getUrl().value?.let {
                    ManagerExpertApproveApi.retrofitService(it).sendsMessage()
                }
                if (response != null) {
                    if (response.isSuccessful) {
                        val responseBody = response?.body()
                        if (responseBody != null) {
                            // 서버 응답을 확인하는 작업 수행
                            val responseData = responseBody.experts
                            result = responseData

                            val adapter = binding.managerExpertApproveRecyclerView.adapter as ManagerExpertApproveAdapter
                            adapter.expertList = result // 어댑터에 데이터 설정
                            adapter.notifyDataSetChanged()

                        } else {
                            Log.e("@@@@Error3", "Response body is null")
                        }
                    } else {
                        Log.e("@@@@Error2", "Response not successful: ${response.code()}")
                    }
                }
            } catch (Ex: Exception) {
                Log.e("@@@@Error1", Ex.stackTraceToString())
            }
        }
    }
}