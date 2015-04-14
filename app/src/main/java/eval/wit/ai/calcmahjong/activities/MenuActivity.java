package eval.wit.ai.calcmahjong.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import eval.wit.ai.calcmahjong.R;
import eval.wit.ai.calcmahjong.models.adapter.SimpleDrawerAdapter;
import eval.wit.ai.calcmahjong.models.clients.AppController;
import eval.wit.ai.calcmahjong.models.entities.Player;
import eval.wit.ai.calcmahjong.resources.ConstsManager;
import eval.wit.ai.calcmahjong.utilities.UiUtil;


public class MenuActivity extends ActionBarActivity {
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final AppController appController = (AppController) getApplication();

        // ツールバーを設定する
        setUpToolBar();

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((AppController) getApplication()).getPlayers().size() != ConstsManager.getNumOfPlayer()) {
                    UiUtil.showDialog(MenuActivity.this, null, getResources().getString(R.string.number_of_player_error), null);
                    return;
                }

                String uma = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .getString("ranking_uma_list", "ゴットー");
                String msg = "配給原点: " + ConstsManager.getFirstScore(getApplicationContext()) + "\n"
                        + "順位ウマ: " + uma + "\n"
                        + "参加者:" + "\n";

                int i = 0;
                for (Player p : appController.getPlayers()) {
                    msg += p.getName() + (i < 3 ? "\n" : "");
                    i++;
                }

                UiUtil.showDialog(MenuActivity.this,
                        getResources().getString(R.string.game_information_title),
                        msg, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean[] isParent = appController.getIsParent();
                                isParent[0] = true;
                                appController.setIsParent(isParent);
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


    /******************************************************************************************/
    /******************************************************************************************/
    /************************************Adapter***********************************************/
    /******************************************************************************************/
    /******************************************************************************************/

    /**
     * Drawerに表示する各アイテムのアダプタークラス。
     */
//    class SimpleDrawerAdapter extends RecyclerView.Adapter<ViewHolder> {
//
//        // MainActivityから渡されるデータ
//        private String[] mDrawerMenuArr;
//
//        public SimpleDrawerAdapter(String[] arrayList) {
//            mDrawerMenuArr = arrayList;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//            // レイアウトはsimple_list_item_1を利用
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
//
//            // アイテム選択可能にする
//            itemView.setClickable(true);
//
//            // アイテム選択時の Ripple Drawable を有効にする
//            // Android 4 系端末で確認すると、Ripple効果は付かないが、選択色のみ適用される
//            TypedValue outValue = new TypedValue();
//            parent.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
//            itemView.setBackgroundResource(outValue.resourceId);
//
//            return new ViewHolder(itemView, viewType, getApplicationContext());
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//
//            // 各アイテムのViewに、データをバインドする
//            String menu = mDrawerMenuArr[position];
//            holder.mTextView.setText(menu);
//            holder.mTextView.setOnClickListener(listener(menu));
//        }
//
//        @Override
//        public int getItemCount() {
//            return mDrawerMenuArr.length;
//        }
//
//
//        View.OnClickListener listener(final String menu) {
//            return new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(MenuActivity.this, WikipediaWebViewActivity.class);
//                    intent.putExtra("menu", menu);
//                    startActivity(intent);
//                }
//            };
//        }
//    }
//
//
//    /**
//     * Drawerのホルダークラス。
//     */
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView mTextView;
//
//        public ViewHolder(View itemView, int viewType, Context c) {
//            super(itemView);
//
//            //各アイテムのViewを取得
//            mTextView = (TextView) itemView.findViewById(android.R.id.text1);
//            mTextView.setTextColor(c.getResources().getColor(R.color.black_semi_transparent));
//        }
//    }
}
