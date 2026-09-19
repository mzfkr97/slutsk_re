package com.romanzhurid.re.activity

import com.romanzhurid.common.ExceptionsObserverImpl
import com.romanzhurid.common.ProgressObserverImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainActivityModule = module {
    viewModel<AppActivityViewModel> {
        AppActivityViewModel(
            appSettings = get(),
            progressFlow = get<ProgressObserverImpl>(),
            exceptionsFlow = get<ExceptionsObserverImpl>(),
            exceptionMapper = get(),
            res = get(),
        )
    }
}
