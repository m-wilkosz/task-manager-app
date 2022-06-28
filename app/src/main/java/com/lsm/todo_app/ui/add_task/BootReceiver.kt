package com.lsm.todo_app.ui.add_task

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lsm.todo_app.ui.BaseFragment

class BootReceiver : BroadcastReceiver() {

    val bootCompleted = BaseFragment.SingleLiveEvent<Boolean>()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (intent.action == "android.intent.action.BOOT_COMPLETED")
                bootCompleted.postValue(true)
        }
    }
}