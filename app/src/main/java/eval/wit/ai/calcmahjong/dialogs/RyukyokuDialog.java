package eval.wit.ai.calcmahjong.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import eval.wit.ai.calcmahjong.R;
import eval.wit.ai.calcmahjong.models.clients.AppController;
import eval.wit.ai.calcmahjong.models.entities.Player;
import eval.wit.ai.calcmahjong.resources.Consts;
import eval.wit.ai.calcmahjong.utilities.AudioUtil;

public class RyukyokuDialog {
    private CheckBox[] checkBoxes = new CheckBox[4];
    private View layout;
    private ArrayList<Player> players;

    private MediaPlayer mp;
    private Activity activity;

    /**
     * ダイアログを表示します。
     *
     * @param context  コンテキスト
     * @param activity アクティビティ
     */
    public void showDialog(final Context context, final Activity activity) {
        mp = new MediaPlayer();
        this.activity = activity;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layout = inflater.inflate(R.layout.activity_ryukyoku_dialog,
                (ViewGroup) activity.findViewById(R.id.ryukyoku_dialog_layout));

        // 参加プレイヤーを取得
        players = ((AppController) activity.getApplication()).getPlayers();

        // ダイアログのレイアウトに値をセット
        setView(context, activity, players);

        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme))
                .setTitle(context.getResources().getString(R.string.ryukyoku_title))
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra("tenpai", getTenpaiPlayers(players));
                        activity.setResult(Consts.RYUKYOKU_CODE, intent);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish(dialog);
                            }
                        }, Consts.DELAY_TIME);
                        AudioUtil.play(mp, context, Consts.RYUKYOKU_VOICE_URL, null);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish(dialog);
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            finish(dialog);
                            return true;
                        }
                        return false;
                    }
                }).create().show();
    }

    /**
     * ダイアログを終了します。
     * @param dialog ダイアログ
     */
    private void finish(DialogInterface dialog) {
        Log.d("HANDLER", "START");
        if (mp != null) {
            mp.release();
            mp = null;
        }
        dialog.cancel();
        activity.finish();
        Log.d("HANDLER", "END");
    }

    /**
     * 画面に値をセットします。
     *
     * @param context  コンテキスト
     * @param activity アクティビティ
     * @param players  プレイヤー群
     */
    private void setView(Context context, Activity activity, List<Player> players) {
        String checkBoxTxtResId;
        int checkBoxResId;
        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxTxtResId = "player_" + (i + 1);
            checkBoxResId = context.getResources().getIdentifier(checkBoxTxtResId, "id", activity.getPackageName());
            checkBoxes[i] = (CheckBox) layout.findViewById(checkBoxResId);
            checkBoxes[i].setText(players.get(i).getName());
        }
    }

    /**
     * 聴牌者群を取得。
     *
     * @param players プレイヤー群
     * @return 聴牌者群
     */
    private ArrayList<Player> getTenpaiPlayers(List<Player> players) {
        ArrayList<Player> tenpaiPlayers = new ArrayList<>();
        for (CheckBox c : checkBoxes) {
            if (c.isChecked()) {
                for (Player p : players) {
                    if (p.getName().equals(c.getText().toString())) {
                        tenpaiPlayers.add(p);
                    }
                }
            }
        }

        return tenpaiPlayers;
    }
}
