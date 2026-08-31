package com.romanzhurid.cinema.di

import com.romanzhurid.cinema.ui.CinemaViewModelFactory
import org.koin.core.context.GlobalContext

object CinemaComponentHolder {
    fun getViewModelFactory(): CinemaViewModelFactory {
        return GlobalContext.get().get()
    }
}
