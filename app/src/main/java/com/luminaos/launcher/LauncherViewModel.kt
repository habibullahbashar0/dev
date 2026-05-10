package com.luminaos.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LauncherViewModel(private val repository: AppRepository) : ViewModel() {

    private val _allApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val recentApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val searchQuery = MutableStateFlow("")
    val gridColumns = MutableStateFlow(4)

    val filteredApps: StateFlow<List<AppInfo>> = _allApps
        .combine(searchQuery) { apps, query ->
            if (query.isBlank()) {
                apps
            } else {
                apps.filter { it.label.contains(query, ignoreCase = true) }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadApps()
    }

    fun loadApps() {
        viewModelScope.launch {
            _allApps.value = repository.getInstalledApps()
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun addRecentApp(app: AppInfo) {
        val currentRecents = recentApps.value.toMutableList()
        currentRecents.remove(app)
        currentRecents.add(0, app)
        recentApps.value = currentRecents.take(5)
    }
}
