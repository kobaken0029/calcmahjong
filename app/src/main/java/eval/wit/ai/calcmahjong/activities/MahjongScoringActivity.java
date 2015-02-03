package eval.wit.ai.calcmahjong.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import eval.wit.ai.calcmahjong.R;
import eval.wit.ai.calcmahjong.models.clients.AppController;
import eval.wit.ai.calcmahjong.models.entities.Player;
import eval.wit.ai.calcmahjong.resources.Consts;
import eval.wit.ai.calcmahjong.resources.ConstsManager;
import eval.wit.ai.calcmahjong.utilities.UiUtil;

public class MahjongScoringActivity extends ActionBarActivity {
    private Spinner winnerSpinner;
    private Spinner loserSpinner;
    private Spinner numberOfHanSpinner;
    private Spinner numberOfFuSpinner;
    private Spinner parentSpinner;
    private EditText honbaTxt;
    private Button calcBtn;

    private ArrayList<Player> players;
    private Player parent;
    private Player winner;
    private Player loser;
    private int[] point;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahjong_scoring);

        winnerSpinner = (Spinner) findViewById(R.id.winnerSpinner);
        loserSpinner = (Spinner) findViewById(R.id.loserSpinner);
        numberOfHanSpinner = (Spinner) findViewById(R.id.hanSpinner);
        numberOfFuSpinner = (Spinner) findViewById(R.id.fuSpinner);
        parentSpinner = (Spinner) findViewById(R.id.parentSpinner);
        honbaTxt = (EditText) findViewById(R.id.honba);

        // スピナーに値をセット。
        setSpinner();

        calcBtn = (Button) findViewById(R.id.calc);
        calcBtn.setOnClickListener(calcListener);
    }

    /**
     * スピナーに値をセット。
     */
    public void setSpinner() {
        ArrayAdapter<String> winnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> loserAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);

        // 参加者を取得
        players = ((AppController) getApplication()).getPlayers();

        // 参加中のプレイヤーをセット
        for (Player p : players) {
            winnerAdapter.add(p.getName());
            winnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            loserAdapter.add(p.getName());
            loserAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        winnerSpinner.setAdapter(winnerAdapter);
        parentSpinner.setAdapter(winnerAdapter);

        // 他家を加えセット
        loserAdapter.add("他家(ツモアガリ)");
        loserSpinner.setAdapter(loserAdapter);

        // 飜数セット
        numberOfHanSpinner.setAdapter(ConstsManager.getHanAdapter(this));

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
            // 和了者、放銃者、親をセット
            for (Player p : players) {
                if (p.getName().equals(winnerSpinner.getSelectedItem().toString())) {
                    winner = p;
                }

                if (p.getName().equals(loserSpinner.getSelectedItem().toString())) {
                    loser = p;
                }

                if (p.getName().equals(parentSpinner.getSelectedItem().toString())) {
                    parent = p;
                }
            }

            // 自摸和了の場合
            if (loserSpinner.getSelectedItem().toString().equals("他家(ツモアガリ)")) {
                loser = null;
            }

            // 和了者と放銃者が同一人物の場合
            if (winner.equals(loser)) {
                UiUtil.showToast(MahjongScoringActivity.this, "和了者と放銃者が一致しています!");
                return;
            }

            // 20符ロン和了の場合
            if (loser != null && numberOfFuSpinner.getSelectedItem().toString().equals(Consts.NIZYU_FU)) {
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

            // 何本場かを取得
            int honba = 0;
            if (!honbaTxt.getText().toString().equals("")) {
                honba = Integer.parseInt(honbaTxt.getText().toString());
            }

            // 和了者が親かどうかの判定
            boolean isParent = parent.getId() == winner.getId();

            // 得点を取得
            point = ConstsManager.calcPoint(numberOfFuSpinner.getSelectedItem().toString(),
                                            numberOfHanSpinner.getSelectedItem().toString(),
                                            isParent,
                                            loser == null,
                                            honba);

            String msg = "アガリ:" + winner.getName() + "\n"
                    + "放銃者:" + (loser == null ? "自摸和了" : loser.getName()) + "\n"
                    + "飜数" + numberOfHanSpinner.getSelectedItem().toString() + "\n"
                    + "符数" + numberOfFuSpinner.getSelectedItem().toString() + "\n"
                    + honba + "本場" + "\n"
                    + "得点" + point[0];
            UiUtil.showDialog(MahjongScoringActivity.this, msg, listener);
        }
    };

    /**
     * ダイアログでPositiveなボタンを押下した時のリスナー。
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent();
            intent.putExtra("point", point);
            intent.putExtra("winner", winner);
            intent.putExtra("parent", parent);

            if (loser != null) {
                intent.putExtra("loser", loser);
            }

            setResult(RESULT_OK, intent);

            dialog.cancel();
            finish();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mahjong_scoring, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.ryukyoku) {
            new RyukyokuDialog().showDialog(MahjongScoringActivity.this, this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
