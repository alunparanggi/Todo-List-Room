package com.challenge.refactory

import android.app.Application
import com.challenge.refactory.data.TaskRoomDatabase

class App: Application() {
    val database: TaskRoomDatabase by lazy { TaskRoomDatabase.getDatabase(this) }
}