package oasis.team.econg.graduationproject.retrofit

import android.util.Log
import com.google.gson.JsonElement
import oasis.team.econg.graduationproject.data.Document
import oasis.team.econg.graduationproject.data.JournalsResponseDto
import oasis.team.econg.graduationproject.data.LoginDto
import oasis.team.econg.graduationproject.data.PlantsResponseDto
import oasis.team.econg.graduationproject.utils.*
import oasis.team.econg.graduationproject.utils.Constants.TAG
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class RetrofitManager {
    companion object{
        val instance = RetrofitManager()
    }

    private val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    //유저로그인
    fun signIn(loginDto: LoginDto, completion: (RESPONSE_STATE, String?) -> Unit){
        var dto = loginDto?: LoginDto("", "")
        Log.d(TAG, "Login: RetrofitManager - in API")
        val call = iRetrofit?.signIn(dto).let{
            it
        }?: return

        call.enqueue(object: retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "Login: onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "Login: onResponse() called/ response: $response")
                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            val token = it.asJsonObject.get("token").asString
                            completion(RESPONSE_STATE.OKAY, token)
                            Log.d(TAG, "onResponse: $token")
                        }
                    }
                }
            }
        })
    }

    //회원가입
    fun signUp(key: RequestBody, file: MultipartBody.Part?, completion: (RESPONSE_STATE) -> Unit){
        var key = key
        var file = file

        val call = iRetrofit?.signUp(key, file).let{
            it
        }?: return

        call.enqueue(object: retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "SignUp: onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "SignUp: onResponse() called/ response: $response")
                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            completion(RESPONSE_STATE.OKAY)
                            Log.d(TAG, "onResponse: SUCCESS")
                        }
                    }
                    else -> {
                        response.body()?.let{
                            completion(RESPONSE_STATE.FAIL)
                            Log.d(TAG, "onResponse: FAIL")
                        }
                    }
                }
            }
        })
    }

    //내 식물 목록 불러오기
    fun getPlants(auth: String?, completion:( RESPONSE_STATE, ArrayList<PlantsResponseDto>) -> Unit){
        var au = auth.let{it}?:""
        val call = iRetrofit?.getPlants(auth = au).let{it}?:return
        var parsedDataArray = ArrayList<PlantsResponseDto>()

        call.enqueue(object: retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - getPlants(): onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, parsedDataArray)
            }
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - getPlants(): onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            val body = it.asJsonObject.get("data").asJsonArray

                            Log.d(TAG, "RetrofitManager - getPlants(): onResponse() called")

                            body.forEach { resultItem ->
                                val plantsResponseDto = resultItem.convertToPlantsResponseDto()
                                parsedDataArray.add(plantsResponseDto)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }
        })
    }

    //식물 등록하기
    fun postPlants(auth: String?,key: RequestBody, file: MultipartBody.Part?, completion: (RESPONSE_STATE, String?) -> Unit){
        var au = auth.let{it}?:""
        val call = iRetrofit?.postPlants(auth = au, key = key, file = file).let{it}?:return
        Log.d(TAG, "postPlants: RetrofitManager - In API")

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "postPlants: RetrofitManager - onFailure called/ t:: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "postPlants: RetrofitManager - onResponse called/ response: ${response.raw()}")
                when(response.code()){
                    200 ->{
                        response.body()?.let {
                            val body = it.asJsonObject.get("data").asString
                            completion(RESPONSE_STATE.OKAY, body)
                        }
                    }
                }
            }
        })
    }

    //다이어리 목록 가져오기
    fun getJournals(auth: String?, plantId: Long, completion: (RESPONSE_STATE, ArrayList<JournalsResponseDto>)-> Unit){
        var au = auth.let{it}?:""
        val call = iRetrofit?.getJournals(auth = au, plantId = plantId).let{it}?:return
        var parsedDataArray = ArrayList<JournalsResponseDto>()
        Log.d(TAG, "getJournals: RetrofitManager - In API")

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - getJournals(): onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, parsedDataArray)
            }
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - getJournals(): onResponse() called/ response: ${response.raw()}")
                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            val body = it.asJsonObject.get("data").asJsonArray
                            Log.d(TAG, "RetrofitManager - getJournals(): onResponse() called")

                            body.forEach { resultItem ->
                                val journalsResponseDto = resultItem.convertToJournalsResponseDto()
                                parsedDataArray.add(journalsResponseDto)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }
        })
    }

    //다이어리 올리기
    fun postJournals(auth: String?, plantId: Long, key: RequestBody, file: MultipartBody.Part?, completion: (RESPONSE_STATE, String?) -> Unit){
        var au = auth.let{it}?:""
        val call = iRetrofit?.postJournals(au, plantId, key, file).let{
            it
        }?: return

        call.enqueue(object: retrofit2.Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - postJournals(): onResponse() called/ response: ${response.raw()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            val body = it.asJsonObject.get("data").asString
                            completion(RESPONSE_STATE.OKAY, body)
                            Log.d(TAG, "RetrofitManager - postJournals(): onResponse: SUCCESS")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - postJournals(): onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }
        })
    }

    //식물샵 가져오기
    fun getPlaces(auth: String?, x: String, y: String, completion: (RESPONSE_STATE, ArrayList<Document>) -> Unit){
        var au = auth.let{it}?:""
        val call = iRetrofit?.getPlaces(au, x, y).let{
            it
        }?: return
        var parsedDataArray = ArrayList<Document>()

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - getPlaces(): onResponse() called/ response: ${response.raw()}")
                when(response.code()){
                    200 ->{
                        response.body()?.let{
                            val body = it.asJsonObject.get("documents").asJsonArray
                            Log.d(TAG, "RetrofitManager - getPlaces(): onResponse() called")

                            body.forEach { resultItem ->
                                val document = resultItem.convertToDocument()
                                parsedDataArray.add(document)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedDataArray)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - getPlaces(): onFailure() called/ t: $t")
                completion(RESPONSE_STATE.FAIL, parsedDataArray)
            }
        })
    }
}