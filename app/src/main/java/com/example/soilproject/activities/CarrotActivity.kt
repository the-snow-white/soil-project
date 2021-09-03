package com.example.soilproject.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.soilproject.R

class CarrotActivity : AppCompatActivity() {
    lateinit var progressDialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrot)

        val HomeArrow: ImageView = findViewById(R.id.HomeArrow)
        HomeArrow.setOnClickListener {
            val intent = Intent(this, PestActivity::class.java)
            startActivity(intent)
            finish()
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val carrotwebview: WebView = findViewById(R.id.carrotwebView)
        // this will enable the javascript settings
        carrotwebview.settings.javaScriptEnabled = true
        carrotwebview.webViewClient = CustomWebViewClient(this, progressDialog)
        carrotwebview.loadUrl("https://plantvillage.psu.edu/topics/carrot/infos")
    }

    class CustomWebViewClient internal constructor(private val activity: Activity, private val progressDialog: ProgressDialog) :
        WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?
        ): Boolean {
            val url: String = request?.url.toString();
            view?.loadUrl(url)
            return true
        }
        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            return true
        }
        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            Toast.makeText(activity, "Error! $error", Toast.LENGTH_SHORT).show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressDialog.dismiss()
        }
    }

}
