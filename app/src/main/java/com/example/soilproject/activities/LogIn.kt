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
import com.example.soilproject.MainActivity
import com.example.soilproject.R
import com.example.soilproject.api.RetrofitClient
import com.example.soilproject.models.GenericResponse
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Authenticating...")
        progressDialog.setCancelable(false)

        val phoneNumberLoginContainer: TextInputLayout = findViewById(R.id.phoneNumberLoginContainer)
        val passwordLoginContainer: TextInputLayout = findViewById(R.id.passwordLoginContainer)

        val ClickToRegister: TextView = findViewById(R.id.ClickToRegister)
        ClickToRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }

        val button_login: Button = findViewById(R.id.button_login)
        button_login.setOnClickListener {
            val mobile = phoneNumberLoginContainer.editText?.text.toString().trim()
            val password = passwordLoginContainer.editText?.text.toString().trim()

            if (mobile.isEmpty()){
                phoneNumberLoginContainer.error = "Email is required"
                phoneNumberLoginContainer.requestFocus()
                return@setOnClickListener
            }else if (password.isEmpty()){
                passwordLoginContainer.error = "password is required"
                passwordLoginContainer.requestFocus()
                return@setOnClickListener
            }else if(password.length < 6) {
                passwordLoginContainer.error = "Password should be at least 6 characters"
                passwordLoginContainer.requestFocus()
                return@setOnClickListener
            }else{
                if (isConnected()){
                    try {
                        progressDialog.show()
                        RetrofitClient.instance.signIn(mobile,password)
                            .enqueue(object : Callback<GenericResponse> {
                                override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                                    try {
                                        progressDialog.dismiss()
                                        if(!response.body()?.error!!){
                                            progressDialog.dismiss()
                                            Toast.makeText(this@LogIn,response.body()?.message, Toast.LENGTH_LONG ).show()
                                            val intent = Intent(this@LogIn, MainActivity::class.java)
                                            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                            finish()
                                        }
                                    }catch (e:Exception){
                                        progressDialog.dismiss()
                                        Toast.makeText(this@LogIn,"SignIn kk failed, try again later", Toast.LENGTH_LONG ).show()
                                    }
                                }

                                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                                    try {
                                        progressDialog.dismiss()
                                        Toast.makeText(this@LogIn,"SignIn failed, try again later", Toast.LENGTH_LONG ).show()
                                    }catch (e:Exception){
                                        progressDialog.dismiss()
                                        Toast.makeText(this@LogIn,"SignIn failed", Toast.LENGTH_LONG ).show()
                                    }
                                }

                            })
                    }catch (e:Exception){

                    }
                }else{
                    Toast.makeText(this, "Internet connection is required", Toast.LENGTH_LONG).show()
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