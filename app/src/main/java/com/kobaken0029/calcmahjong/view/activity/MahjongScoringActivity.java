package com.kobaken0029.calcmahjong.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;

import com.kobaken0029.calcmahjong.R;
import com.kobaken0029.calcmahjong.CalcMahjong;
import com.kobaken0029.calcmahjong.model.entity.Player;
import com.kobaken0029.calcmahjong.resource.Consts;
import com.kobaken0029.calcmahjong.resource.ConstsManager;
import com.kobaken0029.calcmahjong.util.AudioUtil;
import com.kobaken0029.calcmahjong.util.UiUtil;

public class MahjongScoringActivity extends AppCompatActivity {
    private Spinner winnerSpinner;
    private Spinner loserSpinner;
    private Spinner numberOfHanSpinner;
    private Spinner numberOfFuSpinner;

    private ArrayList<Player> players;
    private Player parent;
    private Player winner;
    private Player loser;
    private int[] point;

    private CalcMahjong calcMahjong;

    private MediaPlayer mp;

    private boolean isClicked;
    private boolean isParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahjong_scoring);
        calcMahjong = (CalcMahjong) getApplication();
        mp = new MediaPlayer();

        winnerSpinner = (Spinner) findViewById(R.id.winnerSpinner);
        loserSpinner = (Spinner) findViewById(R.id.loserSpinner);
        numberOfHanSpinner = (Spinner) findViewById(R.id.hanSpinner);
        numberOfFuSpinner = (Spinner) findViewById(R.id.fuSpinner);

        // スピナーに値をセット。
        setSpinner();

        findViewById(R.id.calc).setOnClickListener(calcListener);

        // 広告バナーを表示
        ((AdView)this.findViewById(R.id.adView_scoring)).loadAd(new AdRequest.Builder()
                .addTestDevice("21499EE04196C2E0E48CB407366D501F")
                .build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    /**
     * スピナーに値をセット。
     */
    public void setSpinner() {
        ArrayAdapter<String> winnerAdapter = new ArrayAdapter<>(MahjongScoringActivity.this, R.layout.spinner_item);
        ArrayAdapter<String> loserAdapter = new ArrayAdapter<>(MahjongScoringActivity.this, R.layout.spinner_item);

        // 参加者を取得
        players = calcMahjong.getPlayers();

        // 参加中のプレイヤーをセット
        for (Player p : players) {
            winnerAdapter.add(p.getName());
            winnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            loserAdapter.add(p.getName());
            loserAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        winnerSpinner.setAdapter(winnerAdapter);

        // 他家を加えセット
        loserAdapter.add("他家(ツモアガリ)");
        loserSpinner.setAdapter(loserAdapter);
        loserSpinner.setSelection(1);

        // 飜数セット
        numberOfHanSpinner.setAdapter(ConstsManager.getHanAdapter(MahjongScoringActivity.this));

        // 符数セット
        numberOfFuSpinner.setAdapter(ConstsManager.getFuAdapter(this));
        numberOfFuSpinner.setSelection(2);
    }

    /**
     * 計算ボタン押下時のリスナー。
     */
    View.OnClickListener calcListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int cnt = 0;

            // 和了者、放銃者、親をセット
            for (Player p : players) {
                if (p.getName().equals(winnerSpinner.getSelectedItem().toString())) {
                    winner = p;
                }

                if (p.getName().equals(loserSpinner.getSelectedItem().toString())) {
                    loser = p;
                }

                if (calcMahjong.getIsParent()[cnt]) {
                    parent = p;
                }

                cnt++;
            }

            // 自摸和了の場合
            if (loserSpinner.getSelectedItem().toString().equals("他家(ツモアガリ)")) {
                loser = null;
            }

            boolean isTumo = loser == null;

            // 和了者と放銃者が同一人物の場合
            if (winner.equals(loser)) {
                UiUtil.showToast(MahjongScoringActivity.this, "和了者と放銃者が一致しています!");
                return;
            }

            // 20符ロン和了の場合
            if (!isTumo && numberOfFuSpinner.getSelectedItem().toString().equals(Consts.NIZYU_FU)) {
                UiUtil.showToast(MahjongScoringActivity.this, "20符のロン和了は存在しません。");
                return;
            }

            // 20符1翻の場合
            if (numberOfFuSpinner.getSelectedItem().toString().equals(Consts.NIZYU_FU)
                    && numberOfHanSpinner.getSelectedItem().toString().equals(Consts.I_HAN)) {
                UiUtil.showToast(MahjongScoringActivity.this, "20符1翻は存在しません。");
                return;
            }

            // 25符1翻の場合
            if (numberOfFuSpinner.getSelectedItem().toString().equals(Consts.NIZYUGO_FU)
                    && numberOfHanSpinner.getSelectedItem().toString().equals(Consts.I_HAN)) {
                UiUtil.showToast(MahjongScoringActivity.this, "25符1翻は存在しません。");
                return;
            }

            // 和了者が親かどうかの判定
            isParent = parent.getId() == winner.getId();

            // 得点を取得
            point = ConstsManager.calcPoint(numberOfFuSpinner.getSelectedItem().toString(),
                    numberOfHanSpinner.getSelectedItem().toString(),
                    isParent,
                    isTumo,
                    calcMahjong.getNumOfhonba());

            // 得点結果をダイアログで表示
            showPointResultDialog(point, isTumo);
        }
    };

    /**
     * 得点をダイアログで表示。
     *
     * @param point 得点
     */
    private void showPointResultDialog(int[] point, boolean isTumo) {
        int[] natureScores = new int[3];
        int honbaTumoPoint = calcMahjong.getNumOfhonba() * 100;
        int honbaRonPoint = calcMahjong.getNumOfhonba() * 300;

        for (int i = 0; i < 3; i++) {
            if (!isTumo) {
                natureScores[0] = point[0] - honbaRonPoint;
                break;
            }
            natureScores[i] = point[i] - honbaTumoPoint;
        }

        String msg = "親 : " + parent.getName() + "\n"
                + (calcMahjong.getNumOfhonba() != 0 ? calcMahjong.getNumOfhonba() + "本場\n" : "")
                + (isTumo ? winner.getName() + " 自摸" : loser.getName() + " → " + winner.getName()) + "\n"
                + numberOfFuSpinner.getSelectedItem().toString()
                + numberOfHanSpinner.getSelectedItem().toString() + "\n"
                + (isTumo
                    ? (parent.getName().equals(winner.getName())
                        ? natureScores[0] + "オール"
                        : natureScores[1] + "・" + natureScores[0]
                        + (calcMahjong.getNumOfhonba() != 0 ? "(+" + honbaTumoPoint + ")" : ""))
                    : natureScores[0] + (calcMahjong.getNumOfhonba() != 0 ? "(+" + honbaRonPoint + ")" : ""));

        UiUtil.showDialog(MahjongScoringActivity.this, null, msg, listener);
    }

    /**
     * ダイアログでPositiveなボタンを押下した時のリスナー。
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, int which) {
            if (isClicked) {
                UiUtil.showToast(MahjongScoringActivity.this,
                        getResources().getString(R.string.already_processing_message));
                return;
            }

            // 親なら連荘
            if (isParent) {
                calcMahjong.setNumOfhonba(calcMahjong.getNumOfhonba() + 1);
            } else {
                calcMahjong.flowParent();
                calcMahjong.setNumOfhonba(0);
            }

            isClicked = true;
            String uri = Consts.TUMO_VOICE_URL;
            Intent intent = new Intent();
            intent.putExtra("point", point);
            intent.putExtra("winner", winner);
            intent.putExtra("parent", parent);

            // 和了数をカウント
            countWinning();

            if (loser != null) {
                intent.putExtra("loser", loser);
                uri = Consts.RON_VOICE_URL;
                countDiscarding();
            }

            setResult(RESULT_OK, intent);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mp.release();
                    mp = null;
                    dialog.cancel();
                    finish();
                }
            }, Consts.DELAY_TIME);
            AudioUtil.play(mp, getApplicationContext(), uri, null);
        }
    };

    /**
     * 和了数をカウントする。
     */
    private void countWinning() {
        HashMap<Integer, Integer> winnerHashMap = calcMahjong.getWinningHashMap();
        winnerHashMap.put(winner.getId(), winnerHashMap.get(winner.getId()) + 1);
        calcMahjong.setWinningHashMap(winnerHashMap);
    }

    /**
     * 放銃数をカウントする。
     */
    private void countDiscarding() {
        HashMap<Integer, Integer> discardingHashMap = calcMahjong.getDiscardingHashMap();
        discardingHashMap.put(loser.getId(), discardingHashMap.get(loser.getId()) + 1);
        calcMahjong.setDiscardingHashMap(discardingHashMap);
    }
}
