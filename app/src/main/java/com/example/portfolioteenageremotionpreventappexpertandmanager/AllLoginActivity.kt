package com.example.portfolioteenageremotionpreventappexpertandmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityAllLoginBinding
import com.example.portfolioteenageremotionpreventappexpertandmanager.login.LoginApi
import com.example.portfolioteenageremotionpreventappexpertandmanager.login.LoginData
import kotlinx.coroutines.launch

class AllLoginActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var id: String
    private lateinit var pw: String

    private lateinit var role: String

    private lateinit var viewModel: AppViewModel

    private lateinit var register: String
    private lateinit var registerValue: String

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

        val registers = arrayOf("전문가", "관리자")
        val spinner = findViewById<Spinner>(R.id.spinner_gender)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, registers)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        login.setOnClickListener {
            id = binding.idInput.text.toString()
            pw = binding.pwdInput.text.toString()
            viewModel.setUrl(resources.getString(R.string.api_ip_server))
            mobileToServer()
        }

        join.setOnClickListener {
            registerValue?.let { selectedRegister ->
                register = if (selectedRegister == "전문가") "0" else "1"
            }
            onJoinButtonClicked()
        }
    }

    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: android.view.View?,
        position: Int,
        id: Long
    ) {
        registerValue = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        when (message) {
            "expert" -> {
                builder.setTitle(" 전문가 로그인 성공")
                builder.setMessage("다음 화면으로 이동합니다")
                builder.setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                    onLoginButtonClicked()
                }
            }
            "manager" -> {
                builder.setTitle("관리자 로그인 성공")
                builder.setMessage("다음 화면으로 이동합니다")
                builder.setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                    onLoginButtonClicked()
                }
            }
        }

        builder.show()
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("로그인 실패")
        builder.setMessage("다시 입력하세요")
        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()

        }

        builder.show()
    }

    private fun onLoginButtonClicked() {
        val intent: Intent = if(role == "expert"){
            Intent(this, ExpertSelectActivity::class.java)
        } else{
            Intent(this, ManagerSelectActivity::class.java)
        }
        startActivity(intent)
    }

    private fun onJoinButtonClicked() {

        val intent: Intent = if(register == "0"){
            Intent(this, ExpertRegisterActivity::class.java)
        } else{
            Intent(this, ManagerRegisterActivity::class.java)
        }
        startActivity(intent)
    }

    private fun mobileToServer() {
        lifecycleScope.launch {
            try {
                val message = LoginData(id, pw)
                val response = viewModel.getUrl().value?.let {
                    LoginApi.retrofitService(it).sendsMessage(message)
                }
                if (response != null) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            viewModel.setJwtToken(responseBody.access_token)
                            val responseData = responseBody.role

                            role = responseData
                            showAlertDialog(responseData)

                            viewModel.setUserId(id)
                            viewModel.setUserPwd(pw)
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

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)
    }
}