package com.kobaken0029.calcmahjong.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.kobaken0029.calcmahjong.R;

public class UiUtil {

    /**
     * ダイアログを表示。
     *
     * @param c        コンテキスト
     * @param title    タイトル
     * @param msg      メッセージ
     * @param listener リスナー
     */
    public static void showDialog(Context c, String title, String msg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(c, R.style.AppTheme));
        builder.setMessage(msg)
                .setPositiveButton(c.getResources().getString(R.string.ok),
                        listener != null ? listener : new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                .setCancelable(false);

        if (title != null && !title.equals("")) {
            builder.setTitle(title);
        }

        if (listener != null) {
            builder.setNegativeButton(c.getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
        }

        builder.show();
    }

    /**
     * トーストを表示。
     *
     * @param c   コンテキスト
     * @param msg メッセージ
     */
    public static void showToast(Context c, String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }
}
