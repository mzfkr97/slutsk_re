package com.romanzhurid.navigation.featurehost

import kotlinx.coroutines.channels.Channel
import javax.inject.Inject
import javax.inject.Singleton

interface NavigationChannelProvider {
    val navigationChannel: Channel<NavIntent>
}

@Singleton
class NavigationChannelProviderImpl @Inject constructor() : NavigationChannelProvider {
    override val navigationChannel = Channel<NavIntent>(
        capacity = Channel.BUFFERED
    )
}
