package com.romanzhurid.currencies.di

object CurrencyComponentHolder {
    private val components = mutableMapOf<String, CurrencyComponent>()

    fun get(
        instanceId: String,
        dependencies: CurrencyComponentDependencies
    ): CurrencyComponent {
        return components.getOrPut(instanceId) {
            DaggerCurrencyComponent.builder()
                .currencyComponentDependencies(dependencies)
                .build()
        }
    }

    fun clear(instanceId: String) {
        components.remove(instanceId)
    }
}
