package com.kobaken0029.calcmahjong.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kobaken0029.calcmahjong.R;
import com.kobaken0029.calcmahjong.CalcMahjong;
import com.kobaken0029.calcmahjong.model.db.DatabaseAdapter;
import com.kobaken0029.calcmahjong.model.entity.Player;
import com.kobaken0029.calcmahjong.model.entity.Record;
import com.kobaken0029.calcmahjong.resource.Consts;
import com.kobaken0029.calcmahjong.resource.ConstsManager;
import com.kobaken0029.calcmahjong.util.UiUtil;

public class ResultActivity extends AppCompatActivity {
    private TextView[] player1ScoreTxt = new TextView[4];
    private TextView[] player2ScoreTxt = new TextView[4];
    private TextView[] player3ScoreTxt = new TextView[4];
    private TextView[] player4ScoreTxt = new TextView[4];
    private TextView player1ScoreTotalTxt;
    private TextView player2ScoreTotalTxt;
    private TextView player3ScoreTotalTxt;
    private TextView player4ScoreTotalTxt;
    private ButtonRectangle rePlayBtn;

    private List<Player> players;
    private List<HashMap<Integer, Integer>> playersPointList;
    private HashMap<Integer, Integer> playerTotalScore;

    private int gameCnt;

    private CalcMahjong calcMahjong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        calcMahjong = (CalcMahjong) getApplication();
        gameCnt = calcMahjong.getGameCnt();

        players = calcMahjong.getPlayers();
        playersPointList = new ArrayList<>(calcMahjong.getPlayersPointList());


        // プレイヤー名をTextViewにセット
        ((TextView)findViewById(R.id.player1)).setText(players.get(0).getName());
        ((TextView)findViewById(R.id.player2)).setText(players.get(1).getName());
        ((TextView)findViewById(R.id.player3)).setText(players.get(2).getName());
        ((TextView)findViewById(R.id.player4)).setText(players.get(3).getName());

        player1ScoreTotalTxt = (TextView) findViewById(R.id.player1_total);
        player2ScoreTotalTxt = (TextView) findViewById(R.id.player2_total);
        player3ScoreTotalTxt = (TextView) findViewById(R.id.player3_total);
        player4ScoreTotalTxt = (TextView) findViewById(R.id.player4_total);

        // 半荘ごとのスコアを表に反映
        setScoreView();

        rePlayBtn = (ButtonRectangle) findViewById(R.id.rePlay);
        rePlayBtn.setOnClickListener(rePlayListener);

        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameSet();
            }
        });

        // 広告バナーを表示
        ((AdView)this.findViewById(R.id.adView_result)).loadAd(new AdRequest.Builder()
                .addTestDevice("21499EE04196C2E0E48CB407366D501F")
                .build());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 4半荘目の場合、再度半荘を出来ないようにする
        if (gameCnt == 4) {
            rePlayBtn.setVisibility(View.GONE);
        }
    }

    /**
     * もう一半荘ボタン押下時のリスナー。
     */
    View.OnClickListener rePlayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gameCnt++;
            calcMahjong.setGameCnt(gameCnt);
            startActivity(new Intent(ResultActivity.this, ScoreActivity.class));
            finish();
        }
    };

    /**
     * 現在のスコアを表にセット。
     */
    private void setScoreView() {
        String player1ScoreTxtResId;
        String player2ScoreTxtResId;
        String player3ScoreTxtResId;
        String player4ScoreTxtResId;
        int player1ScoreResId;
        int player2ScoreResId;
        int player3ScoreResId;
        int player4ScoreResId;

        String numOfHantyanTxtResId = "hantyan" + gameCnt;
        Log.d("HANTYAN", numOfHantyanTxtResId);
        int numOfHantyanResId = getResources().getIdentifier(numOfHantyanTxtResId, "id", getPackageName());
        TextView numOfHantyanTxt = (TextView) findViewById(numOfHantyanResId);
        numOfHantyanTxt.setTextColor(Color.YELLOW);
        playerTotalScore = new HashMap<>();
        int[] totalScoreBuf = new int[ConstsManager.getNumOfPlayer()];

        for (int i = 0; i < gameCnt; i++) {
            player1ScoreTxtResId = "player1_score" + (i + 1);
            player2ScoreTxtResId = "player2_score" + (i + 1);
            player3ScoreTxtResId = "player3_score" + (i + 1);
            player4ScoreTxtResId = "player4_score" + (i + 1);
            player1ScoreResId = getResources().getIdentifier(player1ScoreTxtResId, "id", getPackageName());
            player2ScoreResId = getResources().getIdentifier(player2ScoreTxtResId, "id", getPackageName());
            player3ScoreResId = getResources().getIdentifier(player3ScoreTxtResId, "id", getPackageName());
            player4ScoreResId = getResources().getIdentifier(player4ScoreTxtResId, "id", getPackageName());
            player1ScoreTxt[i] = (TextView) findViewById(player1ScoreResId);
            player2ScoreTxt[i] = (TextView) findViewById(player2ScoreResId);
            player3ScoreTxt[i] = (TextView) findViewById(player3ScoreResId);
            player4ScoreTxt[i] = (TextView) findViewById(player4ScoreResId);

            // 現在のゲームの場合、得点を計算
            if (i == gameCnt - 1) {
                calcScore(playersPointList.get(i));
            }

            player1ScoreTxt[i].setText(playersPointList.get(i).get(players.get(0).getId()).toString());
            player2ScoreTxt[i].setText(playersPointList.get(i).get(players.get(1).getId()).toString());
            player3ScoreTxt[i].setText(playersPointList.get(i).get(players.get(2).getId()).toString());
            player4ScoreTxt[i].setText(playersPointList.get(i).get(players.get(3).getId()).toString());

            totalScoreBuf[0] += playersPointList.get(i).get(players.get(0).getId());
            totalScoreBuf[1] += playersPointList.get(i).get(players.get(1).getId());
            totalScoreBuf[2] += playersPointList.get(i).get(players.get(2).getId());
            totalScoreBuf[3] += playersPointList.get(i).get(players.get(3).getId());
        }

        playerTotalScore.put(players.get(0).getId(), totalScoreBuf[0]);
        playerTotalScore.put(players.get(1).getId(), totalScoreBuf[1]);
        playerTotalScore.put(players.get(2).getId(), totalScoreBuf[2]);
        playerTotalScore.put(players.get(3).getId(), totalScoreBuf[3]);

        player1ScoreTotalTxt.setText(String.valueOf(playerTotalScore.get(players.get(0).getId())));
        player2ScoreTotalTxt.setText(String.valueOf(playerTotalScore.get(players.get(1).getId())));
        player3ScoreTotalTxt.setText(String.valueOf(playerTotalScore.get(players.get(2).getId())));
        player4ScoreTotalTxt.setText(String.valueOf(playerTotalScore.get(players.get(3).getId())));
    }

    /**
     * スコアを計算する。
     *
     * @param playersPoint プレイヤーの得点
     */
    private void calcScore(HashMap<Integer, Integer> playersPoint) {
        int topId = players.get(0).getId();

        // 順位を決定する
        // プレイヤーの順位<プレイヤーID， 順位>
        HashMap<Integer, Integer> playersRankingHashMap = calcMahjong.getRankingHashMap(playersPoint);

        // プレイヤーの持ち点からスコアを算出
        for (Player p : players) {
            Log.d("SCORE_BEFORE", p.getName() + " " + playersPoint.get(p.getId()).toString());
            playersPoint.put(p.getId(), (int)(new BigDecimal(playersPoint.get(p.getId()) / 1000.0)
                                                .setScale(0, BigDecimal.ROUND_HALF_DOWN).doubleValue())
                                        - (ConstsManager.getOriginScore(getApplicationContext()) / 1000));
            Log.d("SCORE_AFTER", p.getName() + " " + playersPoint.get(p.getId()).toString());

            // トップを探す
            if (playersRankingHashMap.get(p.getId()) == Consts.TOP) {
                topId = p.getId();
            }
        }

        // トップの得点にオカを反映させる
        playersPoint.put(topId, playersPoint.get(topId) + ConstsManager.getOka(getApplicationContext()));

        // 順位ウマを反映させる
        for (Player p : players) {
            playersPoint.put(p.getId(),
                    playersPoint.get(p.getId())
                            + ConstsManager.getUma(getApplicationContext(), playersRankingHashMap.get(p.getId())));
        }

        // 得点リストに追加
        playersPointList.add(playersPoint);
    }

    /**
     * 試合終了時の処理。
     */
    private void gameSet() {
        UiUtil.showDialog(ResultActivity.this, null, getResources().getString(R.string.game_set_message),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<Integer, Integer> rankingHashMap = calcMahjong.getRankingHashMap(playerTotalScore);
                        DatabaseAdapter adapter = calcMahjong.getDbAdapter();
                        adapter.open();

                        for (Player p : players) {
                            Record record = null;

                            // 成績を取得
                            Cursor c = adapter.getPlayerRecord(p.getId());
                            if (c.moveToFirst()) {
                                record = new Record(
                                        c.getInt(c.getColumnIndex(DatabaseAdapter.COL_ID)),
                                        c.getInt(c.getColumnIndex(DatabaseAdapter.COL_PLAYER_ID)),
                                        c.getDouble(c.getColumnIndex(DatabaseAdapter.COL_SCORE)),
                                        c.getInt(c.getColumnIndex(DatabaseAdapter.COL_NUM_OF_PLAY)),
                                        c.getInt(c.getColumnIndex(DatabaseAdapter.COL_TOP)),
                                        c.getInt(c.getColumnIndex(DatabaseAdapter.COL_SECOND)),
                                        c.getInt(c.getColumnIndex(DatabaseAdapter.COL_THIRD)),
                                        c.getInt(c.getColumnIndex(DatabaseAdapter.COL_LAST)),
                                        c.getInt(c.getColumnIndex(DatabaseAdapter.COL_WINNING)),
                                        c.getInt(c.getColumnIndex(DatabaseAdapter.COL_DISCARDING)));
                            }

                            // 成績をつける
                            if (record != null) {
                                record.setTotalScore(playerTotalScore.get(p.getId()));
                                record.setTotalPlay(record.getTotalPlay() + calcMahjong.getGameCnt());
                                record.setWinning(record.getWinning() + calcMahjong.getWinningHashMap().get(p.getId()));
                                record.setDiscarding(record.getDiscarding() + calcMahjong.getDiscardingHashMap().get(p.getId()));
                                record.setRanking(rankingHashMap.get(p.getId()));

                                Log.d("FINAL_RESULT_RANKING",
                                        p.getName() + " " + rankingHashMap.get(p.getId()) + "位");
                                // 成績を更新
                                adapter.updateRecord(record);
                            }
                        }
                        adapter.close();

                        dialog.cancel();
                        calcMahjong.setGameCnt(1);
                        calcMahjong.setPlayersPointList(new ArrayList<HashMap<Integer, Integer>>());
                        calcMahjong.setNumOfDepositBar(0);
                        finish();
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        } else {
            gameSet();
            return false;
        }
    }
}
