package eval.wit.ai.calcmahjong.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import eval.wit.ai.calcmahjong.R;

/**
 * Created by koba on 2015/01/26.
 */
public class UiUtil {

    /**
     * ダイアログを表示。
     * @param c コンテキスト
     * @param msg メッセージ
     * @param listener リスナー
     */
    public static void showDialog(Context c, String msg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(c, R.style.AppTheme));
        builder.setMessage(msg)
                .setPositiveButton("OK", listener != null ? listener : new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setCancelable(false);

        if (listener != null) {
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
     * @param c コンテキスト
     * @param msg メッセージ
     */
    public static void showToast(Context c, String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }
}
