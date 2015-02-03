package eval.wit.ai.calcmahjong.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import eval.wit.ai.calcmahjong.R;
import eval.wit.ai.calcmahjong.models.entities.Player;

public class PlayerRecordActivity extends ActionBarActivity {
    private TextView nameTxt;
    private TextView messageTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_record);

        Player player = (Player) getIntent().getSerializableExtra("player");

        nameTxt = (TextView) findViewById(R.id.name);
        nameTxt.setText(player.getName());
        messageTxt = (TextView) findViewById(R.id.message);
        messageTxt.setText(player.getMessage());
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
