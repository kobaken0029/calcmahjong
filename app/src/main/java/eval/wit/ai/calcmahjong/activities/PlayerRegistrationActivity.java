package eval.wit.ai.calcmahjong.activities;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import eval.wit.ai.calcmahjong.R;
import eval.wit.ai.calcmahjong.models.clients.AppController;
import eval.wit.ai.calcmahjong.models.db.DatabaseAdapter;
import eval.wit.ai.calcmahjong.utilities.UiUtil;

public class PlayerRegistrationActivity extends ActionBarActivity {
    private EditText nameTxt;
    private EditText messageTxt;

    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_registration);
        databaseAdapter = ((AppController) getApplication()).getDbAdapter();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_player_registration);
        toolbar.setTitle(getResources().getString(R.string.title_activity_player_registration));
        toolbar.setBackgroundColor(getResources().getColor(R.color.action_bar));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        nameTxt = (EditText) findViewById(R.id.nameTxt);
        messageTxt = (EditText) findViewById(R.id.messageTxt);
        findViewById(R.id.registrationBtn).setOnClickListener(registrationListener);

        // 広告バナーを表示
        ((AdView)this.findViewById(R.id.adView_player_registration)).loadAd(new AdRequest.Builder()
                .addTestDevice("21499EE04196C2E0E48CB407366D501F")
                .build());
    }

    /**
     * 登録ボタンを押下した際のリスナー。
     */
    private View.OnClickListener registrationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // 名前有無判定
            if (nameTxt.getText().toString().equals("")) {
                UiUtil.showToast(PlayerRegistrationActivity.this,
                        getResources().getString(R.string.name_nothing_message));
                return;
            }

            // 入力文字数制限
            if (nameTxt.getText().toString().length() > 3) {
                UiUtil.showToast(PlayerRegistrationActivity.this,
                        getResources().getString(R.string.number_of_name_over_message));
                return;
            }

            try {
                databaseAdapter.open();

                // 名前の重複チェック
                if (databaseAdapter.isDuplicationPlayer(nameTxt.getText().toString())) {
                    UiUtil.showToast(PlayerRegistrationActivity.this,
                            getResources().getString(R.string.name_duplication_message));
                    databaseAdapter.close();
                    return;
                }

                // プレイヤー登録
                databaseAdapter.savePlayer(nameTxt.getText().toString(), messageTxt.getText().toString());
                databaseAdapter.close();

                databaseAdapter.open();
                Cursor c = databaseAdapter.getPlayerByName(nameTxt.getText().toString());
                if (c.moveToFirst()) {
                    // 成績登録
                    databaseAdapter.saveRecord(c.getInt(c.getColumnIndex(DatabaseAdapter.COL_ID)));
                }
                databaseAdapter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            finish();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player_registration, menu);
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
