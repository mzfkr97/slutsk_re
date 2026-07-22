package com.romanzhurid.cinema.di

import com.romanzhurid.cinema.ui.CinemaViewModelFactory
import dagger.Component

@Component(dependencies = [CinemaComponentDependencies::class])
interface CinemaComponent {

    fun getCinemaViewModelFactory(): CinemaViewModelFactory

    @Component.Builder
    interface Builder {
        fun cinemaComponentDependencies(dependencies: CinemaComponentDependencies): Builder
        fun build(): CinemaComponent
    }
}
