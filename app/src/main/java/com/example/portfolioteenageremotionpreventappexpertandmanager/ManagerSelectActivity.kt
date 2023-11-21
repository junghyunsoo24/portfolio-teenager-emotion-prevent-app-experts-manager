package com.example.portfolioteenageremotionpreventappexpertandmanager

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityManagerSelectBinding

class ManagerSelectActivity : AppCompatActivity() {
    private lateinit var viewModel: AppViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val binding = ActivityManagerSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AppViewModel.getInstance()

        binding.childListBtn.setOnClickListener {
            onTeenagerListButtonClicked()
        }

    }

    private fun onTeenagerListButtonClicked(){
        val intent = Intent(this, ManagerTeenagerListActivity::class.java)
        startActivity(intent)
    }

}