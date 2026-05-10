package com.luminaos.launcher

import android.content.Context

object ServiceLocator {
    private var repository: AppRepository? = null

    fun provideRepository(context: Context): AppRepository {
        return repository ?: synchronized(this) {
            val instance = AppRepositoryImpl(context.applicationContext)
            repository = instance
            instance
        }
    }
}
