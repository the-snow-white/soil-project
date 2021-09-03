package com.example.soilproject.activities

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.soilproject.MainActivity
import com.example.soilproject.R
import com.example.soilproject.api.UpdateImagePictureApi
import com.example.soilproject.api.UploadImageResponse
import com.example.soilproject.api.UploadRequestBody
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


@Suppress("DEPRECATION")
class UploadImage : AppCompatActivity() , UploadRequestBody.UploadCallback{
    lateinit var uploadImage:CircleImageView
    private var selectedImageUri: Uri? = null
    lateinit var progressDialog:ProgressDialog
    private var mobile:String = "07657653345"
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Uploading...")
        progressDialog.setCancelable(false)

        val HomeArrow: ImageView = findViewById(R.id.HomeArrow)
        HomeArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }



        val buttonUpload: Button = findViewById(R.id.button_upload)
        uploadImage = findViewById(R.id.upload_image)
        buttonUpload.setOnClickListener {
            uploadSoilImage()
        }
        uploadImage.setOnClickListener {
            //check runtime permission
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    //Permission not yet granted
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //Show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    //Permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //System OS is < Marshmallow
                pickImageFromGallery()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission from popup granted
                    pickImageFromGallery()
                } else {
                    //Permission from popup denied
                    Toast.makeText(this, "Permission to access gallery denied", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            myImage = data?.extras?.get("data").toString()
            selectedImageUri = data?.data
            uploadImage.setImageURI(selectedImageUri)
            //uploadProfile()
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object{
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
        private var myImage="gjh"
    }

    private fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun uploadSoilImage(){
        if (isConnected()) {
            try {
                progressDialog.show()
                val parcelFileDescriptor =
                        contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return
                val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)

                val body = UploadRequestBody(file, "image", this)
                UpdateImagePictureApi().uploadImage(
                        MultipartBody.Part.createFormData("image", file.name, body),
                        RequestBody.create(MediaType.parse("multipart/form-data"), mobile)
                ).enqueue(object : Callback<UploadImageResponse> {
                    override fun onFailure(call: Call<UploadImageResponse>, t: Throwable) {
                        Toast.makeText(this@UploadImage, "Failed,on fail", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<UploadImageResponse>, response: Response<UploadImageResponse>) {
                        try {
                            //val editor = sharedPref.edit()
                            //editor.putString("profile_pic", response.body()?.image.toString())
                            //editor.apply()
                            progressDialog.dismiss()
                            Toast.makeText(this@UploadImage, response.body()?.message, Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            progressDialog.dismiss()
                            Toast.makeText(this@UploadImage, "Failed", Toast.LENGTH_LONG).show()
                        }
                    }

                })
            }catch (e: Exception){
                progressDialog.dismiss()
                Toast.makeText(this@UploadImage, "Failed, try again later", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this@UploadImage, "No internet access", Toast.LENGTH_LONG).show()
        }
    }

    override fun onProgressUpdate(percentage: Int) {
        //return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}