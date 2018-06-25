package com.yhishi.alarmclock

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.DatePicker
import android.widget.TimePicker
import java.util.*
import kotlin.math.min

// ダイアログ用クラス
class SimapleAlertDialog: DialogFragment() {

    interface OnClicklistener {
        fun onPositiveClick()
        fun onNegativeClick()
    }

    private lateinit var listener: OnClicklistener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // OnClicklistenerを実装しているかどうか確認
        if(context is SimapleAlertDialog.OnClicklistener) {
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context
        if(context == null) {
            return super.onCreateDialog(savedInstanceState)
        }
        // AlertDialog.Builderのインスタンス生成
        val builder = AlertDialog.Builder(context).apply {
            setMessage("時間になりました！")

            // ダイアログに表示する1番目のボタン設定（onClickメソッドのみのSAMインタフェースなので,ラムダ式記述）
            setPositiveButton("起きる") { dialog, which ->
                listener.onPositiveClick()
            }
            // 2番目のボタン
            setNegativeButton("あと5分") { dialog, which ->
                listener.onNegativeClick()
            }
        }
        return builder.create()
    }
}

// 日付を選択するダイアログ
class DatePickerFragment: DialogFragment(),
        DatePickerDialog.OnDateSetListener {

    interface OnDateSelectedListener {
        // 日付が選択された時の処理
        fun onSelected(year: Int, month: Int, date: Int)
    }

    private lateinit var listener: OnDateSelectedListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is OnDateSelectedListener) {
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val date = c.get(Calendar.DAY_OF_MONTH)
        // DatePickerDialogのコンストラクタでインスタンスを生成し、返却（現在時刻を初期値設定）
        return DatePickerDialog(context, this, year, month, date)
    }

    // 日付が選択された時の処理
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, date: Int) {
        listener.onSelected(year, month, date)
    }
}

// 時刻を選択するダイアログ
class TimePickerFragment: DialogFragment(),
        TimePickerDialog.OnTimeSetListener {

    interface OnTimeSelectedListener {
        fun onSelected(hourOfDay: Int, minute: Int)
    }

    private lateinit var listener: OnTimeSelectedListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is TimePickerFragment.OnTimeSelectedListener) {
            listener = context
        }
    }

    // 現在時刻の設定
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        return TimePickerDialog(context, this, hour, minute, true)
    }

    // 時刻選択時の処理
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener.onSelected(hourOfDay, minute)
    }

}