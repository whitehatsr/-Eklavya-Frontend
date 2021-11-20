package com.example.eklavyafrontend

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import android.content.SharedPreferences
import android.view.View
import com.example.eklavyafrontend.mentee.MenteeMainActivity
import com.example.eklavyafrontend.mentee.MyMentors
import com.example.eklavyafrontend.mentor.MentorMainActivity
import com.example.eklavyafrontend.mentor.MyMentees
import com.example.test_eklavya.OkHttpRequest


class Login : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText


    val shared_Prefs = "shared_prefs"

    // key for storing email.
    val EMAIL_KEY = "email_key"

    // key for storing password.
    val PASSWORD_KEY = "password_key"

    //key for storing token
    val TOKEN_KEY = "token_key"

    private lateinit var sharedPrefrences: SharedPreferences
    private lateinit var eml: String
    private lateinit var pwd: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)

        sharedPrefrences = getSharedPreferences(shared_Prefs, MODE_PRIVATE)

        eml = sharedPrefrences.getString(EMAIL_KEY, null).toString()
        pwd = sharedPrefrences.getString(PASSWORD_KEY, null).toString()


    }

    override fun onStart() {
        super.onStart()
        if (eml.isNullOrEmpty() && pwd.isNullOrEmpty()) {
            val i = Intent(this@Login, MainActivity::class.java)
            startActivity(i)
        }
    }

    fun open_registration(view: View) {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }

    fun perform_login(view: View) {
        Log.d("call", "Log_In function called")
        //setContentView(R.layout.login_activity)

        val url = "https://eklavya1.herokuapp.com/api/auth/login"
        val map: HashMap<String, String> = hashMapOf(
            "email" to "${email.text.toString()}",
            "password" to "${password.text.toString()}"
        )
        Log.d("Idol Map", "$map")
        var client = OkHttpClient()
        var request = OkHttpRequest(client)

        request.POST(url, map, object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                val responseData = response.body()?.string()

                //val httpResponse = client.newCall(request).execute()
                //httpResponse.code()
                if (response.isSuccessful) {
                    //status code lies within 200-299
                    runOnUiThread {
                        try {
                            var json = JSONObject(responseData)
                            Log.d("Successfull Request", "$json")
                            var token = json.getString("token")
                            Log.d("token", "$token")
                            var role = json.getString("role")
                            Log.d("role", "$role")
                            var id = json.getString("_id")

                            //save data to local storage
                            var editor: SharedPreferences.Editor = sharedPrefrences.edit()
                            editor.putString(EMAIL_KEY, email.text.toString())
                            editor.putString(PASSWORD_KEY, password.text.toString())
                            editor.putString(TOKEN_KEY, token)
                            editor.putString("user_id", id)
                            editor.putString("user_role", role)

                            editor.apply()
                            if (role.equals("mentor")) {
                                val intent = Intent(this@Login, MyMentees::class.java)
                                intent.putExtra("token", token)
                                intent.putExtra("_id", id)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this@Login, MyMentors::class.java)
                                intent.putExtra("token", token)
                                intent.putExtra("_id", id)
                                startActivity(intent)
                            }


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@Login, "${responseData.toString()}", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                e!!.printStackTrace()
            }
        })


    }

}