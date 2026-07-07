package com.romanzhurid.re.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romanzhurid.common.ExceptionsFlow
import com.romanzhurid.common.ProgressFlow
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.domain.AppSettings
import javax.inject.Inject

class MainActivityViewModelFactory @Inject constructor(
    private val appSettings: AppSettings,
    private val progressFlow: ProgressFlow,
    private val exceptionsFlow: ExceptionsFlow,
    private val res: ResourceProvider,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(
            appSettings = appSettings,
            progressFlow = progressFlow,
            exceptionsFlow = exceptionsFlow,
            res = res,
        ) as T
    }
}
