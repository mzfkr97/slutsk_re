package com.romanzhurid.navigation.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.scene.Scene
import androidx.navigationevent.NavigationEvent

fun <T : Any> predictiveTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.(@NavigationEvent.SwipeEdge Int) ->
ContentTransform =
    {
        slideInHorizontally(
            initialOffsetX = { -it / 3 }
        ) togetherWith slideOutHorizontally(targetOffsetX = { it })
    }

fun <T : Any> popTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform =
    {
        slideInHorizontally(
            initialOffsetX = { -it / 3 }
        ) + fadeIn() togetherWith
            slideOutHorizontally(
                targetOffsetX = { it }
            ) + fadeOut()
    }

fun <T : Any> transitionSpec(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform =
    {
        slideInHorizontally(
            initialOffsetX = { it }
        ) + fadeIn() togetherWith
            slideOutHorizontally(
                targetOffsetX = { -it / 3 }
            ) + fadeOut()
    }
