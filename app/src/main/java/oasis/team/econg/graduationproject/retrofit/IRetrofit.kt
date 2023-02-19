package oasis.team.econg.graduationproject.retrofit

import com.google.gson.JsonElement
import oasis.team.econg.graduationproject.data.LoginDto
import oasis.team.econg.graduationproject.data.PwDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

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
    fun getPlaces(@Header("Authorization") auth: String,
                  @Query("x") x: String, @Query("y") y: String): Call<JsonElement>

    //날씨 가져오기
    @GET("/api/home")
    fun getWeather(@Header("Authorization") auth: String,
                   @Query("x") x: String, @Query("y") y: String): Call<JsonElement>

    //사용자 정보 가져오기
    @GET("/api/user")
    fun getUser(@Header("Authorization") auth: String): Call<JsonElement>

    //사용자 정보(프로필, 닉네임 수정)
    @Multipart
    @JvmSuppressWildcards
    @PATCH("/api/user/update")
    fun updateUserInfo(@Header("Authorization") auth: String, @Part("key") key: RequestBody, @Part file: MultipartBody.Part?): Call<JsonElement>

    //비밀번호 변경
    @PATCH("/api/user/changepw")
    fun changeUserPw(@Header("Authorization") auth: String, @Body param: PwDto): Call<JsonElement>

    //전체 식물도감
    @GET("/api/garden")
    fun getGarden(@Header("Authorization") auth: String): Call<JsonElement>

    //식물도감 디테일
    @GET("/api/garden/{gardenId}")
    fun getGardenDetail(@Header("Authorization") auth: String, @Path("gardenId") gardenId: Long): Call<JsonElement>

    //식물도감 검색
    @GET("/api/garden/search")
    fun searchGarden(@Header("Authorization") auth: String, @Query("key") key: String): Call<JsonElement>
}