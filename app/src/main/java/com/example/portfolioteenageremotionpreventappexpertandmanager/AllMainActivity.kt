package com.example.portfolioteenageremotionpreventappexpertandmanager

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityAllMainBinding

class AllMainActivity : AppCompatActivity() {
    private lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = AppViewModel.getInstance()

        val actionBar: ActionBar? = supportActionBar
        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.actionbar_all)

        val actionBarTitle = actionBar?.customView?.findViewById<TextView>(R.id.actionBarAll)
        actionBarTitle?.text = "하루친구(전문가,관리자)"

        val binding = ActivityAllMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val linkTextView: TextView = findViewById(R.id.icon_url)
        linkTextView.setOnClickListener {
            openLink()
        }

        binding.loginBtn.setOnClickListener {
            onStartButtonClicked()
        }
    }

    private fun onStartButtonClicked() {
        val intent = Intent(this, AllLoginActivity::class.java)
        startActivity(intent)
    }

    private fun openLink() {
        val url = "https://Icons8.com"

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

}