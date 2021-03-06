package com.example.eklavyafrontend


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.eklavyafrontend.OkHttpRequest
import com.example.eklavyafrontend.mentor.MentorMainActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONException
import java.io.IOException
import java.util.HashMap

class RegisterActivityMentor : AppCompatActivity() {
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String

    var company: EditText?=null
    var designation: EditText?=null
    var experience: EditText?=null
    var spinner: Spinner?=null
    private lateinit var btnSubmit: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_mentor)

        val extras = intent.extras
        firstName = extras!!.getString("firstName").toString()
        lastName = extras!!.getString("lastName").toString()
        email = extras!!.getString("email").toString()
        password = extras!!.getString("password").toString()

        company = findViewById(R.id.etCompany)
        designation = findViewById(R.id.etDesignation)
        experience = findViewById(R.id.etExperience)
        spinner = findViewById(R.id.idSelectSkillSpinner)
        btnSubmit = findViewById(R.id.btnSubmit)


        btnSubmit.setOnClickListener {
            Log.d("call", "Log_In function called")
            //setContentView(R.layout.login_activity)

            val url = "https://eklavya1.herokuapp.com/api/auth/register"
            val map: HashMap<String, String> = hashMapOf(
                "name" to "$firstName $lastName",
                "email" to "$email",
                "password" to "$password",
                "role" to "mentor",
                "company" to "${company?.text.toString()}",
                "designation" to "${designation?.text.toString()}",
                "experience" to "${experience?.text.toString()}",
                "field" to "${spinner?.selectedItem.toString()}"
            )
            Log.d("Mentor Map", "$map")
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
                                /*Toast.makeText(
                                    this@RegisterActivityMentor,
                                    "${responseData.toString()}",
                                    Toast.LENGTH_SHORT
                                ).show() */
                                val intent =
                                    Intent(this@RegisterActivityMentor, Login::class.java)
                                startActivity(intent)

                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        runOnUiThread {
                            /* Toast.makeText(
                                this@RegisterActivityMentor,
                                "${responseData.toString()}",
                                Toast.LENGTH_SHORT
                            ).show() */
                        }

                    }
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    e!!.printStackTrace()
                }
            })
        }
    }
}

