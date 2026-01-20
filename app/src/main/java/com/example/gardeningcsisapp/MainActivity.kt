package com.example.gardeningcsisapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gardeningcsisapp.ui.MenuActivity

class MainActivity : AppCompatActivity() {

    private lateinit var loginBtn: Button
    private lateinit var registerBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loginBtn = findViewById<Button>(R.id.loginBtn)
        registerBtn = findViewById<Button>(R.id.registerBtn)

        val emailInput = findViewById<EditText>(R.id.emailText)
        val passwordInput = findViewById<EditText>(R.id.passwordText)

        loginBtn.setOnClickListener ( View.OnClickListener { view ->
            //val email = emailInput.text.toString()
            //val pass = passwordInput.text.toString()
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)

            //UserLogin(email, pass) //commented out currently to stop from errors and sending data
            //to a random file that isnt created
        } )

        registerBtn.setOnClickListener ( View.OnClickListener { view ->
            val email = emailInput.text.toString()
            val pass = passwordInput.text.toString()
            //UserRegister(email, pass)

            //commented out currently to stop from errors and sending data
            //to a random file that isnt created
        } )
    }

    fun UserLogin(email: String, password: String){
        val queue = Volley.newRequestQueue(this)

        val clicked =
            JsonObjectRequest(
                Request.Method.GET,
                "https://jwuclasses.com/ugly/login?email=${email}&password=${password}",
                null,
                { data ->
                    val successValue = data.get("success")
                    //do if statement where if it succeeded to send it to the new page with intent
                    if (successValue == 1) {
                        Log.e("MyApp", "Login Successful");
                        val tokenID = data.get("token")
                        val token = tokenID.toString()
                        //val intent = Intent(this, MenuActivity::class.java)
                        //intent.putExtra("token", token)
                        // startActivity(intent)
                    } else {
                        val errorMessage = data.get("errormessage")
                        val message = errorMessage.toString()
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }

                },
                { Log.e("MyApp", "Did not receive network data"); })

        clicked.setShouldCache(false)
        queue.add(clicked);
    }

    fun UserRegister(email: String, password: String){
        val queue = Volley.newRequestQueue(this)
        var DataSuccess: Int

        val clicked =
            JsonObjectRequest(
                Request.Method.GET,
                "https://jwuclasses.com/ugly/register?email=${email}&password=${password}",
                null,
                { data ->
                    val errorMessage = data.get("errormessage")
                    val message = errorMessage.toString()
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                },
                { Log.e("MyApp", "Did not receive network data"); })

        clicked.setShouldCache(false)
        queue.add(clicked);
    }
}