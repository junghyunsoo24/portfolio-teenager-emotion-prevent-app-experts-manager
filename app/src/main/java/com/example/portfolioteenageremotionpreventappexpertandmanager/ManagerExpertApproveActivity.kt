package com.example.portfolioteenageremotionpreventappexpertandmanager

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portfolioteenageremotionpreventappexpertandmanager.adapter.ExpertApproveAdapter
import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityManagerExpertapproveBinding
import com.example.portfolioteenageremotionpreventappexpertandmanager.managerApprove.ManagerApproveApi
import com.example.portfolioteenageremotionpreventappexpertandmanager.managerApprove.ApproveData
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertApprove.Expert
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertApprove.ManagerExpertApproveApi
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
        val adapter = ExpertApproveAdapter(emptyList()) { expert ->
            viewModel.setExpertId(expert.id)
            approveToServer()
        }
        binding.managerExpertApproveRecyclerView.adapter = adapter

        viewModel.setUrl(resources.getString(R.string.api_ip_server))
        mobileToServer()
    }

    private fun approveToServer() {
        lifecycleScope.launch {
            try {
                val message = ApproveData(viewModel.getExpertId().value.toString())
                val response = viewModel.getUrl().value?.let {
                    ManagerApproveApi.retrofitService(it).sendsMessage(message)
                }
                if (response != null) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val responseData = responseBody.result
                            viewModel.setApproveExpertId(viewModel.getExpertId().value.toString())
                            viewModel.removeExpertId(viewModel.getApproveExpertId().value.toString())
                            showAlertDialog(viewModel.getApproveExpertId().value.toString())
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

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("전문가: $message")
        builder.setMessage("전문가 승인 성공")

        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
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
                            val responseData = responseBody.experts
                            result = responseData

                            val adapter = binding.managerExpertApproveRecyclerView.adapter as ExpertApproveAdapter
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