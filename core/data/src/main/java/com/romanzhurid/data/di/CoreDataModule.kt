package com.romanzhurid.data.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.romanzhurid.data.BuildConfig
import com.romanzhurid.data.di.module.databaseModule
import com.romanzhurid.data.di.module.repositoryModule
import com.romanzhurid.data.settings.AppSettingsImpl
import com.romanzhurid.domain.AppSettings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreDataModule = module {
    includes(retrofitModule, databaseModule, repositoryModule)

    single<SharedPreferences> {
        val context = androidContext()
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        EncryptedSharedPreferences.create(
            BuildConfig.PREF_PACKAGE_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    single<AppSettings> {
        AppSettingsImpl(get())
    }
}
