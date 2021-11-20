package com.example.eklavyafrontend.mentee

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eklavyafrontend.Login
import com.example.eklavyafrontend.MainActivity
import com.example.eklavyafrontend.OkHttpRequestAuth
import com.example.eklavyafrontend.R
import com.example.eklavyafrontend.chat.ChatActivity
import com.example.eklavyafrontend.mentor.MyMentees
import com.example.eklavyafrontend.mentee.MyMentorClicked
import com.example.eklavyafrontend.mentee.MyMentorsInfo
import com.example.eklavyafrontend.mentee.MyMentorsRVAdapter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MyMentors : AppCompatActivity(), MyMentorClicked {
    private lateinit var btnSearchMentors: ImageButton
    var token: String? = null
    var userId: String? = null
    private lateinit var mAdapter: MyMentorsRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_mentors)
        btnSearchMentors = findViewById(R.id.SearchMentors)
        val extras = intent.extras
        token = extras?.getString("token")
        userId = extras?.getString("_id")
        btnSearchMentors.setOnClickListener {
            val MyMentees = Intent(this@MyMentors, MenteeMainActivity::class.java)
            MyMentees.putExtra("token", "$token")
            MyMentees.putExtra("userId", "$userId")
            startActivity(MyMentees)
        }

        val recyclerView: RecyclerView = findViewById<RecyclerView>(R.id.myMentorsRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = MyMentorsRVAdapter(this)
        recyclerView.adapter = mAdapter

    }

    private fun fetchData() {
        val url = "https://eklavya1.herokuapp.com/api/myMentors"
        Log.d("fetchDataCalled", "fetchDataCalled")
        var client = OkHttpClient()
        var request = OkHttpRequestAuth(client)
        request.GET(url, token!!, object : Callback {
            override fun onResponse(call: Call?, response: okhttp3.Response) {
                val responseData = response.body()?.string()
                Log.d("responseData", "$responseData")
                runOnUiThread {
                    try {
                        Log.d("In try block", "In try block")
                        //var json = JSONObject(responseData)
                        //Log.d("Successfull_Request_Mentee", "$json")
                        //var mentorsJsonArray = JSONArray(json)
                        var mentorsJsonArray = JSONArray(responseData)
                        val mentorsArray = ArrayList<MyMentorsInfo>()
                        for (i in 0 until mentorsJsonArray.length()) {
                            val jsonObject = mentorsJsonArray.getJSONObject(i)
                            val mentor = MyMentorsInfo(
                                jsonObject.getString("_id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("email"),
                                jsonObject.getString("role"),
                                jsonObject.getString("companyName"),
                                "SDE-1",
                                jsonObject.getString("field")
                            )
                            mentorsArray.add(mentor)
                            Log.d("mentor", "$mentor")
                        }
                        mAdapter.updateMyMentors(mentorsArray)
                        Log.d("mentorsArray", "$mentorsArray")
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

    override fun onViewProfileClicked(item: MyMentorsInfo) {
        //Toast.makeText(applicationContext, "View Profile clicked", Toast.LENGTH_SHORT).show()
        val viewProfileIntent = Intent(this@MyMentors, MentorProfile::class.java)
        viewProfileIntent.putExtra("token", "$token")
        viewProfileIntent.putExtra("_id", "$userId")
        viewProfileIntent.putExtra("name", "${item.name}")
        viewProfileIntent.putExtra("role", "${item.role}")
        viewProfileIntent.putExtra("designation", "${item.designation}")
        viewProfileIntent.putExtra("company", "${item.companyName}")
        viewProfileIntent.putExtra("email", "${item.email}")
        viewProfileIntent.putExtra("field", "${item.field}")
        startActivity(viewProfileIntent)
    }

    override fun onChatClicked(item: MyMentorsInfo) {
        //Toast.makeText(applicationContext, "Chat item clicked", Toast.LENGTH_SHORT).show()
        val chatIntent = Intent(this@MyMentors, ChatActivity::class.java)
        chatIntent.putExtra("token", "$token")
        chatIntent.putExtra("_id", "$userId")
        chatIntent.putExtra("to", "${item.userId}")
        startActivity(chatIntent)
    }

    fun logoutFunc(view: android.view.View) {

        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Are you sure you want to log out?")
        alertDialog.setPositiveButton("YES"){ _, _ ->
            val sharedPref = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putString("email_key", null)
            editor.putString("password_key", null)
            editor.putString("token_key", null)
            editor.putString("user_id", null)
            editor.putString("user_role", null)
            editor.apply()

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
        alertDialog.setNegativeButton("NO"){_, _ ->
            Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show()
        }

        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }
}