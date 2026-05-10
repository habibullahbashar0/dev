package com.luminaos.launcher

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepositoryImpl(private val context: Context) : AppRepository {

    private val packageManager: PackageManager = context.packageManager

    override suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val apps = mutableListOf<AppInfo>()
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        try {
            val resolveInfos = packageManager.queryIntentActivities(intent, 0)
            for (resolveInfo in resolveInfos) {
                val label = resolveInfo.loadLabel(packageManager).toString()
                val packageName = resolveInfo.activityInfo.packageName
                val icon = IconPackManager.getCustomIcon(context, packageName)
                    ?: resolveInfo.loadIcon(packageManager)
                apps.add(AppInfo(label, packageName, icon))
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Error fetching installed apps", e)
        }

        apps.sortBy { it.label.lowercase() }
        apps
    }
}
