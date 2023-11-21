package com.example.portfolioteenageremotionpreventappexpertandmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityExpertInfolistBinding
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertInfoList.InfoListApi
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertInfoList.InfoListData
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertInfoList.InfoListDataResponse
import kotlinx.coroutines.launch

class ExpertInfoListActivity : AppCompatActivity() {
    private lateinit var viewModel: AppViewModel
    private lateinit var binding: ActivityExpertInfolistBinding
    private lateinit var result: InfoListDataResponse
    private lateinit var id: String
    private lateinit var pw: String
    private lateinit var baseUrl: String

    private val expertKey = "expert_history"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExpertInfolistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar

        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.actionbar_all)

        val actionBarTitle = actionBar?.customView?.findViewById<TextView>(R.id.actionBarAll)
        actionBarTitle?.text = "마이페이지"

        actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = AppViewModel.getInstance()

        id = viewModel.getUserId().value.toString()
        pw = viewModel.getUserPwd().value.toString()

        baseUrl = resources.getString(R.string.api_ip_server)
        mobileToServer()

        binding.logoutBtn.setOnClickListener {
            clearChatHistory()
//            mobileToServers()
        }
    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        if (message == "1") {
            builder.setTitle("회원탈퇴 성공")
            builder.setMessage("회원탈퇴 완료")
            builder.setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
                onLogoutButtonClicked()
            }
        } else {
            builder.setTitle("회원탈퇴 실패")
            builder.setMessage("다시 시도하세요.")
            builder.setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
        }

        builder.show()
    }

    private fun onLogoutButtonClicked() {
        val intent = Intent(this, AllLoginActivity::class.java)
        startActivity(intent)
    }

    private fun mobileToServer() {
        lifecycleScope.launch {
            try {
                val message = InfoListData(id)
                val response = InfoListApi.retrofitService(baseUrl).sendsMessage(message)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        // 서버 응답을 확인하는 작업 수행
                        result = responseBody
                        viewModel.setUserName(result.name)
                        viewModel.setUserEmail(result.email)
                        viewModel.setUserInstitution(result.institution)

                        binding.idInput.text = viewModel.getUserId().value.toString()
                        binding.pwdInput.text = viewModel.getUserPwd().value.toString()
                        binding.nameInput.text = viewModel.getUserName().value.toString()
                        binding.emailInput.text = viewModel.getUserEmail().value.toString()
                        binding.institutionInput.text = viewModel.getUserInstitution().value.toString()

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

//    private fun mobileToServers() {
//        lifecycleScope.launch {
//            try {
//                val message = LogoutData(id, pw, "teenager")
//                val response = LogoutApi.retrofitService(baseUrl).sendsMessage(message)
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        showAlertDialog(responseBody.affected)
//
//                    } else {
//                        Log.e("@@@@Error3", "Response body is null")
//                    }
//                } else {
//                    Log.e("@@@@Error2", "Response not successful: ${response.code()}")
//                }
//            } catch (Ex: Exception) {
//                Log.e("@@@@Error1", Ex.stackTraceToString())
//            }
//        }
//    }

    override fun onCreateOptionsMenu(menuset: Menu): Boolean {
        menuInflater.inflate(R.menu.menuset, menuset)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.myPage_set_btn -> {
                val intent = Intent(this, ExpertInfoSetListActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun clearChatHistory() {
        val expert = getSharedPreferences(expertKey, Context.MODE_PRIVATE)
        val expertEditor = expert.edit()
        expertEditor.remove(viewModel.getUserId().value)
        expertEditor.apply()
    }
}