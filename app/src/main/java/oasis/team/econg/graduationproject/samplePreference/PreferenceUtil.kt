package oasis.team.econg.graduationproject.samplePreference


import android.content.Context
import android.content.SharedPreferences
import oasis.team.econg.graduationproject.data.UserDto

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user", Context.MODE_PRIVATE)

    private var editor: SharedPreferences.Editor = prefs.edit()

    var token:String? = null
        get() = prefs.getString("token",null)//pref.getString("email", "").toString()
        set(value){
            editor.putString("token"," $value").commit()
            field = "Bearer $value"
        }

    var nickname: String? = null
        get() = prefs.getString("nickname", null)
        set(value){
            editor.putString("nickname", value).commit()
            field = value
        }

    var picture: String? = null
        get() = prefs.getString("picture", null)
        set(value){
            editor.putString("picture", value).commit()
            field = value
        }

    fun logout(){
        editor.clear()
        editor.commit()//edit.apply()
    }
}
