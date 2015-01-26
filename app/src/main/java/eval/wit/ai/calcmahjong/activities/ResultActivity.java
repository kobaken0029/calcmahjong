package eval.wit.ai.calcmahjong.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import eval.wit.ai.calcmahjong.R;
import eval.wit.ai.calcmahjong.models.clients.AppController;

public class ResultActivity extends ActionBarActivity {
    private Button rePlayBtn;
    private Button finishBtn;

    private int score1;
    private int score2;
    private int score3;
    private int score4;

    private int gameCnt;

    private AppController appController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        appController = (AppController) getApplication();

        rePlayBtn = (Button) findViewById(R.id.rePlay);
        rePlayBtn.setOnClickListener(rePlayListener);

        finishBtn = (Button) findViewById(R.id.finish);
        finishBtn.setOnClickListener(finishListener);

        gameCnt = ((AppController) getApplication()).getGameCnt();
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

        }
    };

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
