package com.example.eklavyafrontend.mentee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RecoverySystem
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eklavyafrontend.OkHttpRequest
import com.example.eklavyafrontend.OkHttpRequestAuth
import com.example.eklavyafrontend.R
import com.example.eklavyafrontend.mentee.MyMentorsRVAdapter
import com.example.test_eklavya.mentee.MenteeMainAdapter
import com.example.test_eklavya.mentee.MentorClicked
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MenteeMainActivity : AppCompatActivity(), MentorClicked {

    var token:String?=null
    var userId:String?=null
    private lateinit var mAdapter: MenteeMainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentee_main)
        val extras = intent.extras
        token = extras?.getString("token")
        userId = extras?.getString("_id")
        val recyclerView:RecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = MenteeMainAdapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun fetchData() {
        val url = "https://eklavya1.herokuapp.com/api/mentors"
        Log.d("fetchDataCalled","fetchDataCalled")
        var client = OkHttpClient()
        var request = OkHttpRequestAuth(client)
        //Toast.makeText(applicationContext,"tpken:$token",Toast.LENGTH_SHORT).show()
        request.GET(url,token!!,object :Callback{


            override fun onResponse(call: Call?, response:okhttp3.Response) {
                val responseData = response.body()?.string()
                Log.d("responseData","$responseData")
                    runOnUiThread {
                        try {
                            Log.d("In try block","In try block")
                            var json = JSONObject(responseData)
                            Log.d("Successfull_Request_Mentee", "$json")
                            val mentorsJsonArray = json.getJSONArray("mentors")
                            Log.d("postsJSONArray","$mentorsJsonArray")
                            val mentorsArray = ArrayList<MentorsInfo>()
                            for (i in 0 until mentorsJsonArray.length()){
                                val jsonObject = mentorsJsonArray.getJSONObject(i)
                                val mentor = MentorsInfo(
                                    jsonObject.getString("_id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("companyName"),
                                    "SDE-1",
                                    jsonObject.getString("field")
                                )
                                mentorsArray.add(mentor)
                                Log.d("mentor","$mentor")
                            }
                            mAdapter.updateMentors(mentorsArray)
                            Log.d("mentorsArray","$mentorsArray")
                        } catch (e: JSONException) {
                            Log.d("In Catch block","$e")
                            e.printStackTrace()
                        }
                    }
                }
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun onItemClicked(item: MentorsInfo) {
        //Toast.makeText(applicationContext,"Connect button Clicked",Toast.LENGTH_SHORT).show()
        val userId:String = item.userId
        val companyName:String = item.company
        sendconnectionRequest(userId, item.name)
    }

    private fun sendconnectionRequest(userId: String,userName:String) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@MenteeMainActivity)
        alertDialog.setTitle("Are you sure")
        alertDialog.setMessage("Do you wanna connect to $userName?")
        alertDialog.setPositiveButton(
            "yes"
        ) { _, _ ->
            val url = "https://eklavya1.herokuapp.com/api/request/mentor/$userId"
            Log.d("sendConnectionRequest","sendConnectionRequestCalled")
            var client = OkHttpClient()
            var request = OkHttpRequestAuth(client)
            val map: HashMap<String, String> = hashMapOf(
                "userId" to "${userId}"
            )
            //Toast.makeText(applicationContext,"token:$token\n userId:$userId",Toast.LENGTH_SHORT).show()
            request.POST(url,map,token!!,object :Callback{
                override fun onResponse(call: Call?, response:okhttp3.Response) {
                    val responseData = response.body()?.string()
                    Log.d("responseData","${responseData.toString()}")
                    runOnUiThread {
                        try {
                            Toast.makeText(applicationContext,"$responseData",Toast.LENGTH_SHORT).show()
                        } catch (e: JSONException) {
                            Log.d("In Catch block","$e")
                            e.printStackTrace()
                        }
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    TODO("Not yet implemented")
                }

            })
        }
        alertDialog.setNegativeButton(
            "No"
        ) { _, _ ->
            Toast.makeText(applicationContext,"Not Connected",Toast.LENGTH_SHORT).show()
        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }


}