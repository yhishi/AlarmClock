package com.yhishi.alarmclock

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

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
