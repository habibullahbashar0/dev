package com.luminaos.launcher

interface AppRepository {
    suspend fun getInstalledApps(): List<AppInfo>
}
