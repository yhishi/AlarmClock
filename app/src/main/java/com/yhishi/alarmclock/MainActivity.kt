package com.yhishi.alarmclock

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(),
        SimapleAlertDialog.OnClicklistener,
        DatePickerFragment.OnDateSelectedListener,
        TimePickerFragment.OnTimeSelectedListener {

    // 日付選択後の処理
    override fun onSelected(year: Int, month: Int, date: Int) {
        val c = Calendar.getInstance()
        c.set(year, month, date)
        dateText.text = DateFormat.format("yyyy/MM/dd", c)
    }

    // 時刻選択後の処理
    override fun onSelected(hourOfDay: Int, minute: Int) {
        timetext.text = "%1$02d:%2$02d".format(hourOfDay, minute)
    }

    override fun onPositiveClick() {
        finish()
    }

    override fun onNegativeClick() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MINUTE, 5)
        setAlarmManager(calendar)
        finish()
    }

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
            // ダイアログで入力された日付・時刻をdate型に変換
            val date = "${dateText.text} ${timetext.text}".toDate()
            when {
                date != null -> {
                    // calendarへの時刻設定
                    val calendar = Calendar.getInstance()
                    calendar.time = date

                    // ブロードキャスト設定
                    setAlarmManager(calendar)
                    toast("アラームをセットしました")
                }
                else -> {
                    toast("日付の形式が正しくありません")
                }
            }
        }

        // アラームキャンセル
        cancelAlerm.setOnClickListener {
            cancelAlarmManager()
        }

        dateText.setOnClickListener {
            // 日付選択ダイアログ表示
            val dialog = DatePickerFragment()
            dialog.show(supportFragmentManager, "date_dialog")
        }

        timetext.setOnClickListener {
            // 時刻選択ダイアログ表示
            val dialog = TimePickerFragment()
            dialog.show(supportFragmentManager, "time_dialog")
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

    // Date型に変換
    fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date? {
        val sdFormat = try {
            // Dateに変換する文字列形式を指定して、SimpleDateFormatインスタンス生成
            SimpleDateFormat(pattern)
        } catch (e: IllegalArgumentException) {
            null
        }
        val date = sdFormat?.let {
            try {
                // 文字列フォーマットを渡して、Date型変換
                it.parse(this)
            } catch (e: ParseException) {
                null
            }
        }
        return date
    }
}
