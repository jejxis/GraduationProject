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
    @POST("/~/~")
    fun signIn(@Body param: LoginDto): Call<JsonElement>

    //회원가입
    @Multipart
    @JvmSuppressWildcards
    @POST("/~/~")
    fun signUp(@Part("key") key: RequestBody, @Part file: MultipartBody.Part?): Call<JsonElement>

    //내 식물 목록 불러오기
    @GET("/~/~")
    fun getPlants(@Header("Authorization") auth: String): Call<JsonElement>

    //식물 등록하기
    @Multipart
    @POST("/~/~")
    fun postPlants(@Header("Authorization") auth: String, @Part("key") key: RequestBody, @Part file: MultipartBody.Part?): Call<JsonElement>

    //다이어리 리스트 가져오기
    @GET("/~/~/{plantId}")
    fun getJournals(@Header("Authorization") auth: String, @Path("plantId") plantId: Long): Call<JsonElement>

    //다이어리 올리기
    @Multipart
    @POST("/~/~/{plantId}")
    fun postJournals(@Header("Authorization") auth: String, @Path("plantId") plantId: Long,
    @Part("key") key: RequestBody, @Part file: MultipartBody.Part?): Call<JsonElement>

    //식물샵 가져오기
    @GET("/~/~")
    fun getPlaces(@Header("Authorization") auth: String,
                  @Query("x") x: String, @Query("y") y: String): Call<JsonElement>

    //날씨 가져오기
    @GET("/~/~")
    fun getWeather(@Header("Authorization") auth: String,
                   @Query("x") x: String, @Query("y") y: String): Call<JsonElement>

    //사용자 정보 가져오기
    @GET("/~/~")
    fun getUser(@Header("Authorization") auth: String): Call<JsonElement>

    //사용자 정보(프로필, 닉네임 수정)
    @Multipart
    @JvmSuppressWildcards
    @PATCH("/~/~/~")
    fun updateUserInfo(@Header("Authorization") auth: String, @Part("name") name: RequestBody, @Part("change") change: RequestBody, @Part file: MultipartBody.Part?): Call<JsonElement>

    //비밀번호 변경
    @PATCH("/~/~/~")
    fun changeUserPw(@Header("Authorization") auth: String, @Body param: PwDto): Call<JsonElement>

    //전체 식물도감
    @GET("/~/~")
    fun getGarden(@Header("Authorization") auth: String): Call<JsonElement>

    //식물도감 디테일
    @GET("/~/~/{gardenId}")
    fun getGardenDetail(@Header("Authorization") auth: String, @Path("gardenId") gardenId: Long): Call<JsonElement>

    //식물도감 검색
    @GET("/~/~/~")
    fun searchGarden(@Header("Authorization") auth: String, @Query("keyword") keyword: String): Call<JsonElement>

    //찜한 식물 가져오기
    @GET("/~/~")
    fun getBookmarks(@Header("Authorization") auth: String): Call<JsonElement>

    //식물 찜하기
    @POST("/~/~/{gardenId}")
    fun postBookmarks(@Header("Authorization") auth: String, @Path("gardenId") gardenId: Long): Call<JsonElement>

    //물주기, 분갈이, 영양제 주기 표시
    @POST("/~/~/{plantId}")
    fun postCalendars(@Header("Authorization") auth: String, @Path("plantId") plantId: Long, @Query("type") type: String): Call<JsonElement>

    //물주기, 분갈이, 영양제 준 날들 가져오기
    @GET("/~/~")
    fun getCalendars(@Header("Authorization") auth: String):Call<JsonElement>

    //다이어리 삭제
    @DELETE("/~/~/{journalId}")
    fun deleteJournals(@Header("Authorization") auth: String, @Path("journalId") journalId: Long): Call<JsonElement>

    //사용자가 입력한 특정 식물 정보 받아오기
    @GET("/~/~/{plantId}")
    fun getPlantInfo(@Header("Authorization") auth: String, @Path("plantId") plantId: Long): Call<JsonElement>

    @DELETE("/~/~/{plantId}")
    fun deletePlants(@Header("Authorization") auth: String, @Path("plantId") plantId: Long): Call<JsonElement>

    @PATCH("/~/~/~/{plantId}")
    fun starPlants(@Header("Authorization") auth: String, @Path("plantId") plantId: Long): Call<JsonElement>
}
