package com.romanzhurid.cinema.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romanzhurid.cinema.mapper.CalendarToUiMapper
import com.romanzhurid.cinema.mapper.CinemaToUiMapper
import com.romanzhurid.common.DispatcherProvider
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.domain.cinema.repo.CinemaRepository
import javax.inject.Inject

class CinemaViewModelFactory @Inject constructor(
    private val cinemaRepository: CinemaRepository,
    private val calendarToUiMapper: CalendarToUiMapper,
    private val cinemaMapper: CinemaToUiMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val progressDelegate: ProgressDelegate,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CinemaViewModel(
            cinemaRepository = cinemaRepository,
            calendarToUiMapper = calendarToUiMapper,
            cinemaMapper = cinemaMapper,
            dispatcherProvider = dispatcherProvider,
            progressDelegate = progressDelegate,
        ) as T
    }
}
