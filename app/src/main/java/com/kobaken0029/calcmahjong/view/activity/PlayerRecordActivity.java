package com.kobaken0029.calcmahjong.view.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.kobaken0029.calcmahjong.R;
import com.kobaken0029.calcmahjong.CalcMahjong;
import com.kobaken0029.calcmahjong.model.db.DatabaseAdapter;
import com.kobaken0029.calcmahjong.model.entity.Player;
import com.kobaken0029.calcmahjong.model.entity.Record;
import com.kobaken0029.calcmahjong.resource.Consts;

public class PlayerRecordActivity extends AppCompatActivity {
    private TextView nameTxetView;
    private TextView messageTxetView;
    private TextView numOfPlayTextView;
    private TextView scoreTextView;
    private TextView numOfWinTextView;
    private TextView numOfLoseTextView;
    private TextView topRateTextView;

    private DatabaseAdapter adapter;

    private CalcMahjong calcMahjong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_record);
        calcMahjong = (CalcMahjong) getApplication();
        adapter = calcMahjong.getDbAdapter();

        Player player = (Player) getIntent().getSerializableExtra("player");
        Record record = getRecord(player.getId());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_player_record);
        toolbar.setTitle(getResources().getString(R.string.title_activity_player_record));
        toolbar.setBackgroundColor(getResources().getColor(R.color.action_bar));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        nameTxetView = (TextView) findViewById(R.id.name);
        nameTxetView.setText(player.getName());
        messageTxetView = (TextView) findViewById(R.id.message);
        messageTxetView.setText(player.getMessage());

        numOfPlayTextView = (TextView) findViewById(R.id.num_of_play);
        numOfPlayTextView.setText(String.valueOf(record.getTotalPlay()));

        scoreTextView = (TextView) findViewById(R.id.score);
        scoreTextView.setText(String.valueOf(record.getTotalScore()));

        ((TextView) findViewById(R.id.ave_ranking)).setText(String.valueOf(calcAverageRanking(record)));

        numOfWinTextView = (TextView) findViewById(R.id.win);
        numOfWinTextView.setText(String.valueOf(record.getWinning()));

        numOfLoseTextView = (TextView) findViewById(R.id.lose);
        numOfLoseTextView.setText(String.valueOf(record.getDiscarding()));

        topRateTextView = (TextView) findViewById(R.id.top_rate);
        topRateTextView.setText(String.valueOf(calcTopRate(record)) + "%");

        // 広告バナーを表示
        ((AdView)this.findViewById(R.id.adView_player_record)).loadAd(new AdRequest.Builder()
                .addTestDevice("21499EE04196C2E0E48CB407366D501F")
                .build());
    }

    /**
     * 成績を取得。
     *
     * @param playerId プレイヤーID
     * @return 成績
     */
    private Record getRecord(int playerId) {
        Record record = new Record();

        adapter.open();
        Cursor c = adapter.getPlayerRecord(playerId);
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
                    c.getInt(c.getColumnIndex(DatabaseAdapter.COL_DISCARDING))
            );
        }
        adapter.close();

        return record;
    }

    /**
     * 平均順位を計算。
     *
     * @param record 成績
     * @return 平均順位
     */
    private double calcAverageRanking(Record record) {
        double averageRanking = 0.0;

        if (record.getTotalPlay() != 0) {
            averageRanking = (double) (record.getTop()
                    + record.getSecond() * Consts.SECOND
                    + record.getThird() * Consts.THIRD
                    + record.getLast() * Consts.LAST) / record.getTotalPlay();
        }

        return Math.round(averageRanking * 10) / 10.0;
    }

    /**
     * トップ率を計算。
     *
     * @param record 成績
     * @return トップ率
     */
    private double calcTopRate(Record record) {
        double rate = 0.0;

        if (record.getTotalPlay() != 0) {
            rate = (((double) record.getTop()) / record.getTotalPlay()) * 100.0;
        }

        return Math.round(rate * 10) / 10.0;
    }
}
