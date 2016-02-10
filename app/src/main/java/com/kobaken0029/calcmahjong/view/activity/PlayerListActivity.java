package com.kobaken0029.calcmahjong.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import com.kobaken0029.calcmahjong.R;
import com.kobaken0029.calcmahjong.CalcMahjong;
import com.kobaken0029.calcmahjong.model.db.DatabaseAdapter;
import com.kobaken0029.calcmahjong.model.entity.Player;
import com.kobaken0029.calcmahjong.util.UiUtil;

public class PlayerListActivity extends AppCompatActivity {
    private static final int MENU_ITEM_ID_DELETE = 1;

    private List<Player> players = new ArrayList<>();
    private CustomCheckAdapter checkAdapter;

    private CalcMahjong calcMahjong;
    private DatabaseAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);
        calcMahjong = (CalcMahjong) getApplication();
        dbAdapter = calcMahjong.getDbAdapter();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_player_list);
        toolbar.setTitle(getResources().getString(R.string.title_activity_player_list));
        toolbar.setBackgroundColor(getResources().getColor(R.color.action_bar));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        checkAdapter = new CustomCheckAdapter();
        ListView playerList = (ListView) findViewById(R.id.player_list);
        playerList.setOnItemClickListener(onItemClickListener);
        playerList.setAdapter(checkAdapter);
        playerList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, MENU_ITEM_ID_DELETE, 0, R.string.delete);
            }
        });

        // 広告バナーを表示
        ((AdView) this.findViewById(R.id.adView_player_list)).loadAd(new AdRequest.Builder()
                .addTestDevice("21499EE04196C2E0E48CB407366D501F")
                .build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPlayers();
    }

    @Override
    protected void onPause() {
        super.onPause();

        dbAdapter.open();
        for (Player p : players) {
            dbAdapter.updatePlayer(p);
            Log.d("PLAYER", p.getName() + ":" + String.valueOf(p.isPlay()));
        }
        dbAdapter.close();
    }

    /**
     * プレイヤーの読み込みをします。
     */
    protected void loadPlayers() {
        players.clear();

        dbAdapter.open();
        Cursor c = dbAdapter.getAllPlayers();

        if (c.moveToFirst()) {
            do {
                Player player = new Player(
                        c.getInt(c.getColumnIndex(DatabaseAdapter.COL_ID)),
                        c.getString(c.getColumnIndex(DatabaseAdapter.COL_NAME)),
                        c.getString(c.getColumnIndex(DatabaseAdapter.COL_MESSAGE)),
                        c.getString(c.getColumnIndex(DatabaseAdapter.COL_IS_PLAY)).equals("1"));
                players.add(player);
            } while (c.moveToNext());
        }

        dbAdapter.close();
        checkAdapter.notifyDataSetChanged();
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
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Player deleteTargetPlayer = players.get(info.position);

        switch (item.getItemId()) {
            // プレイヤーを削除する
            case MENU_ITEM_ID_DELETE:
                UiUtil.showDialog(PlayerListActivity.this, null, getResources().getString(R.string.delete_message),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbAdapter.open();
                                if (dbAdapter.deletePlayer(deleteTargetPlayer.getId())
                                        && dbAdapter.deleteRecord(deleteTargetPlayer.getId())) {
                                    UiUtil.showToast(PlayerListActivity.this, deleteTargetPlayer.getName() + "さんを削除しました");
                                    loadPlayers();
                                }
                                dbAdapter.close();
                            }
                        });
                break;
            default:
                break;
        }
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.registration:
                startActivity(new Intent(PlayerListActivity.this, PlayerRegistrationActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(PlayerListActivity.this, SettingsActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 設定リストのAdapterです。
     */
    private class CustomCheckAdapter extends BaseAdapter {
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
                holder.textView.setText(p.getName());
                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        p.setPlay(!p.isPlay());

                        ArrayList<Player> playToPlayers = new ArrayList<>();
                        for (Player p : players) {
                            if (p.isPlay()) {
                                playToPlayers.add(p);
                            }
                        }
                        calcMahjong.setPlayers(playToPlayers);
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
