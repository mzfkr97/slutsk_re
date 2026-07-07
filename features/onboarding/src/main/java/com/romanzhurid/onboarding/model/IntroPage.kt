package com.romanzhurid.onboarding.model

import com.romanzhurid.brandbook.R

data class IntroPage(
        val title: Int,
        val description: Int,
        // val imageRes: Int // Add if needed
    ){
        companion object {
            fun getPages() = listOf(
                IntroPage(
                    title = R.string.onboarding__welcome_title,
                    description = R.string.onboarding__welcome_description
                ),
                IntroPage(
                        title = R.string.onboarding__bus_schedules_title,
                        description = R.string.onboarding__bus_schedules_description
                    ),
                    IntroPage(
                        title = R.string.onboarding__stay_updated_title,
                        description = R.string.onboarding__stay_updated_description
                    )
                )
        }
    }