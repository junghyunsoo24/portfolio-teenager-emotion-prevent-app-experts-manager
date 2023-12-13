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
import com.example.portfolioteenageremotionpreventappexpertandmanager.adapter.ManagerTeenagerListAdapter
import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityManagerTeenagerlistBinding
import com.example.portfolioteenageremotionpreventappexpertandmanager.login.LoginApi
import com.example.portfolioteenageremotionpreventappexpertandmanager.managerTeenagerList.ManagerTeenagerListApi
import com.example.portfolioteenageremotionpreventappexpertandmanager.managerTeenagerList.Teenager
import kotlinx.coroutines.launch

class ManagerTeenagerListActivity : AppCompatActivity(){
    private lateinit var viewModel: AppViewModel
    private lateinit var binding: ActivityManagerTeenagerlistBinding

    private lateinit var result: List<Teenager>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManagerTeenagerlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar

        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.actionbar_all)

        val actionBarTitle = actionBar?.customView?.findViewById<TextView>(R.id.actionBarAll)
        actionBarTitle?.text = "할당받지않은청소년"

        actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = AppViewModel.getInstance()

        val layoutManager = LinearLayoutManager(this)
        binding.managerTeenagerListRecyclerView.layoutManager = layoutManager
        val adapter = ManagerTeenagerListAdapter(emptyList()) { teenager ->
            viewModel.setTeenagerId(teenager.id)
            viewModel.setTeenagerName(teenager.name)
            onExpertListButtonClicked()
        }
        binding.managerTeenagerListRecyclerView.adapter = adapter

        viewModel.setUrl(resources.getString(R.string.api_ip_server))
        mobileToServer()
    }

    private fun onExpertListButtonClicked() {
        val intent = Intent(this, ManagerExpertListActivity::class.java)
        startActivity(intent)
    }

    private fun mobileToServer() {
        lifecycleScope.launch {
            try {
                val response = viewModel.getUrl().value?.let {
                    ManagerTeenagerListApi.retrofitService(it).sendsMessage()
                }
                if (response != null) {
                    if (response.isSuccessful) {
                        val responseBody = response?.body()
                        if (responseBody != null) {
                            val responseData = responseBody.teenagers
                            result = responseData
                            result = result.sortedByDescending { it.percentage }

                            val adapter =
                                binding.managerTeenagerListRecyclerView.adapter as ManagerTeenagerListAdapter
                            adapter.managerTeenagerList = result // 어댑터에 데이터 설정
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