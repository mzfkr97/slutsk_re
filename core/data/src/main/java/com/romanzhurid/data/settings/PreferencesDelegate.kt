package com.romanzhurid.data.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferencesDelegate<TValue>(
    private val preferences: SharedPreferences,
    private val name: String,
    private val defValue: TValue,
    private val serializer: KSerializer<TValue>? = null,
    private val json: Json = Json.Default
) : ReadWriteProperty<Any?, TValue> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): TValue {
        val s = serializer
        return when {
            serializer != null -> {
                val raw = preferences.getString(name, null)
                raw?.let { value ->
                    runCatching {
                        json.decodeFromString(s, value)
                    }.getOrElse { defValue }
                } ?: defValue
            }

            defValue is Boolean -> preferences.getBoolean(name, defValue) as TValue
            defValue is Int -> preferences.getInt(name, defValue) as TValue
            defValue is Float -> preferences.getFloat(name, defValue) as TValue
            defValue is Long -> preferences.getLong(name, defValue) as TValue
            defValue is String? -> preferences.getString(name, defValue) as TValue
            defValue is Set<*> -> preferences.getStringSet(name, defValue as? Set<String>) as TValue

            else -> defValue
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: TValue) {
        preferences.edit {
            if (serializer != null) {
                if (value == null) {
                    remove(name)
                } else {
                    putString(name, json.encodeToString(serializer, value))
                }
                return@edit
            }

            when (value) {
                is Boolean -> putBoolean(name, value)
                is Int -> putInt(name, value)
                is Float -> putFloat(name, value)
                is Long -> putLong(name, value)
                is String? -> {
                    if (value == null) {
                        remove(name)
                    } else {
                        putString(name, value)
                    }
                }
                is Set<*> -> putStringSet(name, value as? Set<String>)
            }
        }
    }
}
