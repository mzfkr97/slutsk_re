package com.romanzhurid.common.di

import com.romanzhurid.common.permisison.PermissionHelper
import com.romanzhurid.common.permisison.PermissionHelperImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val permissionModule = module {
    single<PermissionHelper> {
        PermissionHelperImpl(
            context = androidContext()
        )
    }
}