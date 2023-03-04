package oasis.team.econg.graduationproject.samplePreference

import android.app.Application
/*import android.content.Context
import oasis.team.econg.graduationproject.di.repositoryModule
import oasis.team.econg.graduationproject.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin*/

class MyApplication: Application() {
    /*init{
        instance = this
    }*/
    companion object {
        lateinit var prefs: PreferenceUtil
        /*lateinit var instance: MyApplication
        fun applicationContext() : Context {
            return instance.applicationContext
        }*/
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