package com.yhishi.alarmclock

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // BroadcastReceiverからのインテントかどうか確認
        if(intent?.getBooleanExtra("onReceive", false) == true) {

            // SimapleAlertDialogインスタンスを生成し、ダイアログ表示
            val dialog = SimapleAlertDialog()
            dialog.show(supportFragmentManager, "alert_dialog")
        }

        setContentView(R.layout.activity_main)

        setAlarm.setOnClickListener {
            val calendar = Calendar.getInstance()

            // calendarへの時刻設定
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.SECOND, 5)

            // ブロードキャスト設定
            setAlarmManager(calendar)
        }

        // アラームキャンセル
        cancelAlerm.setOnClickListener {
            cancelAlarmManager()
        }
    }

    /**
     * setAlarmManager
     *
     * ブロードキャスト（Androidシステムで非同期に発生するイベントがインテントで送られる時に使用する仕組み）
     * するインテントと時間の設定
     * アラーム時刻設定（アプリ）→ AlarmManager（Androidシステム）→ BroadcastReceiver（アプリ）
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setAlarmManager(calendar: Calendar) {

        // 設定用のAlarmManagerクラスのインスタンス作成
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // アラーム時刻になった時にAndroidシステムから発行されるインテントを作成（インテント先はAlarmBroadcastreceiver）
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)

        // ブロードキャスト実行のためにはペンディングインテントと呼ばれる特殊なインテントが必要
        val pending = PendingIntent.getBroadcast(this, 0, intent, 0)

        // AlarmManagerはAndroid OSバージョンごとに設定する（頻繁に仕様が変わっているため）
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val info = AlarmManager.AlarmClockInfo(calendar.timeInMillis, null)
                am.setAlarmClock(info, pending)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pending)
            }
            else -> {
                am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pending)
            }
        }
    }

    /**
     * cancelAlarmManager
     *
     * アラームキャンセル
     */
    private fun cancelAlarmManager() {
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        val pending = PendingIntent.getBroadcast(this, 0, intent, 0)
        am.cancel(pending)
    }
}
