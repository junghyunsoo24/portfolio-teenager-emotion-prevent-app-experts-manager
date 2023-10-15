package com.example.portfolioteenageremotionpreventappexpertandmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityAllLoginBinding
import com.example.portfolioteenageremotionpreventappexpertandmanager.login.ExpertLoginApi
import com.example.portfolioteenageremotionpreventappexpertandmanager.login.LoginData
import com.example.portfolioteenageremotionpreventappexpertandmanager.login.ManagerLoginApi
import kotlinx.coroutines.launch

class AllLoginActivity : AppCompatActivity() {
    private lateinit var id: String
    private lateinit var pw: String
    private lateinit var baseUrl: String
    private lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = AppViewModel.getInstance()

        val binding = ActivityAllLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar

        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.actionbar_all)

        val actionBarTitle = actionBar?.customView?.findViewById<TextView>(R.id.actionBarAll)
        actionBarTitle?.text = "로그인"

        actionBar?.setDisplayHomeAsUpEnabled(true)

        val login: Button = findViewById(R.id.login_btn)
        val join: Button = findViewById(R.id.join_btn)

        login.setOnClickListener {
            id = binding.idInput.text.toString()
            pw = binding.pwdInput.text.toString()

            baseUrl = resources.getString(R.string.api_ip_server)

            if(viewModel.getUser().value == "1"){
                expertMobileToServer()
            }
            else if(viewModel.getUser().value == "2"){
                managerMobileToServer()
            }
        }

        join.setOnClickListener {
            onJoinButtonClicked()
        }

    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        if (message == "0") {
            builder.setTitle("로그인 성공")
            builder.setMessage("다음 화면으로 이동합니다")
            builder.setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
                onLoginButtonClicked()
            }
        } else {
            builder.setTitle("로그인 실패")
            builder.setMessage("다시 입력하세요")
            builder.setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
        }

        builder.show()
    }

    private fun onLoginButtonClicked() {
        val intent: Intent = if(viewModel.getUser().value == "1"){
            Intent(this, ExpertSelectActivity::class.java)
        } else{
            Intent(this, ManagerSelectActivity::class.java)
        }
        startActivity(intent)
    }

    private fun onJoinButtonClicked() {
        val intent: Intent = if(viewModel.getUser().value == "1"){
            Intent(this, ExpertRegisterActivity::class.java)
        } else{
            Intent(this, ManagerRegisterActivity::class.java)
        }
        startActivity(intent)
    }

    private fun expertMobileToServer() {
        lifecycleScope.launch {
            try {
                val message = LoginData(id, pw)
                val response = ExpertLoginApi.retrofitService(baseUrl).sendsMessage(message)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val responseData = responseBody.result
                        showAlertDialog(responseData)

                        viewModel.setUserId(id)
                        viewModel.setUserPwd(pw)
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

    private fun managerMobileToServer() {
        lifecycleScope.launch {
            try {
                val message = LoginData(id, pw)
                val response = ManagerLoginApi.retrofitService(baseUrl).sendsMessage(message)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val responseData = responseBody.result
                        showAlertDialog(responseData)

                        viewModel.setUserId(id)
                        viewModel.setUserPwd(pw)
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
}