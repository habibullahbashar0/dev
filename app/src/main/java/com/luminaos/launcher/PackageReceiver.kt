package com.luminaos.launcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PackageReceiver : BroadcastReceiver() {

    var onPackageChanged: (() -> Unit)? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (action == Intent.ACTION_PACKAGE_ADDED ||
            action == Intent.ACTION_PACKAGE_REMOVED ||
            action == Intent.ACTION_PACKAGE_REPLACED
        ) {
            onPackageChanged?.invoke()
        }
    }
}
