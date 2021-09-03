package com.example.soilproject.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UpdateImagePictureApi {
    @Multipart
    @POST("uploadsoilimage.php?apicall=upload")
    fun uploadImage(
            @Part image: MultipartBody.Part,
            @Part("mobile") mobile: RequestBody
    ): Call<UploadImageResponse>

    companion object {
        operator fun invoke(): UpdateImagePictureApi {
            return Retrofit.Builder()
                    .baseUrl("http://heriplex.com/joy/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(UpdateImagePictureApi::class.java)
        }
    }
}