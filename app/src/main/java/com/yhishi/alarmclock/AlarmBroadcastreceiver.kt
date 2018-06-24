package com.yhishi.alarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.jetbrains.anko.toast

class AlarmBroadcastreceiver: BroadcastReceiver() {

    // ブロードキャストインテントを受け取った時
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.toast("アラームを受信しました")
    }
}