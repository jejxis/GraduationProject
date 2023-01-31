package oasis.team.econg.graduationproject.retrofit

import com.google.gson.JsonElement
import oasis.team.econg.graduationproject.data.LoginDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IRetrofit {

    //유저 로그인
    @POST("/api/signin")
    fun signIn(@Body param: LoginDto): Call<JsonElement>
}