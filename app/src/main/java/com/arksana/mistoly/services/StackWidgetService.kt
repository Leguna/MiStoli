package com.arksana.mistoly.services

import android.content.Intent
import android.widget.RemoteViewsService
import com.arksana.mistoly.ui.widget.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}