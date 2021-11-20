package com.example.eklavyafrontend.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eklavyafrontend.OkHttpRequestAuth
import com.example.eklavyafrontend.R
import com.example.eklavyafrontend.chat.ChatRVAdapter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {
    var token: String? = null
    var userId: String? = null
    var messageRecieveruserId: String? = null

    private lateinit var mAdapter: ChatRVAdapter
    private lateinit var etMessage: EditText
    private lateinit var sendImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val extras = intent.extras
        token = extras?.getString("token")
        userId = extras?.getString("_id")
        messageRecieveruserId = extras?.getString("to")


        etMessage = findViewById(R.id.etInputMessage)
        sendImg = findViewById(R.id.imgSendButton)

        val recyclerView: RecyclerView = findViewById<RecyclerView>(R.id.messageRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = ChatRVAdapter(userId!!)
        recyclerView.adapter = mAdapter

        sendImg.setOnClickListener {
            if (etMessage.text.isNullOrEmpty()) {
                Toast.makeText(applicationContext, "Message Cannot Be empty", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val url = "https://eklavya1.herokuapp.com/api/message/$messageRecieveruserId"
                Log.d("sendMessageRequest", "sendMessageRequestCalled")
                var client = OkHttpClient()
                var request = OkHttpRequestAuth(client)
                val map: HashMap<String, String> = hashMapOf(
                    "message" to "${etMessage.text.toString()}"
                )
                val chat = ChatInfo(
                    "$userId",
                    "$userId",
                    "$messageRecieveruserId",
                    "${etMessage.text.toString()}"
                )
                etMessage.setText("")
                //Toast.makeText(applicationContext,"token:$token\n userId:$userId",Toast.LENGTH_SHORT).show()
                request.POST(url, map, token!!, object : Callback {
                    override fun onResponse(call: Call?, response: okhttp3.Response) {
                        val responseData = response.body()?.string()
                        Log.d("responseData", "${responseData.toString()}")
                        runOnUiThread {
                            try {
                                Toast.makeText(
                                    applicationContext,
                                    "$responseData",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: JSONException) {
                                Log.d("In Catch block", "$e")
                                e.printStackTrace()
                            }
                            //recreate()
                            mAdapter.addChat(chat)

                        }
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.scrollToPosition(mAdapter.itemCount - 1);

    }


    private fun fetchData() {
        //val url = "https://eklavya1.herokuapp.com/api/messages/$userId"
        val url = "https://eklavya1.herokuapp.com/api/messages/$messageRecieveruserId"
        Log.d("fetchDataCalled", "fetchDataCalled")
        var client = OkHttpClient()
        var request = OkHttpRequestAuth(client)
        request.GET(url, token!!, object : Callback {
            override fun onResponse(call: Call?, response: okhttp3.Response) {
                val responseData = response.body()?.string()
                Log.d("responseData", "$responseData")
                runOnUiThread {
                    try {
                        Log.d("ChatActivityTryBlock", "In try block")
                        var chatsJsonArray = JSONArray(responseData)
                        val chatsArray = ArrayList<ChatInfo>()
                        for (i in 0 until chatsJsonArray.length()) {
                            val jsonObject = chatsJsonArray.getJSONObject(i)
                            val chat = ChatInfo(
                                jsonObject.getString("_id"),
                                jsonObject.getString("from"),
                                jsonObject.getString("to"),
                                jsonObject.getString("message")
                            )
                            chatsArray.add(chat)
                            Log.d("chat", "$chat")
                        }
                        mAdapter.updateChat(chatsArray)
                        Log.d("chatsArray", "$chatsArray")
                    } catch (e: JSONException) {
                        Log.d("In Catch block", "$e")
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

        })
    }

    fun goBack(view: android.view.View) {
        finish()
    }
}