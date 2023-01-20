package com.enalymounioz.howtoretrofit.model

import com.google.android.gms.common.api.Api
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface ApiCall {
    @GET("apiCall")
    fun callGet(): Call<ApiCallResponse>

    @POST("apiCall")
    fun callPost(): Call<ApiCallResponse>

    //Static
    @GET("apiCall?name=Alex&age=24")
    fun callQueryStatic(): Call<ApiCallResponse>

    //Dynamic
    @GET("apiCall")
    fun callQueryDynamic(@Query("name") n: String?, @Query("age") a:Int?): Call<ApiCallResponse>


    //Multiple Parameters
    @GET("apiCall")
    fun callQueryMultiple(@QueryMap params:Map<String, String>): Call<ApiCallResponse>

    @GET("https://example.com/user/info")
    fun callUrlBypass(): Call<ApiCallResponse>

    @GET
    fun callUrlDynamic(@Url url:String): Call<ApiCallResponse>

   // @GET("user/{info}")
   @GET("{info}")
    fun callUrlPath(@Path ("info")info:String): Call<ApiCallResponse>

    @POST("healthmonitor/users/login")
    fun callPost(@Body user: User): Call<ApiCallResponse>

    @FormUrlEncoded
    @POST("healthmonitor/users/login")
    fun callFormData(@Field("first_name") firstName:String,
                     @Field("last_name") lastName: String): Call<ApiCallResponse>

    @FormUrlEncoded
    @POST("healthmonitor/users/login")
    fun callFormData(@FieldMap fields: Map<String, String>): Call<ApiCallResponse>

    @Headers("Cache-Control: max-age=3600", "user-agent:abc")
    @GET("healthmonitor/patient")
    fun callHeadersStatic(): Call<ApiCallResponse>

    @GET("healthmonitor/patient")
    fun callHeadersDynamic(@Header("user-name") userName:String): Call<ApiCallResponse>

    @GET("healthmonitor/patient")
    fun callHeaderMultiple(@HeaderMap headers: Map<String, String>): Call<ApiCallResponse>

    @GET("apiCall")
    fun callGetRx(): Single<ApiCallResponse>

}