package eval.wit.ai.calcmahjong.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import eval.wit.ai.calcmahjong.R;
import eval.wit.ai.calcmahjong.models.clients.AppController;
import eval.wit.ai.calcmahjong.models.entities.Player;
import eval.wit.ai.calcmahjong.models.listview.TouchListView;
import eval.wit.ai.calcmahjong.resources.ConstsManager;

/**
 * Created by koba on 2015/02/07.
 */
public class SeatingListDialogActivity extends ListActivity {
    private AppController appController;

    private ArrayList<Player> players;
    private ArrayList<String> array = new ArrayList<>();
    private SeatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seating_list);

        TouchListView tlv = (TouchListView) getListView();
        appController = (AppController) getApplication();
        players = appController.getPlayers();

        HashMap<Integer, Integer> winnerHashMap = new HashMap<>();
        HashMap<Integer, Integer> discardingHashMap = new HashMap<>();

        for (Player p : players) {
            array.add(p.getName());

            winnerHashMap.put(p.getId(), 0);
            discardingHashMap.put(p.getId(), 0);
        }
        appController.setWinningHashMap(winnerHashMap);
        appController.setDiscardingHashMap(discardingHashMap);

        adapter = new SeatAdapter();
        setListAdapter(adapter);

        tlv.setDropListener(onDrop);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.activity_seating_list,
                (ViewGroup) findViewById(R.id.seating_plan_dialog_layout));

        // ルートビューを取得
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.seating_plan_dialog_layout);

        // 親Viewを取得してremoveVieを実行
        ViewGroup parent = (ViewGroup) viewGroup.getParent();
        if (parent != null) {
            parent.removeView(viewGroup);
        }


        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme))
                .setTitle(getResources().getString(R.string.seating_plan_title))
                .setView(layout)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<Player> ps = new ArrayList<>();

                        // プレイヤーの席順を決定する
                        int i = 0;
                        while (ps.size() != ConstsManager.getNumOfPlayer()) {
                            if (array.get(ps.size()).equals(players.get(i).getName())) {
                                ps.add(players.get(i));
                            }
                            i++;

                            if (i >= ConstsManager.getNumOfPlayer()) {
                                i = 0;
                            }
                        }

                        // 席順を保ち、プレイヤーのリストをセット
                        appController.setPlayers(ps);
                        startActivity(new Intent(SeatingListDialogActivity.this, ScoreActivity.class));
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            dialog.cancel();
                            finish();
                        }
                        return false;
                    }
                }).create().show();
    }

    private TouchListView.DropListener onDrop = new TouchListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            String item = adapter.getItem(from);

            adapter.remove(item);
            adapter.insert(item, to);
        }
    };

    class SeatAdapter extends ArrayAdapter<String> {
        SeatAdapter() {
            super(SeatingListDialogActivity.this, R.layout.row_seating, array);
        }

        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.row_seating, parent, false);
            }

            TextView label = (TextView) row.findViewById(R.id.player_seating);
            label.setText(array.get(position));
            TextView direction = (TextView) row.findViewById(R.id.direction);
            switch (position) {
                case 0:
                    direction.setText(getResources().getString(R.string.east));
                    break;
                case 1:
                    direction.setText(getResources().getString(R.string.south));
                    break;
                case 2:
                    direction.setText(getResources().getString(R.string.west));
                    break;
                case 3:
                    direction.setText(getResources().getString(R.string.north));
                    break;
                default:
                    direction.setText("");
                    break;
            }

            return row;
        }
    }
}
