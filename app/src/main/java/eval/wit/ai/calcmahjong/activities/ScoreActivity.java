package eval.wit.ai.calcmahjong.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.test.UiThreadTest;
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
import eval.wit.ai.calcmahjong.utilities.UiUtil;

public class ScoreActivity extends ActionBarActivity {
    private TextView p1Txt;
    private TextView p2Txt;
    private TextView p3Txt;
    private TextView p4Txt;
    private TextView p1ScoreTxt;
    private TextView p2ScoreTxt;
    private TextView p3ScoreTxt;
    private TextView p4ScoreTxt;
    private Button calcBtn;

    private ArrayList<Player> players;
    private HashMap<Integer, Integer> playersPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        init();

        calcBtn = (Button) findViewById(R.id.calc);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Consts.REQUEST_CODE :
                if (resultCode == RESULT_OK) {
                    int point = data.getIntExtra("point", 0);
                    Player winner = (Player) data.getSerializableExtra("winner");
                    Player loser = (Player) data.getSerializableExtra("loser");

                    String msg = winner.getName() + "←" + (loser == null ? "他家" : loser.getName()) + " " + point;
                    UiUtil.showToast(ScoreActivity.this, msg);

                    boolean flg = loser == null;
                    ArrayList<Player> ps = new ArrayList<>();
                    for (Player p : players) {
                        // 和了者の持ち点に得点を追加
                        if (winner.getId() == p.getId()) {
                            playersPoint.put(winner.getId(), playersPoint.get(winner.getId()) + point);
                        } else {
                            // 和了者以外をリストに追加
                            ps.add(p);
                        }

                        // 放銃者の持ち点から点数を引く
                        if (!flg) {
                            if (loser.getId() == p.getId()) {
                                playersPoint.put(loser.getId(), playersPoint.get(loser.getId()) - point);
                            }
                        }
                    }

                    // 自摸和了の場合
                    if (flg) {
                        for (Player p : ps) {
                            playersPoint.put(p.getId(), playersPoint.get(p.getId()) - point);
                        }
                    }

                    setPlayersScore();
                }
                break;
            default :
                break;
        }

    }

    /**
     * 画面に表示するデータを初期化。
     */
    private void init() {
        playersPoint = new HashMap<>();
        players = ((AppController) getApplication()).getPlayers();
        Integer p1Score = 25000;
        Integer p2Score = 25000;
        Integer p3Score = 25000;
        Integer p4Score = 25000;

        playersPoint.put(players.get(0).getId(), p1Score);
        playersPoint.put(players.get(1).getId(), p2Score);
        playersPoint.put(players.get(2).getId(), p3Score);
        playersPoint.put(players.get(3).getId(), p4Score);

        p1Txt = (TextView) findViewById(R.id.p1);
        p2Txt = (TextView) findViewById(R.id.p2);
        p3Txt = (TextView) findViewById(R.id.p3);
        p4Txt = (TextView) findViewById(R.id.p4);

        p1ScoreTxt = (TextView) findViewById(R.id.p1Score);
        p2ScoreTxt = (TextView) findViewById(R.id.p2Score);
        p3ScoreTxt = (TextView) findViewById(R.id.p3Score);
        p4ScoreTxt = (TextView) findViewById(R.id.p4Score);

        p1Txt.setText(players.get(0).getName());
        p2Txt.setText(players.get(1).getName());
        p3Txt.setText(players.get(2).getName());
        p4Txt.setText(players.get(3).getName());

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        } else {
            UiUtil.showDialog(ScoreActivity.this, "試合を終了しますか？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
