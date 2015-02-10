package eval.wit.ai.calcmahjong.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import eval.wit.ai.calcmahjong.R;
import eval.wit.ai.calcmahjong.models.entities.Player;
import eval.wit.ai.calcmahjong.models.entities.Record;

public class PlayerRecordActivity extends ActionBarActivity {
    private TextView nameTxetView;
    private TextView messageTxetView;
    private TextView scoreTextView;
    private TextView numOfWinTextView;
    private TextView numOfLoseTextView;
    private TextView topRateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_record);

        Player player = (Player) getIntent().getSerializableExtra("player");
        Record record = new Record(1, player.getId(), 10, 3, 1, 0, 2, 0, 4, 2);

        nameTxetView = (TextView) findViewById(R.id.name);
        nameTxetView.setText(player.getName());
        messageTxetView = (TextView) findViewById(R.id.message);
        messageTxetView.setText(player.getMessage());

        scoreTextView = (TextView) findViewById(R.id.score);
        scoreTextView.setText(String.valueOf(record.getTotalScore()));

        numOfWinTextView = (TextView) findViewById(R.id.win);
        numOfWinTextView.setText(String.valueOf(record.getWinning()));

        numOfLoseTextView = (TextView) findViewById(R.id.lose);
        numOfLoseTextView.setText(String.valueOf(record.getDiscarding()));

        topRateTextView = (TextView) findViewById(R.id.top_rate);
        topRateTextView.setText(String.valueOf(calcTopRate(record)));
    }

    private double calcTopRate(Record record) {
        return 0.0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player_record, menu);
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
