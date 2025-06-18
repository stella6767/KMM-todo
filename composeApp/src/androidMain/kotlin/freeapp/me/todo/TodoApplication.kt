package freeapp.me.todo

import android.app.Application
import freeapp.me.todo.config.di.initKoin
import org.koin.android.ext.koin.androidContext

class TodoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@TodoApplication)
        }
    }
}
