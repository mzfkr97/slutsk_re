package com.romanzhurid.home.di

object HomeComponentHolder {
    private val components = mutableMapOf<String, HomeComponent>()

    fun get(
        instanceId: String,
        dependencies: HomeComponentDependencies
    ): HomeComponent {
        return components.getOrPut(instanceId) {
            DaggerHomeComponent.builder()
                .homeComponentDependencies(dependencies)
                .build()
        }
    }

    fun clear(instanceId: String) {
        components.remove(instanceId)
    }
}
