package com.example.portfolioteenageremotionpreventappexpertandmanager

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityManagerSelectBinding

class ManagerSelectActivity : AppCompatActivity() {
    private lateinit var viewModel: AppViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = AppViewModel.getInstance()

        val actionBar: ActionBar? = supportActionBar
        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.actionbar_all)

        val actionBarTitle = actionBar?.customView?.findViewById<TextView>(R.id.actionBarAll)
        actionBarTitle?.text = "하루친구(관리자)"

        actionBar?.setDisplayHomeAsUpEnabled(true)

        val binding = ActivityManagerSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.teenagerListBtn.setOnClickListener {
            onTeenagerListButtonClicked()
        }

        binding.expertListBtn.setOnClickListener {
            onExpertListButtonClicked()
        }

    }

    private fun onTeenagerListButtonClicked(){
        val intent = Intent(this, ManagerTeenagerListActivity::class.java)
        startActivity(intent)
    }

    private fun onExpertListButtonClicked(){
        val intent = Intent(this, ManagerExpertApproveActivity::class.java)
        startActivity(intent)
    }
}