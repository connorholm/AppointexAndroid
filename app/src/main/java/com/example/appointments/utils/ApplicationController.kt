package com.example.appointments.utils

import android.app.Application
import com.example.insuppli.repository.local.DataManager

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationController : Application() {
    //private var viewModelInjector: ViewModelInjector? = null
    //private lateinit var repository: EventRepository


    override fun onCreate() {
        super.onCreate()
        DataManager.init(this)

    }
}