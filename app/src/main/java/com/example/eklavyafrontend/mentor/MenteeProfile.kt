package com.example.eklavyafrontend.mentor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.eklavyafrontend.R

class MenteeProfile : AppCompatActivity(){
    private lateinit var name: TextView
    private lateinit var email:TextView
    private lateinit var role:TextView
    private lateinit var college:TextView
    private lateinit var course:TextView
    private lateinit var field:TextView


    var token:String?=null
    var userId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentee_profile)
        name = findViewById(R.id.tvName)
        email = findViewById(R.id.tvEmail)
        role = findViewById(R.id.tvRole)
        college = findViewById(R.id.tvCollege)
        course = findViewById(R.id.tvCourse)
        field = findViewById(R.id.tvField)


        val extras = intent.extras
        token = extras?.getString("token")
        userId = extras?.getString("_id")
        name.text = extras?.getString("name")
        email.text = extras?.getString("email")
        role.text = extras?.getString("role")
        college.text = extras?.getString("college")
        course.text = extras?.getString("course")
        field.text = extras?.getString("field")
    }
}