package eval.wit.ai.calcmahjong.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
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

import eval.wit.ai.calcmahjong.R;
import eval.wit.ai.calcmahjong.models.clients.AppController;
import eval.wit.ai.calcmahjong.models.entities.Player;
import eval.wit.ai.calcmahjong.resources.Consts;
import eval.wit.ai.calcmahjong.resources.ConstsManager;
import eval.wit.ai.calcmahjong.utilities.AudioUtil;
import eval.wit.ai.calcmahjong.utilities.UiUtil;

public class ScoreActivity extends ActionBarActivity {
    private TextView p1ScoreTxt;
    private TextView p2ScoreTxt;
    private TextView p3ScoreTxt;
    private TextView p4ScoreTxt;

    private ArrayList<Player> players;
    private HashMap<Integer, Integer> playersPoint;
    private HashMap<Integer, Boolean> isCallPlayers;

    private AppController appController;

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        appController = (AppController) getApplication();

        init();

        Button calcBtn = (Button) findViewById(R.id.calc);
        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, MahjongScoringActivity.class);
                startActivityForResult(intent, Consts.REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mp = new MediaPlayer();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Consts.REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    int[] point = data.getIntArrayExtra("point");
                    Player winner = (Player) data.getSerializableExtra("winner");
                    Player loser = (Player) data.getSerializableExtra("loser");
                    Player parent = (Player) data.getSerializableExtra("parent");

                    String msg = winner.getName() + "←"
                            + (loser == null ? "他家" : loser.getName()) + " "
                            + (loser == null ? "" + point[0] + ":" + point[1] + ":" + point[2] : point[0]);
                    UiUtil.showToast(ScoreActivity.this, msg);

                    // 和了者の得点
                    int getWinnerPoint = 0;
                    for (int p : point) {
                        getWinnerPoint += p;
                    }

                    // 供託棒分の加点
                    getWinnerPoint += appController.getNumOfDepositBar() * Consts.SEN;
                    appController.setNumOfDepositBar(0);

                    boolean isTumo = loser == null;
                    ArrayList<Player> exceptingWinnerList = new ArrayList<>();
                    for (Player p : players) {
                        // 和了者の持ち点に得点を追加
                        if (winner.getId() == p.getId()) {
                            playersPoint.put(winner.getId(), playersPoint.get(winner.getId()) + getWinnerPoint);
                        } else {
                            // 和了者以外をリストに追加
                            exceptingWinnerList.add(p);
                        }

                        // 放銃者の持ち点から点数を引く
                        if (!isTumo) {
                            if (loser.getId() == p.getId()) {
                                playersPoint.put(loser.getId(), playersPoint.get(loser.getId()) - point[0]);
                            }
                        }
                    }

                    // 自摸和了の場合
                    if (isTumo) {
                        int i = 1;
                        for (Player p : exceptingWinnerList) {
                            if (p.getId() == parent.getId()) {
                                playersPoint.put(p.getId(), playersPoint.get(p.getId()) - point[0]);
                            } else {
                                playersPoint.put(p.getId(), playersPoint.get(p.getId()) - point[i]);
                                i++;
                            }

                            if (i == 3) {
                                i = 0;
                            }
                        }
                    }
                } else if (resultCode == Consts.RYUKYOKU_CODE) {
                    // 流局時の聴牌者人数に応じて得点を振り分け
                    calcRyukyokuPoint((ArrayList<Player>) data.getSerializableExtra("tenpai"));
                }

                // 計算処理後のスコアを画面に反映
                setPlayersScore();

                // 全プレイヤーの立直状態を解除
                isCallPlayers.put(players.get(0).getId(), Boolean.FALSE);
                isCallPlayers.put(players.get(1).getId(), Boolean.FALSE);
                isCallPlayers.put(players.get(2).getId(), Boolean.FALSE);
                isCallPlayers.put(players.get(3).getId(), Boolean.FALSE);
                break;
            default:
                break;
        }

    }

    /**
     * 画面に表示するデータを初期化。
     */
    private void init() {
        playersPoint = new HashMap<>();
        isCallPlayers = new HashMap<>();
        players = ((AppController) getApplication()).getPlayers();
        Integer firstScore = ConstsManager.getFirstScore();

        playersPoint.put(players.get(0).getId(), firstScore);
        playersPoint.put(players.get(1).getId(), firstScore);
        playersPoint.put(players.get(2).getId(), firstScore);
        playersPoint.put(players.get(3).getId(), firstScore);

        isCallPlayers.put(players.get(0).getId(), Boolean.FALSE);
        isCallPlayers.put(players.get(1).getId(), Boolean.FALSE);
        isCallPlayers.put(players.get(2).getId(), Boolean.FALSE);
        isCallPlayers.put(players.get(3).getId(), Boolean.FALSE);

        TextView p1Txt = (TextView) findViewById(R.id.p1);
        TextView p2Txt = (TextView) findViewById(R.id.p2);
        TextView p3Txt = (TextView) findViewById(R.id.p3);
        TextView p4Txt = (TextView) findViewById(R.id.p4);

        p1ScoreTxt = (TextView) findViewById(R.id.p1Score);
        p2ScoreTxt = (TextView) findViewById(R.id.p2Score);
        p3ScoreTxt = (TextView) findViewById(R.id.p3Score);
        p4ScoreTxt = (TextView) findViewById(R.id.p4Score);

        p1Txt.setText(players.get(0).getName());
        p2Txt.setText(players.get(1).getName());
        p3Txt.setText(players.get(2).getName());
        p4Txt.setText(players.get(3).getName());

        p1Txt.setOnClickListener(callListener);
        p2Txt.setOnClickListener(callListener);
        p3Txt.setOnClickListener(callListener);
        p4Txt.setOnClickListener(callListener);
//        p1ScoreTxt.setOnClickListener(callListener);
//        p2ScoreTxt.setOnClickListener(callListener);
//        p3ScoreTxt.setOnClickListener(callListener);
//        p4ScoreTxt.setOnClickListener(callListener);

        setPlayersScore();
    }

    /**
     * プレイヤーのスコアをセット。
     */
    private void setPlayersScore() {
        p1ScoreTxt.setText(String.valueOf(playersPoint.get(players.get(0).getId())));
        p2ScoreTxt.setText(String.valueOf(playersPoint.get(players.get(1).getId())));
        p3ScoreTxt.setText(String.valueOf(playersPoint.get(players.get(2).getId())));
        p4ScoreTxt.setText(String.valueOf(playersPoint.get(players.get(3).getId())));
    }

    /**
     * 流局時のスコアを計算して各プレイヤーのポイントに反映させる。
     *
     * @param tenpaiPlayers 聴牌者群
     */
    private void calcRyukyokuPoint(ArrayList<Player> tenpaiPlayers) {
        int i = 0;
        int tenpaiGetPoint = 0;
        int notenLosePoint = 0;

        if (tenpaiPlayers.size() != 0) {
            switch (tenpaiPlayers.size()) {
                case Consts.TENPAI_ONE:
                    tenpaiGetPoint = Consts.SANZE;
                    notenLosePoint = Consts.SEN;
                    break;
                case Consts.TENPAI_TWO:
                    tenpaiGetPoint = notenLosePoint = Consts.ITIGO;
                    break;
                case Consts.TENPAI_THREE:
                    tenpaiGetPoint = Consts.SEN;
                    notenLosePoint = Consts.SANZE;
                    break;
                default:
                    break;
            }

            for (Player p : players) {
                if (p.getId() == tenpaiPlayers.get(i).getId()) {
                    playersPoint.put(p.getId(), playersPoint.get(p.getId()) + tenpaiGetPoint);
                    if (i < (tenpaiPlayers.size() - 1)) {
                        i++;
                    }
                } else {
                    playersPoint.put(p.getId(), playersPoint.get(p.getId()) - notenLosePoint);
                }
            }
        }
    }

    /**
     * プレイヤー名を押下した際のListener。
     */
    private View.OnClickListener callListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String playerName = ((TextView) v).getText().toString();
            Player buf = null;

            // 立直宣言者を取得
            for (Player p : players) {
                if (p.getName().equals(playerName)) {
                    buf = p;
                }
            }
            final Player callPlayer = buf;

            // 立直済みかどうかの判定
            if (callPlayer != null && isCallPlayers.get(callPlayer.getId()).equals(Boolean.TRUE)) {
                UiUtil.showToast(ScoreActivity.this, getResources().getString(R.string.already_call_message));
                return;
            }

            UiUtil.showDialog(ScoreActivity.this, getResources().getString(R.string.call_message),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AudioUtil.play(mp, getApplicationContext(),
                                    Math.random() < 0.5 ? Consts.CALL_VOICE_1_URL : Consts.CALL_VOICE_2_URL,
                                    callVoiceListener(callPlayer));
                        }
                    });
        }
    };

    /**
     * 立直ボイスが流れる際のリスナー。
     *
     * @param callPlayer 立直者
     * @return リスナー
     */
    private MediaPlayer.OnPreparedListener callVoiceListener(final Player callPlayer) {
        return new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mp.isPlaying()) {
                    mp.stop();
                }
                mp.start();

                // 立直宣言者の点数から1000点引く
                if (callPlayer != null) {
                    playersPoint.put(callPlayer.getId(), playersPoint.get(callPlayer.getId()) - Consts.SEN);
                    isCallPlayers.put(callPlayer.getId(), Boolean.TRUE);
                    appController.setNumOfDepositBar(appController.getNumOfDepositBar() + 1);
                }
                setPlayersScore();
            }
        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        } else {
            UiUtil.showDialog(ScoreActivity.this, getResources().getString(R.string.game_seta_message),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            appController.setGameCnt(1);
                            appController.setPlayersPointList(new ArrayList<HashMap<Integer, Integer>>());
                            appController.setNumOfDepositBar(0);
                            finish();
                        }
                    });
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.game_set) {
            UiUtil.showDialog(ScoreActivity.this, getResources().getString(R.string.half_game_set_message),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            appController.addPlayersPoint(playersPoint);
                            startActivity(new Intent(ScoreActivity.this, ResultActivity.class));
                            finish();
                        }
                    });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
