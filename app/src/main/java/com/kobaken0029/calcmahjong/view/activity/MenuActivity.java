package com.kobaken0029.calcmahjong.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.kobaken0029.calcmahjong.R;
import com.kobaken0029.calcmahjong.adapter.SimpleDrawerAdapter;
import com.kobaken0029.calcmahjong.CalcMahjong;
import com.kobaken0029.calcmahjong.model.entity.Player;
import com.kobaken0029.calcmahjong.resource.ConstsManager;
import com.kobaken0029.calcmahjong.util.UiUtil;

public class MenuActivity extends AppCompatActivity {
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final CalcMahjong calcMahjong = (CalcMahjong) getApplication();

        // ツールバーを設定する
        setUpToolBar();

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CalcMahjong) getApplication()).getPlayers().size() != ConstsManager.getNumOfPlayer()) {
                    UiUtil.showDialog(MenuActivity.this, null, getResources().getString(R.string.number_of_player_error), null);
                    return;
                }

                String uma = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .getString("ranking_uma_list", "ゴットー");
                String msg = "配給原点: " + ConstsManager.getFirstScore(getApplicationContext()) + "\n"
                        + "順位ウマ: " + uma + "\n"
                        + "参加者:" + "\n";

                int i = 0;
                for (Player p : calcMahjong.getPlayers()) {
                    msg += p.getName() + (i < 3 ? "\n" : "");
                    i++;
                }

                UiUtil.showDialog(MenuActivity.this,
                        getResources().getString(R.string.game_information_title),
                        msg, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean[] isParent = calcMahjong.getIsParent();
                                isParent[0] = true;
                                calcMahjong.setIsParent(isParent);
                                startActivity(new Intent(MenuActivity.this, SeatingListDialogActivity.class));
                            }
                        });
            }
        });

        findViewById(R.id.list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, PlayerListActivity.class));
            }
        });

        // 広告バナーを表示
        ((AdView)this.findViewById(R.id.adView_menu)).loadAd(new AdRequest.Builder()
                .addTestDevice("21499EE04196C2E0E48CB407366D501F")
                .build());
    }

    /**
     * ツールバーを設定。
     */
    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        toolbar.setTitle(R.string.menu);
        toolbar.setBackgroundColor(getResources().getColor(R.color.action_bar));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        // UpNavigationアイコン(アイコン横の<の部分)を有効に
        // NavigationDrawerではR.drawable.drawerで上書き
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // UpNavigationを有効に
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.menu, R.string.menu);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(drawerToggle);

        setRecyclerView();
    }

    /**
     * Drawer内のRecyclerViewを設定。
     */
    private void setRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.drawer_view);

        // RecyclerView内のItemサイズが固定の場合に設定すると、パフォーマンス最適化
        mRecyclerView.setHasFixedSize(true);

        // レイアウトの選択
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // XMLを読込んで表示する
        String[] drawerMenuArr = getResources().getStringArray(R.array.drawer_list_arr);
        SimpleDrawerAdapter mAdapter = new SimpleDrawerAdapter(drawerMenuArr, MenuActivity.this);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.registration:
                startActivity(new Intent(MenuActivity.this, PlayerRegistrationActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(MenuActivity.this, SettingsActivity.class));
                break;
        }

        // ActionBarDrawerToggleにandroid.id.home(up ナビゲーション)を渡す。
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
