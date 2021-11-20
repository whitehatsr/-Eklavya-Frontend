package com.example.eklavyafrontend.mentor


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eklavyafrontend.OkHttpRequestAuth
import com.example.eklavyafrontend.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.hashMapOf

class MentorMainActivity : AppCompatActivity(), MenteeClicked {
    var token: String? = null
    var userId: String? = null
    private lateinit var mAdapter: MentorMainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_main)
        val extras = intent.extras
        token = extras?.getString("token")
        userId = extras?.getString("_id")
        val recyclerView: RecyclerView = findViewById<RecyclerView>(R.id.recyclerViewMentee)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = MentorMainAdapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun fetchData() {
        val url = "https://eklavya1.herokuapp.com/api/mentor/requests"
        Log.d("fetchDataCalled", "fetchDataCalled")
        var client = OkHttpClient()
        var request = OkHttpRequestAuth(client)
        //Toast.makeText(applicationContext, "tpken:$token", Toast.LENGTH_SHORT).show()
        request.GET(url, token!!, object : Callback {


            override fun onResponse(call: Call?, response: okhttp3.Response) {
                val responseData = response.body()?.string()
                Log.d("responseData", "$responseData")
                runOnUiThread {
                    try {
                        Log.d("In try block", "In try block")
                        var json = JSONObject(responseData)
                        Log.d("Successfull_Request_Mentee", "$json")
                        val requestsJsonArray = json.getJSONArray("myRequests")
                        Log.d("req", "$requestsJsonArray")
                        val requestsArray = ArrayList<MenteesInfo>()
                        for (i in 0 until requestsJsonArray.length()) {

                            val jsonObjet = requestsJsonArray.getJSONObject(i)
                            val mentee_inf = jsonObjet.getJSONObject("menteeInfo")
                            val mentee = MenteesInfo(
                                mentee_inf.getString("_id"),
                                jsonObjet.getString("requestId"),
                                mentee_inf.getString("name"),
                                mentee_inf.getString("collegeName"),
                                "Computer Engineering",
                                mentee_inf.getString("field"),
                                jsonObjet.getString("status")
                            )
                            requestsArray.add(mentee)
                            Log.d("mentee", "$mentee")
                        }
                        mAdapter.updateMentees(requestsArray)
                        Log.d("mentorsArray", "$requestsArray")
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

    override fun onItemApprove(item: MenteesInfo) {
        //Toast.makeText(applicationContext,"Approved\n Request Id:${item.requestId}",Toast.LENGTH_SHORT).show()
        //request Id , In body: "status" :"approved"
        Log.d("onItemApprove", "Approve function called")
        //setContentView(R.layout.login_activity)
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@MentorMainActivity)
        alertDialog.setTitle("Are you sure?")
        alertDialog.setMessage("Approve ${item.name} as Mentee?")
        alertDialog.setPositiveButton(
            "yes"
        ) { _, _ ->
            //Toast.makeText(this@MentorMainActivity, "Alert dialog closed.", Toast.LENGTH_LONG)
                //.show()
            val url = "https://eklavya1.herokuapp.com/api/request/${item.requestId}/setStatus"
            val map: HashMap<String, String> = hashMapOf(
                "status" to "approved"
            )
            Log.d("MentorMainActivityMap", "$map")
            var client = OkHttpClient()
            var request = OkHttpRequestAuth(client)
            request.POST(url, map, token!!, object : Callback {
                override fun onResponse(call: Call?, response: Response) {
                    val responseData = response.body()?.string()
                    runOnUiThread {
                        try {
                           // Toast.makeText(
                            //    this@MentorMainActivity,
                            //    "${responseData.toString()}",
                            //    Toast.LENGTH_SHORT
                           // ).show()

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    e!!.printStackTrace()
                }
            })
        }
        alertDialog.setNegativeButton(
            "No"
        ) { _, _ ->
            Toast.makeText(applicationContext, "Not approved", Toast.LENGTH_SHORT).show()
        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()


    }

    override fun onItemDecline(item: MenteesInfo) {
        Log.d("onItemDecline", "Decline function called")
        //setContentView(R.layout.login_activity)
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@MentorMainActivity)
        alertDialog.setTitle("Are you sure?")
        alertDialog.setMessage("Disapprove ${item.name} as Mentee?")
        alertDialog.setPositiveButton(
            "yes"
        ) { _, _ ->
            val url = "https://eklavya1.herokuapp.com/api/request/${item.requestId}/setStatus"
            val map: HashMap<String, String> = hashMapOf(
                "status" to "declined"
            )
            Log.d("MentorMainActivityMap", "$map")
            var client = OkHttpClient()
            var request = OkHttpRequestAuth(client)
            request.POST(url, map, token!!, object : Callback {
                override fun onResponse(call: Call?, response: Response) {
                    val responseData = response.body()?.string()
                    runOnUiThread {
                        try {
                            Toast.makeText(
                                this@MentorMainActivity,
                                "${responseData.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    e!!.printStackTrace()
                }
            })
        }
        alertDialog.setNegativeButton(
            "No"
        ) { _, _ ->
            Toast.makeText(applicationContext, "Not declined", Toast.LENGTH_SHORT).show()
        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()


    }


}