package eval.wit.ai.calcmahjong.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eval.wit.ai.calcmahjong.R;
import eval.wit.ai.calcmahjong.models.clients.AppController;
import eval.wit.ai.calcmahjong.models.entities.Player;
import eval.wit.ai.calcmahjong.resources.ConstsManager;
import eval.wit.ai.calcmahjong.utilities.UiUtil;

public class PlayerListActivity extends ActionBarActivity {
    private ListView playerList;
//    private Player player;

    private List<Player> players = new ArrayList<>();
    private CustomCheckAdapter checkAdapter;

    private AppController appController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);
        appController = (AppController) getApplication();


        // mock
        Player p = new Player(1, "A君", "よろしく");
        Player p2 = new Player(2, "B君", "よろしく");
        Player p3 = new Player(3, "C君", "");
        Player p4 = new Player(4, "D君", "やっはろ～");
        Player p5 = new Player(4, "E君", "やっはろ～");
        Player p6 = new Player(4, "F君", "やっはろ～");
        Player p7 = new Player(4, "G君", "やっはろ～");

        players.add(p);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        players.add(p5);
        players.add(p6);
        players.add(p7);
//        players = appController.getPlayers();
        checkAdapter = new CustomCheckAdapter();

        playerList = (ListView) findViewById(R.id.player_list);
//        playerList.setOnItemClickListener(onItemClickListener);
        playerList.setAdapter(checkAdapter);
    }

    /**
     * リストのアイテムを押下した際のリスナー。
     */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView list = (ListView) parent;
            Intent intent = new Intent(PlayerListActivity.this, PlayerRecordActivity.class);
            intent.putExtra("player", (Player) list.getItemAtPosition(position));
            startActivity(intent);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        } else {
            ArrayList<Player> playToPlayers = new ArrayList<>();
            for (Player p : players) {
                if (p.isPlay()) {
                    playToPlayers.add(p);
                }
            }

            if (playToPlayers.size() < ConstsManager.getNumOfPlayer()) {
                UiUtil.showDialog(PlayerListActivity.this, getResources().getString(R.string.number_of_player_error), null);
            } else {
                appController.setPlayers(playToPlayers);
                finish();
            }
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player_list, menu);
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



    /**
     * 設定リストのAdapterです。
     */
    private class CustomCheckAdapter extends BaseAdapter {
        int cnt = 0;

        @Override
        public int getCount() {
            return players.size();
        }

        @Override
        public Object getItem(int position) {
            return players.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_check, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView
                        .findViewById(R.id.is_alarm);
                holder.checkBox = (CheckBox) convertView
                        .findViewById(R.id.check_alarm);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Player p = (Player) getItem(position);
            if (p != null) {
                if (cnt < 4) {
                    p.setPlay(true);
                    cnt++;
                }
                holder.textView.setText(p.getName());
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PlayerListActivity.this, PlayerRecordActivity.class);
                        intent.putExtra("player", p);
                        startActivity(intent);
                    }
                });
                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        p.setPlay(!p.isPlay());
                    }
                });
                holder.checkBox.setChecked(p.isPlay());
            }

            return convertView;
        }

        /**
         * ホルダクラス。
         */
        class ViewHolder {
            TextView textView;
            CheckBox checkBox;
        }
    }
}
