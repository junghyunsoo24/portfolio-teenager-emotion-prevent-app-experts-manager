package com.example.portfolioteenageremotionpreventappexpertandmanager

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portfolioteenageremotionpreventappexpertandmanager.adapter.ManagerExpertListAdapter
import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityManagerExpertlistBinding
import com.example.portfolioteenageremotionpreventappexpertandmanager.managerAllocate.AllocateApi
import com.example.portfolioteenageremotionpreventappexpertandmanager.managerAllocate.AllocateData
import com.example.portfolioteenageremotionpreventappexpertandmanager.managerExpertList.ApproveExpert
import com.example.portfolioteenageremotionpreventappexpertandmanager.managerExpertList.ManagerExpertListApi

import kotlinx.coroutines.launch

class ManagerExpertListActivity : AppCompatActivity() {
    private lateinit var viewModel: AppViewModel
    private lateinit var binding: ActivityManagerExpertlistBinding

    private lateinit var result: List<ApproveExpert>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManagerExpertlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar

        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.actionbar_all)

        val actionBarTitle = actionBar?.customView?.findViewById<TextView>(R.id.actionBarAll)
        actionBarTitle?.text = "승인된전문가목록"

        actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = AppViewModel.getInstance()

        val layoutManager = LinearLayoutManager(this)
        binding.managerExpertListRecyclerView.layoutManager = layoutManager
        val adapter = ManagerExpertListAdapter(emptyList()) { approveExpert ->
            viewModel.setApproveExpertId(approveExpert.id)
            viewModel.setUrl(resources.getString(R.string.api_ip_server))
            allocateDataToServer()

        }
        binding.managerExpertListRecyclerView.adapter = adapter

        viewModel.setUrl(resources.getString(R.string.api_ip_server))
        mobileToServer()
    }

    private fun allocateDataToServer() {
        lifecycleScope.launch {
            try {
                val message = AllocateData(viewModel.getTeenagerId().value.toString(), viewModel.getApproveExpertId().value.toString())
                val response = viewModel.getUrl().value?.let {
                    AllocateApi.retrofitService(it).sendsMessage(message)
                }
                if (response != null) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val responseData = responseBody.result
                            showAlertDialog()

                        } else {
                            Log.e("@@@@Error3", "Response body is null")
                        }
                    } else {
                        showAlertDialog()
                        Log.e("@@@@Error2", "Response not successful: ${response.code()}")
                    }
                }
            } catch (Ex: Exception) {
                Log.e("@@@@Error1", Ex.stackTraceToString())
            }
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("청소년 할당 성공!")
        builder.setMessage("청소년: " + viewModel.getTeenagerId().value +
                "\n" + "전문가: " + viewModel.getApproveExpertId().value)

        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun mobileToServer() {
        lifecycleScope.launch {
            try {
                val response = viewModel.getUrl().value?.let {
                    ManagerExpertListApi.retrofitService(it).sendsMessage()
                }
                if (response != null) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            // 서버 응답을 확인하는 작업 수행
                            val responseData = responseBody.authorizedExperts
                            result = responseData

                            val adapter =
                                binding.managerExpertListRecyclerView.adapter as ManagerExpertListAdapter
                            adapter.approveExpertList = result // 어댑터에 데이터 설정
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