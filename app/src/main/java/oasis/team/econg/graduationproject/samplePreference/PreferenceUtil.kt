package oasis.team.econg.graduationproject.samplePreference


import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("token", Context.MODE_PRIVATE)

    private var editor: SharedPreferences.Editor = prefs.edit()

    var token:String? = null
        get() = prefs.getString("token",null)//pref.getString("email", "").toString()
        set(value){
            editor.putString("token","Bearer $value").apply()
            field = "Bearer $value"
        }

    fun logout(){
        editor.clear()
        editor.commit()//edit.apply()
    }
}