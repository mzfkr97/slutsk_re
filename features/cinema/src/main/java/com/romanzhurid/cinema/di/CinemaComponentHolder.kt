package com.romanzhurid.cinema.di

object CinemaComponentHolder {
    private val components = mutableMapOf<String, CinemaComponent>()

    fun get(
        instanceId: String,
        dependencies: CinemaComponentDependencies
    ): CinemaComponent {
        return components.getOrPut(instanceId) {
            DaggerCinemaComponent.builder()
                .cinemaComponentDependencies(dependencies)
                .build()
        }
    }

    fun clear(instanceId: String) {
        components.remove(instanceId)
    }
}
