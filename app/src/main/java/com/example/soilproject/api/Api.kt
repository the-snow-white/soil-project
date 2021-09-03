package com.example.soilproject.api

import com.example.soilproject.models.GenericResponse
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @FormUrlEncoded
    @POST("signup.php")
    fun signUp(
        @Field("fullName") fullName:String,
        @Field("phoneNumber") phoneNumber:String,
        @Field("password") password:String
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("signin.php")
    fun signIn(
        @Field("phoneNumber") phoneNumber:String,
        @Field("password") password:String
    ): Call<GenericResponse>

}