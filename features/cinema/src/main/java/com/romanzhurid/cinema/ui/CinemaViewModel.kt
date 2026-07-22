package com.romanzhurid.cinema.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanzhurid.brandbook.ext.EMPTY_STRING
import com.romanzhurid.brandbook.ext.getDates
import com.romanzhurid.brandbook.ext.getDayNumber
import com.romanzhurid.cinema.mapper.CalendarToUiMapper
import com.romanzhurid.cinema.mapper.CinemaToUiMapper
import com.romanzhurid.cinema.model.CalendarUi
import com.romanzhurid.cinema.model.CinemaUiItem
import com.romanzhurid.cinema.ui.CinemaViewModel.Event
import com.romanzhurid.cinema.ui.CinemaViewModel.ViewState
import com.romanzhurid.common.DispatcherProvider
import com.romanzhurid.common.progressdelegate.ProgressDelegate
import com.romanzhurid.common.uistate.UiStateDelegate
import com.romanzhurid.common.uistate.UiStateDelegateImpl
import com.romanzhurid.domain.cinema.Calendar
import com.romanzhurid.domain.cinema.repo.CinemaRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class CinemaViewModel(
    private val cinemaRepository: CinemaRepository,
    private val calendarToUiMapper: CalendarToUiMapper,
    private val cinemaMapper: CinemaToUiMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val progressDelegate: ProgressDelegate,
) : ViewModel(),
    UiStateDelegate<ViewState, Event> by UiStateDelegateImpl(ViewState()),
    ProgressDelegate by progressDelegate {

    companion object {
        private const val DAY_START_PERIOD = "yyyy-MM-dd'T'06:00:00zzz"
        private const val DAY_END_PERIOD = "yyyy-MM-dd'T'23:59:00zzz"
    }

    data class ViewState(
        val isInitialLoading: Boolean = false,
        val dayNumber: String = EMPTY_STRING,
        val films: List<CinemaUiItem> = emptyList(),
        val calendarDates: List<CalendarUi> = emptyList(),
        val errorMessage: String = EMPTY_STRING,
    )

    sealed interface Event {
        data class ToGallery(val imageUrl: String) : Event
    }

    private val formatStartPeriod by lazy(LazyThreadSafetyMode.NONE) {
        SimpleDateFormat(DAY_START_PERIOD, Locale.getDefault())
    }

    private val formatEndPeriod by lazy(LazyThreadSafetyMode.NONE) {
        SimpleDateFormat(DAY_END_PERIOD, Locale.getDefault())
    }

    private val exceptionHandler = viewModelScope.exceptionHandler {
        viewModelScope.launch {
            hideProgress()
        }
    }

    init {
        loadCalendar()
    }

    private fun loadCalendar() {
        viewModelScope.launch(exceptionHandler) {
            showProgress()
            val calendarUiList = getDate().map(calendarToUiMapper::map)

            val dayNumber = calendarUiList
                .first().date
                .getDayNumber()

            updateUiState {
                it.copy(
                    calendarDates = calendarUiList,
                    dayNumber = dayNumber,
                )
            }

            loadCinema()
            hideProgress()
        }
    }

    private suspend fun loadCinema() {
        val filmsData = withContext(dispatcherProvider.background()) {
            cinemaRepository.getAllCinema(
                timeStart = formatStartPeriod.format(getDate().first().date.time),
                timeEnd = formatEndPeriod.format(getDate().last().date.time)
            )
        }

        val films = cinemaMapper.map(filmsData)
        updateUiState {
            it.copy(
                films = films,
                isInitialLoading = true
            )
        }
    }

    private fun getDate(): List<Calendar> =
        getDates().mapIndexed { index, model ->
            Calendar(
                id = index,
                date = model,
                isItemSelected = index == 0
            )
        }

    fun onImageClicked(imageUrl: String) {
        viewModelScope.sendEvent(Event.ToGallery(imageUrl))
    }
}
