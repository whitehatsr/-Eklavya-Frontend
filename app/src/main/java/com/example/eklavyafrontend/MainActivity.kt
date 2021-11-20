package com.example.eklavyafrontend

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.eklavyafrontend.mentee.MyMentors
import com.example.eklavyafrontend.mentor.MyMentees

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val TOKEN_KEY = sharedPref.getString("token_key", "Nil")
        val USER_ID = sharedPref.getString("user_id", "Nil")
        val USER_ROLE = sharedPref.getString("user_role", "Nil")

        if (TOKEN_KEY == null) {
            val redirectTo = Intent(applicationContext, Login::class.java)
            startActivity(redirectTo)
        } else {
            if (USER_ROLE == "mentor") {
                val redirectTo = Intent(applicationContext, MyMentees::class.java)
                redirectTo.putExtra("token", TOKEN_KEY)
                redirectTo.putExtra("_id", USER_ID)
                startActivity(redirectTo)
            } else if (USER_ROLE == "mentee") {
                val redirectTo = Intent(applicationContext, MyMentors::class.java)
                redirectTo.putExtra("token", TOKEN_KEY)
                redirectTo.putExtra("_id", USER_ID)
                startActivity(redirectTo)
            } else {
                val redirectTo = Intent(applicationContext, Login::class.java)
                startActivity(redirectTo)
            }
        }

        //Toast.makeText(this, "$TOKEN_KEY    $USER_ROLE", Toast.LENGTH_SHORT).show()

    }
}