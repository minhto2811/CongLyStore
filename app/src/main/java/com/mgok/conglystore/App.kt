package com.mgok.conglystore

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import vn.momo.momo_partner.AppMoMoLib

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT)
    }
}