package com.romanzhurid.home.di

import com.romanzhurid.domain.AppSettings
import com.romanzhurid.common.DispatcherProvider
import com.romanzhurid.common.ExceptionsEmitter
import com.romanzhurid.common.ProgressEmitter
import com.romanzhurid.common.ResourceProvider
import com.romanzhurid.common.progressdelegate.ProgressDelegate

interface HomeComponentDependencies {
    val exceptionsEmitter: ExceptionsEmitter
    val progressEmitter: ProgressEmitter
    val res: ResourceProvider
    val dispatcherProvider: DispatcherProvider
    val progressDelegate: ProgressDelegate
    val appSettings: AppSettings
}
