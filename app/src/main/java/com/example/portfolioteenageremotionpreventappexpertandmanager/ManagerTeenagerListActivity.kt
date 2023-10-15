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
import com.example.portfolioteenageremotionpreventappexpertandmanager.managerTeenagerList.ManagerTeenagerListApi
import com.example.portfolioteenageremotionpreventappexpertandmanager.managerTeenagerList.Teenager
import kotlinx.coroutines.launch

class ManagerTeenagerListActivity : AppCompatActivity(){
    private lateinit var viewModel: AppViewModel
    private lateinit var binding: ActivityManagerTeenagerlistBinding

    private lateinit var result: List<Teenager>
    private lateinit var baseUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManagerTeenagerlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar

        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.actionbar_all)

        val actionBarTitle = actionBar?.customView?.findViewById<TextView>(R.id.actionBarAll)
        actionBarTitle?.text = "할당받지않은 청소년목록"

        actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = AppViewModel.getInstance()

        val layoutManager = LinearLayoutManager(this)
        binding.managerTeenagerListRecyclerView.layoutManager = layoutManager
        val adapter = ManagerTeenagerListAdapter(emptyList()) { teenager ->
            viewModel.setTeenagerId(teenager.id)
            onExpertListButtonClicked()
        }
        binding.managerTeenagerListRecyclerView.adapter = adapter

        baseUrl = resources.getString(R.string.api_ip_server)
        mobileToServer()
    }

    private fun onExpertListButtonClicked() {
        val intent = Intent(this, ManagerExpertListActivity::class.java)
        startActivity(intent)
    }

    private fun mobileToServer() {
        lifecycleScope.launch {
            try {
                val response = ManagerTeenagerListApi.retrofitService(baseUrl).sendsMessage()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        // 서버 응답을 확인하는 작업 수행
                        val responseData = responseBody.teenager
                        result = responseData

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
                val intent = Intent(this, AllMyPageActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}