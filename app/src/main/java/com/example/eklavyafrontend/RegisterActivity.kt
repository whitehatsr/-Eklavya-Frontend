package com.example.eklavyafrontend

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    lateinit var etFirstName: EditText
    lateinit var etLastName: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var UserType: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etEmail = findViewById(R.id.etemail)
        etPassword = findViewById(R.id.etpassword)
        UserType = findViewById(R.id.Usertype)

    }

    fun isValidEmail(Email: String): Boolean {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"

        if (Email.isEmpty()) {
            return false
        } else return EMAIL_REGEX.toRegex().matches(Email)
    }

    private fun checkInput(): String {
        var Error: String = ""
        if (etFirstName.text.isEmpty()) {
            Error += "Enter your first name\n"
        }
        if (etLastName.text.isEmpty()) {
            Error += "Enter your last name\n"
        }
        if (!isValidEmail(etEmail.text.toString())) {
            Error += "Enter a valid email\n"
        }
        if (etPassword.text.isEmpty()) {
            Error += "Please enter password\n"
            //can do much better password checking in future
            // unmask for password
        }
        return Error

    }

    fun furtherInfo(view: android.view.View) {

        var DisplayError: String = checkInput()
        if (DisplayError.isEmpty()) {
            if (UserType.selectedItem.toString() == "Mentor") {
                Toast.makeText(this, "Mentor Selected", Toast.LENGTH_SHORT).show()
                val MentorIntent = Intent(this, RegisterActivityMentor::class.java)
                startActivity(MentorIntent)
            } else {
                Toast.makeText(this, "Mentee Selected", Toast.LENGTH_SHORT).show()
                val MenteeIntent = Intent(this, RegisterActivityMentee::class.java)
                startActivity(MenteeIntent)
            }
        } else {
            Toast.makeText(this, DisplayError, Toast.LENGTH_SHORT).show()
        }
    }
}