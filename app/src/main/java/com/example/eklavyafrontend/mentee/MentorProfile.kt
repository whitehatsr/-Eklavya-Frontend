package com.example.eklavyafrontend.mentee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.eklavyafrontend.R

class MentorProfile : AppCompatActivity() {
    private lateinit var name: TextView
    private lateinit var email:TextView
    private lateinit var role:TextView
    private lateinit var company:TextView
    private lateinit var designation:TextView
    private lateinit var field:TextView


    var token:String?=null
    var userId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_profile)
        name = findViewById(R.id.tvName)
        email = findViewById(R.id.tvEmail)
        role = findViewById(R.id.tvRole)
        company = findViewById(R.id.tvCompany)
        designation = findViewById(R.id.tvDesignation)
        field = findViewById(R.id.tvField)


        val extras = intent.extras
        token = extras?.getString("token")
        userId = extras?.getString("_id")
        name.text = extras?.getString("name")
        email.text = extras?.getString("email")
        role.text = extras?.getString("role")
        company.text = extras?.getString("company")
        designation.text = extras?.getString("designation")
        field.text = extras?.getString("field")
    }
}