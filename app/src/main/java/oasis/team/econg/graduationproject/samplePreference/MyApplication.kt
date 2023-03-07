package oasis.team.econg.graduationproject.samplePreference

import android.app.Application
class MyApplication: Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
        /*startKoin {
            // 로그를 찍어볼 수 있다.
            // 에러확인 - androidLogger(Level.ERROR)
            androidLogger()
            // Android Content를 넘겨준다.
            androidContext(this@MyApplication)
            // assets/koin.properties 파일에서 프로퍼티를 가져옴
            androidFileProperties()
            //module list
            modules(listOf(repositoryModule, viewModelModule))
        }*/
    }
}