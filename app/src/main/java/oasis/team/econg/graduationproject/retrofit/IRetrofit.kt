package oasis.team.econg.graduationproject.retrofit

import com.google.gson.JsonElement
import oasis.team.econg.graduationproject.data.LoginDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*
import java.io.File

interface IRetrofit {

    //유저 로그인
    @POST("/api/signin")
    fun signIn(@Body param: LoginDto): Call<JsonElement>

    //회원가입
    @Multipart
    @JvmSuppressWildcards
    @POST("/api/signup")
    fun signUp(@Part("key") key: RequestBody, @Part file: MultipartBody.Part?): Call<JsonElement>

    //내 식물 목록 불러오기
    @GET("/api/plants")
    fun getPlants(@Header("Authorization") auth: String): Call<JsonElement>

    //식물 등록하기
    @Multipart
    @POST("/api/plants")
    fun postPlants(@Header("Authorization") auth: String, @Part("key") key: RequestBody, @Part file: MultipartBody.Part?): Call<JsonElement>

    //다이어리 리스트 가져오기
    @GET("/api/journals/{plantId}")
    fun getJournals(@Header("Authorization") auth: String, @Path("plantId") plantId: Long): Call<JsonElement>

    //다이어리 올리기
    @Multipart
    @POST("/api/journals/{plantId}")
    fun postJournals(@Header("Authorization") auth: String, @Path("plantId") plantId: Long,
    @Part("key") key: RequestBody, @Part file: MultipartBody.Part?): Call<JsonElement>

    //식물샵 가져오기
    @GET("/api/places")
    fun getPlaces(@Header("Authorization") auth: String)
}