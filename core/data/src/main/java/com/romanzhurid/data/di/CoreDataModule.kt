package com.romanzhurid.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.romanzhurid.data.BuildConfig
import com.romanzhurid.domain.AppSettings
import com.romanzhurid.data.di.module.CurrencyDataModule
import com.romanzhurid.data.di.module.DatabaseModule
import com.romanzhurid.data.settings.AppSettingsImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RetrofitModule::class, CurrencyDataModule::class, DatabaseModule::class])
class CoreDataModule {

    @Singleton
    @Provides
    fun provideEncryptedSharedPreferences(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            BuildConfig.PREF_PACKAGE_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Singleton
    @Provides
    fun provideAppSettings(impl: AppSettingsImpl): AppSettings = impl
}
