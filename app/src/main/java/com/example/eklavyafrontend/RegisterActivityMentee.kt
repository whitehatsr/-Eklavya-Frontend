package com.example.eklavyafrontend



import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.eklavyafrontend.OkHttpRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONException
import java.io.IOException
import java.util.*


class RegisterActivityMentee : AppCompatActivity() {

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String

    //    private lateinit var college:EditText
//    private lateinit var course:EditText
//    private lateinit var year:EditText
//    private lateinit var spinner: Spinner
    var college: EditText? = null
    var course: EditText? = null
    var year: Spinner? = null
    var phone: EditText? = null
    var spinner: Spinner? = null
    private lateinit var btnSubmit: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_mentee)

        val extras = intent.extras
        firstName = extras!!.getString("firstName").toString()
        lastName = extras!!.getString("lastName").toString()
        email = extras!!.getString("email").toString()
        password = extras!!.getString("password").toString()

        college = findViewById(R.id.etCollege)
        course = findViewById(R.id.etCourse)
        year = findViewById(R.id.etYear)
        phone = findViewById(R.id.etPhone)
        spinner = findViewById(R.id.idSelectInterestSpinner)
        btnSubmit = findViewById(R.id.btnSubmit)


        btnSubmit.setOnClickListener {
            Log.d("call", "Log_In function called")
            //setContentView(R.layout.login_activity)

            val url = "https://eklavya1.herokuapp.com/api/auth/register"
            val map: HashMap<String, String> = hashMapOf(
                "name" to "$firstName $lastName",
                "email" to "$email",
                "password" to "$password",
                "role" to "mentee",
                "college" to "${college?.text.toString()}",
                "course" to "${course?.text.toString()}",
                "year" to "${year?.selectedItem.toString()}",
                "phone" to "${phone?.text.toString()}",
                "field" to "${spinner?.selectedItem.toString()}"
            )
            Log.d("Mentee Map", "$map")
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
                                Toast.makeText(
                                    this@RegisterActivityMentee,
                                    "${responseData.toString()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(this@RegisterActivityMentee, Login::class.java)
                                startActivity(intent)

                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@RegisterActivityMentee,
                                "${responseData.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()
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

