package com.myappkunjungan.data.retrofit

import com.myappkunjungan.data.response.DefaultResponse
import com.myappkunjungan.data.response.VisitorResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("date_visited") dateVisited: String
    ): Call<DefaultResponse>

    @GET("visitors")
    fun getVisitors(
    ): Call<VisitorResponse>

    @FormUrlEncoded
    @POST("visitor-store")
    fun addVisitor(
        @Field("nik") nik: String,
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("date_visited") dateVisited: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("visitor-update/{id}")
    fun updateVisitor(
        @Path("id") id: Int,
        @Field("nik") nik: String,
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("date_visited") dateVisited: String
    ): Call<DefaultResponse>

    @POST("visitor-delete/{id}")
    fun deleteVisitor(
        @Path("id") id: Int
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("suggestion-store")
    fun addSuggestion(
        @Field("name") suggestion: String
    ): Call<DefaultResponse>
}