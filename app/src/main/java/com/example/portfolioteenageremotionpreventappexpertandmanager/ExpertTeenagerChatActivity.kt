package com.example.portfolioteenageremotionpreventappexpertandmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portfolioteenageremotionpreventappexpertandmanager.adapter.ExpertTeenagerChatAdapter
import com.example.portfolioteenageremotionpreventappexpertandmanager.appViewModel.AppViewModel
import com.example.portfolioteenageremotionpreventappexpertandmanager.databinding.ActivityExpertTeenagerchatBinding
import com.example.portfolioteenageremotionpreventappexpertandmanager.expertTeenagerChat.ExpertTeenagerChatDataPair
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*

class ExpertTeenagerChatActivity : AppCompatActivity() {
    private lateinit var input: String
    private lateinit var id: String

    private lateinit var adapter: ExpertTeenagerChatAdapter
    private val messages = mutableListOf<ExpertTeenagerChatDataPair>()
    private lateinit var binding: ActivityExpertTeenagerchatBinding

    private lateinit var viewModel: AppViewModel

    private lateinit var expertKey: String

    private lateinit var mSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExpertTeenagerchatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AppViewModel.getInstance()

        id = viewModel.getUserId().value.toString()

        viewModel.setCurrentDate(getCurrentDate())
        binding.teenagerChat.text = viewModel.getCurrentDate().value

        adapter = ExpertTeenagerChatAdapter(messages)
        binding.teenagerChatRecyclerView.adapter = adapter
        binding.teenagerChatRecyclerView.layoutManager = LinearLayoutManager(this)
        expertKey = "expert_history_${viewModel.getTeenagerId().value}"

        try {
            val options = IO.Options()
            options.forceNew = true
            mSocket = IO.socket(resources.getString(R.string.chat_ip_server),options)
            mSocket.connect()

            val roomName = viewModel.getTeenagerId().value

            //(1)입장
            val data = JoinData(id, roomName)
            val jsonObject = JSONObject()
            jsonObject.put("id", data.id)
            jsonObject.put("room", data.room)
            mSocket.emit("join", jsonObject)

            //(3)메시지 수신
            mSocket.on("roomMessage") { args ->
                val roomMessage  = args[0] as String
                val senderID = args[1] as String

                if (senderID != id) {
                    runOnUiThread {
                        val messagePair = ExpertTeenagerChatDataPair("", roomMessage)
                        messages.add(messagePair)

                        adapter.notifyDataSetChanged()
                        saveExpertChatHistory()
                        scrollToBottom()
                    }
                }
            }

            binding.input.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = binding.input.text.toString()
//                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    inputMethodManager.hideSoftInputFromWindow(binding.input.windowToken, 0)
                    if (input.isNotBlank()) {
                        val messagePair = ExpertTeenagerChatDataPair(input, "")
                        messages.add(messagePair)
                        adapter.notifyDataSetChanged()
                        saveExpertChatHistory()
                        scrollToBottom()

                        //(2) 채팅을 서버로부터 전송
                        val message = input
                        val dataToJson2 = roomName?.let{ SocketData(message, it, id) }
                        val jsonObject2 = JSONObject()
                        if (dataToJson2 != null) {
                            jsonObject2.put("message", dataToJson2.message)
                        }
                        if (dataToJson2 != null) {
                            jsonObject2.put("room", dataToJson2.room)
                        }
                        if (dataToJson2 != null) {
                            jsonObject2.put("senderID", dataToJson2.senderID)
                        }
                        mSocket.emit("chatMessage", jsonObject2)

                        binding.input.text = null
                    }
                    true
                } else {
                    false
                }
            }
            binding.chatDeliver.setOnClickListener {
                input = binding.input.text.toString()
                if (input.isNotBlank()) {
                    val messagePair = ExpertTeenagerChatDataPair(input, "")
                    messages.add(messagePair)
                    adapter.notifyDataSetChanged()
                    saveExpertChatHistory()
                    scrollToBottom()
                    //(2)메시지 전달
                    val message = input
                    val dataToJson2 = roomName?.let{ SocketData(message, it, id) }
                    val jsonObject2 = JSONObject()
                    if (dataToJson2 != null)
                        jsonObject2.put("message", dataToJson2.message)
                    if (dataToJson2 != null)
                        jsonObject2.put("room", dataToJson2.room)
                    if (dataToJson2 != null)
                        jsonObject2.put("senderID", dataToJson2.senderID)
                    mSocket.emit("chatMessage", jsonObject2)
                    binding.input.text = null
                }
                true
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        val actionBar: ActionBar? = supportActionBar

        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.actionbar_all)

        val actionBarTitle = actionBar?.customView?.findViewById<TextView>(R.id.actionBarAll)
        actionBarTitle?.text = "청소년 " + viewModel.getTeenagerName().value.toString() + " 상담"

        actionBar?.setDisplayHomeAsUpEnabled(true)

        loadChatHistory()
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

    private fun scrollToBottom() {
        binding.teenagerChatRecyclerView.post {
            binding.teenagerChatRecyclerView.scrollToPosition(messages.size - 1)
        }
    }

    private fun loadChatHistory() {
        val sharedPreferences = getSharedPreferences(expertKey, Context.MODE_PRIVATE)
        val chatHistoryJson = sharedPreferences.getString(id, "")

        if (!chatHistoryJson.isNullOrEmpty()) {
            val chatHistory = Gson().fromJson<List<ExpertTeenagerChatDataPair>>(
                chatHistoryJson,
                object : TypeToken<List<ExpertTeenagerChatDataPair>>() {}.type
            )
            messages.addAll(chatHistory)
            adapter.notifyDataSetChanged()
            scrollToBottom()
        }
    }

    private fun saveExpertChatHistory() {
        val sharedPreferences = getSharedPreferences(expertKey, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val chatHistoryJson = Gson().toJson(messages)
        editor.putString(id, chatHistoryJson)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 EEEE", Locale.getDefault())
        val date = Date(System.currentTimeMillis())
        return dateFormat.format(date)
    }

    data class SocketData(val message: String?, val room: String, val senderID: String)
    data class JoinData(val id: String, val room: String?)
}
