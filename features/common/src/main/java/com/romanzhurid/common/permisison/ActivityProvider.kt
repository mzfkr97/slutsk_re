package com.romanzhurid.common.permisison

import android.app.Activity

interface ActivityProvider {
    val currentActivity: Activity?
}