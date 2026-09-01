package com.romanzhurid.home.di

import com.romanzhurid.navigation.host.FeatureScope
import org.koin.dsl.module
import org.koin.core.qualifier.named

object HomeFeatureScope : FeatureScope {
    override val qualifier = named<HomeFeatureScope>()
}

val homeModule = module {
}
