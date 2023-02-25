package oasis.team.econg.graduationproject.samplePreference


import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    private var editor: SharedPreferences.Editor = prefs.edit()

    var token:String?
        get() = prefs.getString("token",null)
        set(value){
            editor.putString("token",value).apply()
        }

    fun logout(){
        editor.clear()
        editor.commit()
    }
}