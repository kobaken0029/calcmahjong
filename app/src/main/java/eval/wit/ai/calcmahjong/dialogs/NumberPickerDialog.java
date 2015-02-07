package eval.wit.ai.calcmahjong.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import eval.wit.ai.calcmahjong.R;

/**
 * Created by koba on 2015/02/07.
 */
public class NumberPickerDialog {


    /**
     * NumberPickerのダイアログを表示する。
     * @param context コンテキスト
     * @param view view
     * @param numberPicker numberPicker
     * @param minValue 最小値
     * @param maxValue 最大値
     * @param listener OK時のリスナー
     */
    public void showDialog(Context context, View view, NumberPicker numberPicker, String msg,
                           int minValue, int maxValue, DialogInterface.OnClickListener listener) {
        numberPicker.setMinValue(minValue);
        numberPicker.setMaxValue(maxValue);
        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme))
                .setTitle(msg)
                .setView(view)
                .setPositiveButton("OK", listener != null ? listener :
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        })
                .create().show();
    }
}
