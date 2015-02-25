package eval.wit.ai.calcmahjong.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eval.wit.ai.calcmahjong.R;
import eval.wit.ai.calcmahjong.models.clients.AppController;
import eval.wit.ai.calcmahjong.models.db.DatabaseAdapter;
import eval.wit.ai.calcmahjong.models.entities.Player;
import eval.wit.ai.calcmahjong.models.entities.Record;
import eval.wit.ai.calcmahjong.resources.Consts;
import eval.wit.ai.calcmahjong.resources.ConstsManager;
import eval.wit.ai.calcmahjong.utilities.UiUtil;

public class ResultActivity extends ActionBarActivity {
    private TextView[] player1ScoreTxt = new TextView[4];
    private TextView[] player2ScoreTxt = new TextView[4];
    private TextView[] player3ScoreTxt = new TextView[4];
    private TextView[] player4ScoreTxt = new TextView[4];
    private TextView player1ScoreTotalTxt;
    private TextView player2ScoreTotalTxt;
    private TextView player3ScoreTotalTxt;
    private TextView player4ScoreTotalTxt;
    private Button rePlayBtn;

    private List<Player> players;
    private List<HashMap<Integer, Integer>> playersPointList;
    private int[] playerTotalScore = new int[ConstsManager.getNumOfPlayer()];

    private int gameCnt;

    private AppController appController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        appController = (AppController) getApplication();
        gameCnt = appController.getGameCnt();

        players = appController.getPlayers();
        playersPointList = new ArrayList<>(appController.getPlayersPointList());


        // プレイヤー名をTextViewにセット
        TextView player1Txt = (TextView) findViewById(R.id.player1);
        TextView player2Txt = (TextView) findViewById(R.id.player2);
        TextView player3Txt = (TextView) findViewById(R.id.player3);
        TextView player4Txt = (TextView) findViewById(R.id.player4);
        player1Txt.setText(players.get(0).getName());
        player2Txt.setText(players.get(1).getName());
        player3Txt.setText(players.get(2).getName());
        player4Txt.setText(players.get(3).getName());

        player1ScoreTotalTxt = (TextView) findViewById(R.id.player1_total);
        player2ScoreTotalTxt = (TextView) findViewById(R.id.player2_total);
        player3ScoreTotalTxt = (TextView) findViewById(R.id.player3_total);
        player4ScoreTotalTxt = (TextView) findViewById(R.id.player4_total);

        // 半荘ごとのスコアを表に反映
        setScoreView();

        rePlayBtn = (Button) findViewById(R.id.rePlay);
        rePlayBtn.setOnClickListener(rePlayListener);

        Button finishBtn = (Button) findViewById(R.id.finish);
        finishBtn.setOnClickListener(finishListener);
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
                calcScore();
            }

            player1ScoreTxt[i].setText(playersPointList.get(i).get(players.get(0).getId()).toString());
            player2ScoreTxt[i].setText(playersPointList.get(i).get(players.get(1).getId()).toString());
            player3ScoreTxt[i].setText(playersPointList.get(i).get(players.get(2).getId()).toString());
            player4ScoreTxt[i].setText(playersPointList.get(i).get(players.get(3).getId()).toString());

            playerTotalScore[0] += playersPointList.get(i).get(players.get(0).getId());
            playerTotalScore[1] += playersPointList.get(i).get(players.get(1).getId());
            playerTotalScore[2] += playersPointList.get(i).get(players.get(2).getId());
            playerTotalScore[3] += playersPointList.get(i).get(players.get(3).getId());
        }

        player1ScoreTotalTxt.setText(String.valueOf(playerTotalScore[0]));
        player2ScoreTotalTxt.setText(String.valueOf(playerTotalScore[1]));
        player3ScoreTotalTxt.setText(String.valueOf(playerTotalScore[2]));
        player4ScoreTotalTxt.setText(String.valueOf(playerTotalScore[3]));
    }

    /**
     * スコアを計算する。
     */
    private void calcScore() {
        HashMap<Integer, Integer> playersPoint = playersPointList.get(gameCnt - 1);

        // プレイヤーの持ち点からスコアを算出
        for (Player p : players) {
            Log.d("SCORE_BEFORE", p.getName() + " " + playersPoint.get(p.getId()).toString());
            playersPoint.put(p.getId(), (playersPoint.get(p.getId()) - 30000) / 1000);
            Log.d("SCORE_AFTER", p.getName() + " " + playersPoint.get(p.getId()).toString());
        }

        // 順位を決定する
        // プレイヤーの順位<プレイヤーID， 順位>
        HashMap<Integer, Integer> playersRankingHashMap = new HashMap<>();
        int topId = players.get(0).getId();
        for (Player p : players) {

            int ranking = 1;
            for (Player exp : players) {
                // 現在のプレイヤー以外の場合
                if (exp.getId() != p.getId()) {
                    if (playersPoint.get(p.getId()) < playersPoint.get(exp.getId())) {
                        ranking++;
                    }
                }
            }

            if (playersRankingHashMap.containsValue(ranking)) {
                ranking++;
            }
            playersRankingHashMap.put(p.getId(), ranking);

            // トップを探す
            if (ranking == Consts.TOP) {
                topId = p.getId();
            }
            Log.d("RANKING", p.getName() + " " + playersRankingHashMap.get(p.getId()) + "位");
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
     * もう一半荘ボタン押下時のリスナー。
     */
    View.OnClickListener rePlayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gameCnt++;
            appController.setGameCnt(gameCnt);
            startActivity(new Intent(ResultActivity.this, ScoreActivity.class));
            finish();
        }
    };

    /**
     * ゲーム終了ボタン押下時のリスナー。
     */
    View.OnClickListener finishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gameSet();
        }
    };

    /**
     * 試合終了時の処理。
     */
    private void gameSet() {
        UiUtil.showDialog(ResultActivity.this, getResources().getString(R.string.game_set_message),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        appController.setGameCnt(1);
                        appController.setPlayersPointList(new ArrayList<HashMap<Integer, Integer>>());
                        appController.setNumOfDepositBar(0);

                        DatabaseAdapter adapter = appController.getDbAdapter();
                        adapter.open();
                        int i = 0;
                        for (Player p : players) {
                            Record record = null;
                            int score = 0;

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

                            // 該当プレイヤーの総スコアを取得
                            switch (i) {
                                case 0:
                                    score = Integer.parseInt(player1ScoreTotalTxt.getText().toString());
                                    break;
                                case 1:
                                    score = Integer.parseInt(player2ScoreTotalTxt.getText().toString());
                                    break;
                                case 2:
                                    score = Integer.parseInt(player3ScoreTotalTxt.getText().toString());
                                    break;
                                case 3:
                                    score = Integer.parseInt(player4ScoreTotalTxt.getText().toString());
                                    break;
                            }

                            // 成績をつける
                            if (record != null) {
                                record.setTotalScore(score);
                                record.setWinning(record.getWinning() + appController.getWinningHashMap().get(p.getId()));
                                record.setDiscarding(record.getDiscarding() + appController.getDiscardingHashMap().get(p.getId()));
                            }

                            // 成績を更新
                            Log.d("FINAL_RESULT_RANKING", p.getName() + " " + getFinalResultRanking(i) + "位");
                            adapter.updateRecord(record, getFinalResultRanking(i));
                            i++;
                        }
                        adapter.close();

                        finish();
                    }
                });
    }

    /**
     * 最終的なプレイヤーの順位を取得。
     * @param numOfSeat プレイヤーの席
     * @return 順位
     */
    private int getFinalResultRanking(int numOfSeat) {
        int ranking = 1;

        double buf = playerTotalScore[numOfSeat];
        for (int i = 0; i < playerTotalScore.length; i++) {
            if (i == numOfSeat) {
                continue;
            }

            if (buf < playerTotalScore[i]) {
                ranking++;
            }
        }

        return ranking;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
