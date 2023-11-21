package com.example.portfolioteenageremotionpreventappexpertandmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityExpertInfosetlistBinding
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertInfoSet.InfoSetListApi
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertInfoSet.InfoSetListData
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertInfoSet.InfoSetListDataResponse
import kotlinx.coroutines.launch

class ExpertInfoSetListActivity : AppCompatActivity(){
    private lateinit var viewModel: AppViewModel
    private lateinit var binding: ActivityExpertInfosetlistBinding
    private lateinit var result: InfoSetListDataResponse

    private lateinit var id: String
    private lateinit var pw: String
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var institution: String

    private lateinit var baseUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExpertInfosetlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar

        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.actionbar_all)

        val actionBarTitle = actionBar?.customView?.findViewById<TextView>(R.id.actionBarAll)
        actionBarTitle?.text = "개인정보수정"

        actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = AppViewModel.getInstance()


        id = viewModel.getUserId().value.toString()
        binding.idInput.text = id

        binding.infoSetBtn.setOnClickListener {
            pw = binding.firstPwdInput.text.toString()
            if(pw == "") {
                pw = viewModel.getUserPwd().value.toString()
            }
            Log.e("pwd", pw)

            name = binding.nameInput.text.toString()
            if(name == "") {
                name = viewModel.getUserName().value.toString()
            }

            email = binding.emailInput.text.toString()
            if(email == "") {
                email = viewModel.getUserEmail().value.toString()
            }

            institution = binding.institutionInput.text.toString()
            if(institution == "") {
                institution = viewModel.getUserInstitution().value.toString()
            }

            baseUrl = resources.getString(R.string.api_ip_server)
            mobileToServer()
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("개인정보수정 성공")
        builder.setMessage("개인정보수정 완료")
        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
            onInfoListButtonClicked()
        }

        builder.show()
    }

    private fun onInfoListButtonClicked() {
        val intent: Intent = Intent(this, ExpertInfoListActivity::class.java)
        startActivity(intent)
    }

    private fun mobileToServer() {
        lifecycleScope.launch {
            try {

                val message = InfoSetListData(id, pw, name, email, institution)
                val response = InfoSetListApi.retrofitService(baseUrl).sendsMessage(message)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        // 서버 응답을 확인하는 작업 수행
                        result = responseBody
                        viewModel.setUserName(result.name)
                        viewModel.setUserEmail(result.email)
                        viewModel.setUserInstitution(result.institution)

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