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
        val usernameInput = findViewById<EditText>(R.id.usernameText)
        val passwordInput = findViewById<EditText>(R.id.passwordText)

        loginBtn.setOnClickListener { view ->
            val email = emailInput.text.toString()
            val pass = passwordInput.text.toString()
            val username = usernameInput.text.toString()
            UserLogin(email, username, pass)
            //commented out currently to stop from errors and sending data
            //to a random file that isnt created
        }

        registerBtn.setOnClickListener ( View.OnClickListener { view ->
            val email = emailInput.text.toString()
            val pass = passwordInput.text.toString()
            val username = usernameInput.text.toString()
            UserRegister(username, email, pass)

            //commented out currently to stop from errors and sending data
            //to a random file that isnt created
        } )
    }

    fun UserLogin(email: String, username: String, password: String){
        val queue = Volley.newRequestQueue(this)

        val clicked =
            JsonObjectRequest(
                Request.Method.GET,
                "http://10.0.2.2:8888/RootedGardening/login.php?email=$email&username=$username&password=$password&buttonPressed=login",
                null,
                { data ->
                    val successValue = data.get("success")
                    Log.e("myapp", "Success value: $successValue")
                    //do if statement where if it succeeded to send it to the new page with intent
                    if (successValue == 1) {
                        Log.e("MyApp", "Login Successful");
                        val tokenID = data.getString("token")
                        val token = tokenID.toString()
                        Log.e("myapp","$token")
                        val intent = Intent(this, MenuActivity::class.java)
                        intent.putExtra("token", token)
                        startActivity(intent)
                    } else {
                        val errorMessage = data.getString("errormessage")
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Log.e("MyApp", "Did not receive network data");
                }
            )

        clicked.setShouldCache(false)
        queue.add(clicked);
    }

    fun UserRegister(username: String, email: String, password: String){
        val queue = Volley.newRequestQueue(this)

        val clicked =
            JsonObjectRequest(
                Request.Method.GET,
                "http://10.0.2.2:8888/RootedGardening/login.php?email=$email&username=$username&password=$password&buttonPressed=register",
                null,
                { data ->
                    val errorMessage = data.get("errormessage")
                    Log.e("myapp", "$errorMessage")
                    val message = errorMessage.toString()
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                },
                { Log.e("MyApp", "Did not receive network data"); })

        clicked.setShouldCache(false)
        queue.add(clicked);
    }
}