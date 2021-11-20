package com.example.eklavyafrontend


import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Register : AppCompatActivity() {

    private lateinit var UserType: Spinner
    private lateinit var firstName: EditText
    private lateinit var lastName:EditText
    private lateinit var email:EditText
    private lateinit var password:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        firstName = findViewById(R.id.etFirstName)
        lastName = findViewById(R.id.etLastName)
        email = findViewById(R.id.etemail)
        password = findViewById(R.id.etpassword)
        UserType = findViewById(R.id.Usertype)
    }

    fun furtherInfo(view: android.view.View) {
        if (UserType.selectedItem.toString() == "Mentor") {
            //Toast.makeText(this, "Mentor Selected", Toast.LENGTH_SHORT).show()
            val MentorIntent = Intent(this, RegisterActivityMentor::class.java)
            MentorIntent.putExtra("firstName","${firstName.text.toString()}")
            MentorIntent.putExtra("lastName","${lastName.text.toString()}")
            MentorIntent.putExtra("email","${email.text.toString()}")
            MentorIntent.putExtra("password","${password.text.toString()}")
            startActivity(MentorIntent)
        } else {
            //Toast.makeText(this, "Mentee Selected", Toast.LENGTH_SHORT).show()
            val MenteeIntent = Intent(this, RegisterActivityMentee::class.java)
            MenteeIntent.putExtra("firstName","${firstName.text.toString()}")
            MenteeIntent.putExtra("lastName","${lastName.text.toString()}")
            MenteeIntent.putExtra("email","${email.text.toString()}")
            MenteeIntent.putExtra("password","${password.text.toString()}")
            startActivity(MenteeIntent)
        }
    }


}