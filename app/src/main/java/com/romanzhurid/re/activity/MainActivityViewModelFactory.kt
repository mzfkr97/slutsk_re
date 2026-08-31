package com.romanzhurid.re.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romanzhurid.common.ExceptionsFlow
import com.romanzhurid.common.ProgressFlow
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.domain.AppSettings
import org.koin.dsl.module

val mainActivityModule = module {
    factory {
        MainActivityViewModelFactory(
            appSettings = get(),
            progressFlow = get<ProgressFlow>(),
            exceptionsFlow = get<ExceptionsFlow>(),
            res = get(),
        )
    }

    factory {
        MainActivityViewModel(
            appSettings = get(),
            progressFlow = get<ProgressFlow>(),
            exceptionsFlow = get<ExceptionsFlow>(),
            res = get(),
        )
    }
}

class MainActivityViewModelFactory(
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
