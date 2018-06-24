package com.yhishi.alarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmBroadcastReceiver: BroadcastReceiver() {

    // ブロードキャストインテントを受け取った時
    override fun onReceive(context: Context?, intent: Intent?) {
        val intent = Intent(context, MainActivity::class.java)
                .putExtra("onReceive", true)  // BroadcastReceiverからの起動確認用の情報
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)  // BroadcastReceiverからアクティビティを呼び出すためのフラグ
        context?.startActivity(intent)
    }
}