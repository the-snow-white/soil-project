package com.example.soilproject.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.soilproject.R
import com.example.soilproject.api.RetrofitClient
import com.example.soilproject.models.GenericResponse
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val firstNameRegisterContainer: TextInputLayout = findViewById(R.id.firstNameRegisterContainer)
        val phoneRegisterContainer:TextInputLayout = findViewById(R.id.phoneRegisterContainer)
        val passwordRegisterContainer:TextInputLayout = findViewById(R.id.passwordRegisterContainer)


        val ClickToLogin: TextView = findViewById(R.id.ClickToLogin)
        ClickToLogin.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }

        val button_register: Button = findViewById(R.id.button_register)
        button_register.setOnClickListener {
            val firstName = firstNameRegisterContainer.editText?.text.toString().trim()
            val mobile = phoneRegisterContainer.editText?.text.toString().trim()
            val password = passwordRegisterContainer.editText?.text.toString().trim()

            when {
                firstName.isEmpty() -> {
                    firstNameRegisterContainer.error = "Full name is required"
                    firstNameRegisterContainer.requestFocus()
                    return@setOnClickListener
                }
                mobile.isEmpty() -> {
                    phoneRegisterContainer.error = "Phone number is required"
                    phoneRegisterContainer.requestFocus()
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    passwordRegisterContainer.error = "Password is required"
                    passwordRegisterContainer.requestFocus()
                    return@setOnClickListener
                }
                password.length < 8 -> {
                    passwordRegisterContainer.error = "Password should be more than 8 characters"
                    passwordRegisterContainer.requestFocus()
                    return@setOnClickListener
                }
                else -> {
                    if (isConnected()){
                        val progressDialog = ProgressDialog(this)
                        progressDialog.setTitle("Please wait");
                        progressDialog.setMessage("Creating account...");
                        progressDialog.show();
                        progressDialog.setCancelable(false)

                        RetrofitClient.instance.signUp(firstName,mobile,password)
                            .enqueue(object : Callback<GenericResponse> {
                                override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                                    progressDialog.dismiss()
                                    try {
                                        Toast.makeText(this@Register, response.body()?.message, Toast.LENGTH_LONG).show()
                                        val intent = Intent(this@Register, LogIn::class.java)
                                        startActivity(intent)
                                        finish()
                                    }catch (e:Exception){
                                        Toast.makeText(this@Register, "Failed", Toast.LENGTH_LONG).show()
                                    }
                                }

                                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                                    progressDialog.dismiss()
                                    Toast.makeText(this@Register, t.message, Toast.LENGTH_LONG).show()
                                }

                            })
                    }
                    else
                    {
                        Toast.makeText(this@Register, "Internet connection is required", Toast.LENGTH_LONG).show()
                    }

                }
            }

        }
    }

    private fun isConnected(): Boolean {
        var connected = false
        try {
            val cm =
                this?.applicationContext
                    ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected
        } catch (e: java.lang.Exception) {
            /* Log.e("Connectivity Exception", e.message) */
        }
        return connected
    }

}