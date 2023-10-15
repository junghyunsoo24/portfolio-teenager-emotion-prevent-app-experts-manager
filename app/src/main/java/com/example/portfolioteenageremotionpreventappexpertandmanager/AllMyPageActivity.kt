package com.example.portfolioteenageremotionpreventappexpertandmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityAllMypageBinding

class AllMyPageActivity : AppCompatActivity() {
    private lateinit var viewModel: AppViewModel

    private val teenagerKey = "teenager_history"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAllMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar

        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.actionbar_all)

        val actionBarTitle = actionBar?.customView?.findViewById<TextView>(R.id.actionBarAll)
        actionBarTitle?.text = "마이 페이지"

        actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = AppViewModel.getInstance()

        binding.memberId.text = "아이디: " + viewModel.getUserId().value

        binding.logoutBtn.setOnClickListener {
            clearChatHistory()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("로그아웃")
            builder.setMessage("로그아웃 완료")

            builder.setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
                onLogoutButtonClicked()
            }
            builder.show()
        }
    }

    private fun onLogoutButtonClicked(){
        val intent = Intent(this, AllLoginActivity::class.java)
        startActivity(intent)
    }

    private fun clearChatHistory() {
        val teenager = getSharedPreferences(teenagerKey, Context.MODE_PRIVATE)
        val teenagerEditor = teenager.edit()

        teenagerEditor.remove(viewModel.getUserId().value)
        teenagerEditor.apply()
    }
}