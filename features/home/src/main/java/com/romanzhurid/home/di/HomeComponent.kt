package com.romanzhurid.home.di

import dagger.Component

@Component(dependencies = [HomeComponentDependencies::class])
interface HomeComponent {

    @Component.Builder
    interface Builder {
        fun homeComponentDependencies(dependencies: HomeComponentDependencies): Builder
        fun build(): HomeComponent
    }
}
