package com.romanzhurid.common.progressdelegate

import com.romanzhurid.brandbook.R
import com.romanzhurid.common.progressdelegate.ProgressDelegate.Companion.PROGRESS_CANCELLATION_MESSAGE
import com.romanzhurid.common.ExceptionsEmitter
import com.romanzhurid.common.ProgressEmitter
import com.romanzhurid.common.ProgressState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

interface ProgressDelegate {
    companion object {
        const val PROGRESS_CANCELLATION_MESSAGE = "ScmProgressDelegate.PROGRESS_CANCELLATION_MESSAGE"
    }
    fun CoroutineScope.showProgress(resId: Int = R.string.common__loading, onCancel: (() -> Unit)? = null)
    fun CoroutineScope.hideProgress()
    val isLoading: Boolean

    fun CoroutineScope.exceptionHandler(afterHideProgress: (() -> Unit)? = null): CoroutineExceptionHandler
}

class ProgressDelegateImpl @Inject constructor(
    private val progressEmitter: ProgressEmitter,
    private val exceptionsEmitter: ExceptionsEmitter,
) : ProgressDelegate {

    private var isProgressShowing = false

    override fun CoroutineScope.showProgress(resId: Int, onCancel: (() -> Unit)?) {
        isProgressShowing = true

        val onCancelXXX = onCancel ?: {
            this@showProgress.coroutineContext.cancelChildren(CancellationException(PROGRESS_CANCELLATION_MESSAGE))
        }

        launch {
            progressEmitter.emit(
                ProgressState.Show(
                    resId = resId,
                    onCancel = onCancelXXX
                )
            )
        }
    }

    override fun CoroutineScope.hideProgress() {
        isProgressShowing = false
        launch {
            progressEmitter.emit(ProgressState.Hide)
        }
    }

    override val isLoading: Boolean
        get() = isProgressShowing

    override fun CoroutineScope.exceptionHandler(afterHideProgress: (() -> Unit)?): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, error ->
            launch {
                hideProgress()
                afterHideProgress?.invoke()
                exceptionsEmitter.emit(error)
            }
        }
    }
}
