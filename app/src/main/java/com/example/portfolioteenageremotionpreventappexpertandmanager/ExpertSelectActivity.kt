package com.example.portfolioteenageremotionpreventappexpertandmanager

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityExpertSelectBinding

class ExpertSelectActivity : AppCompatActivity() {
    private lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val binding = ActivityExpertSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AppViewModel.getInstance()

        binding.childListBtn.setOnClickListener {
            onChildListButtonClicked()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.myPage_btn -> {
                val intent = Intent(this, ExpertInfoListActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onChildListButtonClicked(){
        val intent = Intent(this, ExpertTeenagerListActivity::class.java)
        startActivity(intent)
    }

}