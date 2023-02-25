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
    fun updateUserInfo(@Header("Authorization") auth: String, @Part("name") name: RequestBody, @Part("change") change: RequestBody, @Part file: MultipartBody.Part?): Call<JsonElement>

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
    fun searchGarden(@Header("Authorization") auth: String, @Query("keyword") keyword: String): Call<JsonElement>

    //찜한 식물 가져오기
    @GET("/api/bookmarks")
    fun getBookmarks(@Header("Authorization") auth: String): Call<JsonElement>

    //식물 찜하기
    @POST("/api/bookmarks/{gardenId}")
    fun postBookmarks(@Header("Authorization") auth: String, @Path("gardenId") gardenId: Long): Call<JsonElement>

    //물주기, 분갈이, 영양제 주기 표시
    @POST("/api/calendars/{plantId}")
    fun postCalendars(@Header("Authorization") auth: String, @Path("plantId") plantId: Long, @Query("type") type: String): Call<JsonElement>

    //물주기, 분갈이, 영양제 준 날들 가져오기
    @GET("/api/calendars")
    fun getCalendars(@Header("Authorization") auth: String):Call<JsonElement>

    //다이어리 삭제
    @DELETE("/api/journals/{journalId}")
    fun deleteJournals(@Header("Authorization") auth: String, @Path("journalId") journalId: Long): Call<JsonElement>

    //사용자가 입력한 특정 식물 정보 받아오기
    @GET("/api/plants/{plantId}")
    fun getPlantInfo(@Header("Authorization") auth: String, @Path("plantId") plantId: Long): Call<JsonElement>

    @DELETE("/api/plants/{plantId}")
    fun deletePlants(@Header("Authorization") auth: String, @Path("plantId") plantId: Long): Call<JsonElement>

    @PATCH("/api/plants/star/{plantId}")
    fun starPlants(@Header("Authorization") auth: String, @Path("plantId") plantId: Long): Call<JsonElement>
}