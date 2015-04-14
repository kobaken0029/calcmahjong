package eval.wit.ai.calcmahjong.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.HashMap;

import eval.wit.ai.calcmahjong.R;
import eval.wit.ai.calcmahjong.models.adapter.SimpleDrawerAdapter;
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
    private TextView roundTextView;
    private TextView numOfRoundTextView;
    private TextView numOfDepositBarTextView;
    private TextView numOfHonbaTextView;
    private TextView topPlayerNameTextView;
    private TextView secondPlayerNameTextView;
    private TextView thirdPlayerNameTextView;
    private TextView lastPlayerNameTextView;
    private TextView gapTopBetweenSecondTextView;
    private TextView gapTopBetweenThirdTextView;
    private TextView gapTopBetweenLastTextView;

    private ImageView p1CallBarImageView;
    private ImageView p2CallBarImageView;
    private ImageView p3CallBarImageView;
    private ImageView p4CallBarImageView;

    private FloatingActionsMenu floatingActionsMenu;
    private ActionBarDrawerToggle drawerToggle;

    private ArrayList<Player> players;
    private HashMap<Integer, Integer> playersPoint;
    private HashMap<Integer, Boolean> isCallPlayers;

    private AppController appController;
    private MediaPlayer mp;
    private AppController.Round round;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        init();

        // インタースティシャルの設定
        final InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(Consts.AD_UNIT_ID);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                setViewOfPlayerData();
                setGapPoint();
            }
        });
        interstitialAd.loadAd(new AdRequest.Builder()
                .addTestDevice("21499EE04196C2E0E48CB407366D501F")
                .build());


        findViewById(R.id.game_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtil.showDialog(ScoreActivity.this, null, getResources().getString(R.string.half_game_set_message),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                resetGameInfoToResultActivity();
                            }
                        });
                floatingActionsMenu.collapse();
            }
        });

        findViewById(R.id.ryukyoku).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ScoreActivity.this, RyukyokuActivity.class), Consts.REQUEST_CODE);
                floatingActionsMenu.collapse();
            }
        });

        findViewById(R.id.calc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, MahjongScoringActivity.class);
                startActivityForResult(intent, Consts.REQUEST_CODE);
                floatingActionsMenu.collapse();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mp = new MediaPlayer();

        // 半荘が終了したら結果発表画面へ飛ばす
        if (appController.isLastGame()) {
            switch (round) {
                case EAST:
                    appController.setIsParent(new boolean[]{true, false, false, false});
                    round = AppController.Round.SOUTH;
                    break;
                case SOUTH:
                    resetGameInfoToResultActivity();
                    break;
                case WEST:
                    break;
                case NORTH:
                    break;
            }
        }

        if (round != null && !round.toString().equals("")) {
            Log.d("ROUND", round.toString());

            roundTextView.setText(round.getWind());
            numOfRoundTextView.setText(String.valueOf(appController.getNumOfHand()));
            numOfHonbaTextView.setText(String.valueOf(appController.getNumOfhonba()));
            numOfDepositBarTextView.setText(String.valueOf(appController.getNumOfDepositBar()));
        }

        for (int i = 0; i < appController.getIsParent().length; i++) {
            String playerNameTextId = "p" + (i + 1);
            int playerNameId = getResources().getIdentifier(playerNameTextId, "id", getPackageName());

            // テキストカラーを親は黄色、子は白にする
            if (appController.getIsParent()[i]) {
                ((TextView) findViewById(playerNameId)).setTextColor(Color.YELLOW);
            } else {
                ((TextView) findViewById(playerNameId)).setTextColor(Color.WHITE);
            }
            Log.d("PARENT", String.valueOf(appController.getIsParent()[i]));
        }
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
                // 誰かが挙がった場合
                if (resultCode == RESULT_OK) {
                    int[] point = data.getIntArrayExtra("point");
                    Player winner = (Player) data.getSerializableExtra("winner");
                    Player loser = (Player) data.getSerializableExtra("loser");
                    Player parent = (Player) data.getSerializableExtra("parent");
                    boolean isTumo = loser == null;

                    String msg = (isTumo
                            ? winner.getName() + "自摸" + " " + point[1] + ":" + point[0]
                            : loser.getName() + "→" + winner.getName() + " " + point[0]);
                    UiUtil.showToast(ScoreActivity.this, msg);

                    // 和了者の得点
                    int getWinnerPoint = 0;
                    for (int p : point) {
                        getWinnerPoint += p;
                    }

                    // 供託棒分の加点
                    getWinnerPoint += appController.getNumOfDepositBar() * Consts.SEN;
                    appController.setNumOfDepositBar(0);

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

                            // 子が挙がった場合のために得点インデックスを調整
                            if (i == 3) {
                                i = 0;
                            }
                        }
                    }

                    // 全プレイヤーの立直状態を解除
                    clearAllPlayerCall();
                } else if (resultCode == Consts.RYUKYOKU_CODE) {
                    // 流局時の聴牌者人数に応じて得点を振り分け
                    calcRyukyokuPoint((ArrayList<Player>) data.getSerializableExtra("tenpai"));

                    // 全プレイヤーの立直状態を解除
                    clearAllPlayerCall();
                }

                // 計算処理後のスコアを画面に反映
                setPlayersScore();
                setGapPoint();
                break;
            default:
                break;
        }
    }

    /**
     * データを初期化。
     */
    private void init() {
        setUpToolBar();
        appController = (AppController) getApplication();
        players = appController.getPlayers();

        playersPoint = new HashMap<>();
        Integer firstScore = ConstsManager.getFirstScore(getApplicationContext());
        playersPoint.put(players.get(0).getId(), firstScore);
        playersPoint.put(players.get(1).getId(), firstScore);
        playersPoint.put(players.get(2).getId(), firstScore);
        playersPoint.put(players.get(3).getId(), firstScore);

        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

        p1ScoreTxt = (TextView) findViewById(R.id.p1Score);
        p2ScoreTxt = (TextView) findViewById(R.id.p2Score);
        p3ScoreTxt = (TextView) findViewById(R.id.p3Score);
        p4ScoreTxt = (TextView) findViewById(R.id.p4Score);
        setPlayersScore();

        p1ScoreTxt.setOnClickListener(playerScoreListener(players.get(0).getId()));
        p2ScoreTxt.setOnClickListener(playerScoreListener(players.get(1).getId()));
        p3ScoreTxt.setOnClickListener(playerScoreListener(players.get(2).getId()));
        p4ScoreTxt.setOnClickListener(playerScoreListener(players.get(3).getId()));

        p2ScoreTxt.setRotation(270);
        p3ScoreTxt.setRotation(180);
        p4ScoreTxt.setRotation(90);

        isCallPlayers = new HashMap<>();
        p1CallBarImageView = (ImageView) findViewById(R.id.p1_call_bar);
        p2CallBarImageView = (ImageView) findViewById(R.id.p2_call_bar);
        p3CallBarImageView = (ImageView) findViewById(R.id.p3_call_bar);
        p4CallBarImageView = (ImageView) findViewById(R.id.p4_call_bar);
        clearAllPlayerCall();

        round = AppController.Round.EAST;
        roundTextView = (TextView) findViewById(R.id.text_round);
        numOfRoundTextView = (TextView) (findViewById(R.id.num_of_round));
        numOfDepositBarTextView = (TextView) findViewById(R.id.num_of_deposit_bar);
        numOfHonbaTextView = (TextView) findViewById(R.id.num_of_honba);

        roundTextView.setText(round.getWind());
        numOfRoundTextView.setText(String.valueOf(appController.getNumOfHand()));
        numOfDepositBarTextView.setText(String.valueOf(appController.getNumOfDepositBar()));
        numOfHonbaTextView.setText(String.valueOf(appController.getNumOfhonba()));

        topPlayerNameTextView = (TextView) findViewById(R.id.top_player_name);
        secondPlayerNameTextView = (TextView) findViewById(R.id.second_player_name);
        thirdPlayerNameTextView = (TextView) findViewById(R.id.third_player_name);
        lastPlayerNameTextView = (TextView) findViewById(R.id.last_player_name);
        gapTopBetweenSecondTextView = (TextView) findViewById(R.id.gap_top_between_second);
        gapTopBetweenThirdTextView = (TextView) findViewById(R.id.gap_top_between_third);
        gapTopBetweenLastTextView = (TextView) findViewById(R.id.gap_top_between_last);
    }

    /**
     * プレイヤー情報をセット。
     */
    private void setViewOfPlayerData() {
        TextView p1Txt = (TextView) findViewById(R.id.p1);
        TextView p2Txt = (TextView) findViewById(R.id.p2);
        TextView p3Txt = (TextView) findViewById(R.id.p3);
        TextView p4Txt = (TextView) findViewById(R.id.p4);

        p2Txt.setRotation(270);
        p3Txt.setRotation(180);
        p4Txt.setRotation(90);

        p1Txt.setText(players.get(0).getName());
        p2Txt.setText(players.get(1).getName());
        p3Txt.setText(players.get(2).getName());
        p4Txt.setText(players.get(3).getName());

        p1Txt.setOnClickListener(callListener(p1CallBarImageView));
        p2Txt.setOnClickListener(callListener(p2CallBarImageView));
        p3Txt.setOnClickListener(callListener(p3CallBarImageView));
        p4Txt.setOnClickListener(callListener(p4CallBarImageView));
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
     * トップとの点差をセット。
     */
    private void setGapPoint() {
        // トップのプレイヤーを取得
        Player topPlayer = new Player();
        HashMap<Integer, Integer> rankingHashMap = appController.getRankingHashMap(playersPoint);
        for (Player p : players) {
            if (rankingHashMap.get(p.getId()) == Consts.TOP) {
                topPlayer = p;
                break;
            }
        }

        for (Player p : players) {
            String gapPoint = String.valueOf(playersPoint.get(p.getId()) - playersPoint.get(topPlayer.getId()));
            switch (rankingHashMap.get(p.getId())) {
                case Consts.TOP:
                    topPlayerNameTextView.setText(p.getName());
                    break;
                case Consts.SECOND:
                    secondPlayerNameTextView.setText(p.getName());
                    gapTopBetweenSecondTextView.setText(gapPoint);
                    break;
                case Consts.THIRD:
                    thirdPlayerNameTextView.setText(p.getName());
                    gapTopBetweenThirdTextView.setText(gapPoint);
                    break;
                case Consts.LAST:
                    lastPlayerNameTextView.setText(p.getName());
                    gapTopBetweenLastTextView.setText(gapPoint);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * プレイヤーの得点等の情報や場の状態をリセットして、結果画面へ遷移。
     */
    private void resetGameInfoToResultActivity() {
        appController.addPlayersPoint(playersPoint);
        appController.setNumOfDepositBar(0);
        appController.setNumOfhonba(0);
        appController.setIsParent(new boolean[]{true, false, false, false});
        startActivity(new Intent(ScoreActivity.this, ResultActivity.class));
        finish();
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
     * プレイヤー名を押下した際のリスナー。
     */
    private View.OnClickListener callListener(final ImageView callBarImageView) {
        return new View.OnClickListener() {
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

                UiUtil.showDialog(ScoreActivity.this, null, getResources().getString(R.string.call_message),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 立直宣言者の点数から1000点引く
                                if (callPlayer != null) {
                                    isCallPlayers.put(callPlayer.getId(), Boolean.TRUE);
                                    playersPoint.put(callPlayer.getId(), playersPoint.get(callPlayer.getId()) - Consts.SEN);
                                    appController.setNumOfDepositBar(appController.getNumOfDepositBar() + 1);
                                    callBarImageView.setVisibility(View.VISIBLE);
                                }
                                setPlayersScore();

                                AudioUtil.play(mp, getApplicationContext(),
                                        Math.random() < 0.5 ? Consts.CALL_VOICE_1_URL : Consts.CALL_VOICE_2_URL,
                                        null);
                            }
                        });
            }
        };
    }

    /**
     * 立直ボイスが流れる際のリスナー。
     *
     * @param callPlayer 立直者
     * @return リスナー
     */
//    private synchronized MediaPlayer.OnPreparedListener callVoiceListener(final Player callPlayer, final ImageView v) {
//        return new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                if (mp.isPlaying()) {
//                    mp.stop();
//                }
//                mp.start();
//
//                // 立直宣言者の点数から1000点引く
//                if (callPlayer != null) {
//                    isCallPlayers.put(callPlayer.getId(), Boolean.TRUE);
//                    playersPoint.put(callPlayer.getId(), playersPoint.get(callPlayer.getId()) - Consts.SEN);
//                    appController.setNumOfDepositBar(appController.getNumOfDepositBar() + 1);
//                    v.setVisibility(View.VISIBLE);
//                }
//                setPlayersScore();
//            }
//        };
//    }

    /**
     * スコアを押下した際のリスナー。
     *
     * @param id プレイヤーID
     */
    private View.OnClickListener playerScoreListener(final int id) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.fix_player_score_dialog, null);

                final TextView playerScoreText = (TextView) v;
                final EditText scoreEditText = (EditText) layout.findViewById(R.id.p_score);
                scoreEditText.setText(playerScoreText.getText().toString());

                new AlertDialog.Builder(ScoreActivity.this)
                        .setTitle("点数修正")
                        .setView(layout)
                        .setPositiveButton(getResources().getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (scoreEditText.getText() != null && !scoreEditText.getText().toString().equals("")) {
                                            playerScoreText.setText(scoreEditText.getText().toString());
                                            playersPoint.put(id, Integer.valueOf(scoreEditText.getText().toString()));
                                        }
                                    }
                                })
                        .setNegativeButton(getResources().getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                        .create().show();
            }
        };
    }

    /**
     * すべてのプレイヤーのリーチをリセットする。
     */
    private void clearAllPlayerCall() {
        isCallPlayers.put(players.get(0).getId(), Boolean.FALSE);
        isCallPlayers.put(players.get(1).getId(), Boolean.FALSE);
        isCallPlayers.put(players.get(2).getId(), Boolean.FALSE);
        isCallPlayers.put(players.get(3).getId(), Boolean.FALSE);

        p1CallBarImageView.setVisibility(View.INVISIBLE);
        p2CallBarImageView.setVisibility(View.INVISIBLE);
        p3CallBarImageView.setVisibility(View.INVISIBLE);
        p4CallBarImageView.setVisibility(View.INVISIBLE);
    }

    /**
     * ツールバーを設定。
     */
    private void setUpToolBar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
//        toolbar.setTitle(R.string.menu);
//        toolbar.setBackgroundColor(getResources().getColor(R.color.action_bar));
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//        setSupportActionBar(toolbar);
//
//        // UpNavigationアイコン(アイコン横の<の部分)を有効に
//        // NavigationDrawerではR.drawable.drawerで上書き
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        // UpNavigationを有効に
//        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.menu, R.string.menu);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(drawerToggle);

        setRecyclerView();
    }

    /**
     * Drawer内のRecyclerViewを設定。
     */
    private void setRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.drawer_view);

        // RecyclerView内のItemサイズが固定の場合に設定すると、パフォーマンス最適化
        mRecyclerView.setHasFixedSize(true);

        // レイアウトの選択
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // XMLを読込んで表示する
        String[] drawerMenuArr = getResources().getStringArray(R.array.drawer_list_arr);
        SimpleDrawerAdapter mAdapter = new SimpleDrawerAdapter(drawerMenuArr, ScoreActivity.this);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        } else {
            UiUtil.showDialog(ScoreActivity.this, null, getResources().getString(R.string.game_set_message),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UiUtil.showDialog(ScoreActivity.this, null, getResources().getString(R.string.really_half_game_set_message),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            appController.setGameCnt(1);
                                            appController.setPlayersPointList(new ArrayList<HashMap<Integer, Integer>>());
                                            appController.setNumOfDepositBar(0);
                                            appController.setNumOfhonba(0);
                                            appController.setIsParent(new boolean[]{true, false, false, false});
                                            finish();
                                        }
                                    });
                        }
                    });
            floatingActionsMenu.collapse();
            return false;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // ActionBarDrawerToggleにandroid.id.home(up ナビゲーション)を渡す。
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
