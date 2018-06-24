package com.yhishi.alarmclock

import android.app.Dialog
import android.os.Bundle
import org.jetbrains.anko.toast
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

// ダイアログ用クラス
class SimapleAlertDialog: DialogFragment() {

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
                context.toast("起きるがクリックされました")
            }
            // 2番目のボタン
            setNegativeButton("あと5分") { dialog, which ->
                context.toast("あと5分がクリックされました")
            }
        }
        return builder.create()
    }

}
