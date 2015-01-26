package eval.wit.ai.calcmahjong.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import eval.wit.ai.calcmahjong.R;
import eval.wit.ai.calcmahjong.models.clients.AppController;
import eval.wit.ai.calcmahjong.models.entities.Player;
import eval.wit.ai.calcmahjong.resources.ConstsManager;
import eval.wit.ai.calcmahjong.utilities.UiUtil;

public class MahjongScoringActivity extends ActionBarActivity {
    private Spinner winner;
    private Spinner loser;
    private Spinner NumberOfHan;
    private Spinner NumberOfFu;
    private Button calcBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahjong_scoring);

        winner = (Spinner) findViewById(R.id.winnerSpinner);
        loser = (Spinner) findViewById(R.id.loserSpinner);
        NumberOfHan = (Spinner) findViewById(R.id.hanSpinner);
        NumberOfFu = (Spinner) findViewById(R.id.fuSpinner);

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

        // 参加中のプレイヤーをセット
        for (Player p : ((AppController) getApplication()).getPlayers()) {
            winnerAdapter.add(p.getName());
            winnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            loserAdapter.add(p.getName());
            loserAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        winner.setAdapter(winnerAdapter);

        // 他家を加えセット
        loserAdapter.add("他家(ツモアガリ)");
        loser.setAdapter(loserAdapter);

        // 飜数セット
        NumberOfHan.setAdapter(ConstsManager.getHanAdapter(this));

        // 符数セット
        NumberOfFu.setAdapter(ConstsManager.getFuAdapter(this));
    }

    /**
     * 計算ボタン押下時のリスナー。
     */
    View.OnClickListener calcListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = "アガリ:" + winner.getSelectedItem().toString() + "\n"
                    + "放銃者:" + loser.getSelectedItem().toString() + "\n"
                    + "飜数" + NumberOfHan.getSelectedItem().toString() + "\n"
                    + "符数" + NumberOfFu.getSelectedItem().toString();
            UiUtil.showDialog(MahjongScoringActivity.this, msg, listener);
        }
    };

    /**
     * ダイアログでPositiveなボタンを押下した時のリスナー。
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            startActivity(new Intent(MahjongScoringActivity.this, ScoreActivity.class));
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
