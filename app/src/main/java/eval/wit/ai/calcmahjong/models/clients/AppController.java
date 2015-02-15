package eval.wit.ai.calcmahjong.models.clients;

import android.app.Application;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eval.wit.ai.calcmahjong.models.db.DatabaseAdapter;
import eval.wit.ai.calcmahjong.models.entities.Player;

/**
 * Created by koba on 2015/01/25.
 */
public class AppController extends Application {
    private DatabaseAdapter dbAdapter;
    private ArrayList<Player> players;

    private int gameCnt;
    private List<HashMap<Integer, Integer>> playersPointList;
    private HashMap<Integer, Integer> winningHashMap;
    private HashMap<Integer, Integer> discardingHashMap;

    private int numOfDepositBar;

    @Override
    public void onCreate() {
        super.onCreate();
        dbAdapter = new DatabaseAdapter(getApplicationContext());
        gameCnt = 1;
        playersPointList = new ArrayList<>();
        winningHashMap = new HashMap<>();
        discardingHashMap = new HashMap<>();
        numOfDepositBar = 0;

        setIsPlayers();
    }

    public DatabaseAdapter getDbAdapter() {
        return dbAdapter;
    }

    /**
     * プレイ中のプレイヤーをセットします。
     */
    private void setIsPlayers() {
        ArrayList<Player> ps = new ArrayList<>();
        dbAdapter.open();
        Cursor c = dbAdapter.getIsPlayPlayer();
        if (c.moveToFirst()) {
            do {
                Player player = new Player(
                        c.getInt(c.getColumnIndex(DatabaseAdapter.COL_ID)),
                        c.getString(c.getColumnIndex(DatabaseAdapter.COL_NAME)),
                        c.getString(c.getColumnIndex(DatabaseAdapter.COL_MESSAGE)),
                        c.getString(c.getColumnIndex(DatabaseAdapter.COL_IS_PLAY)).equals("1"));
                ps.add(player);
            } while (c.moveToNext());
        }
        dbAdapter.close();
        setPlayers(ps);
    }

    /**
     * 現在の半荘数を返します。
     *
     * @return 半荘数
     */
    public int getGameCnt() {
        return gameCnt;
    }

    /**
     * 半荘数をセットします。
     *
     * @param gameCnt 半荘数
     */
    public void setGameCnt(int gameCnt) {
        this.gameCnt = gameCnt;
    }

    /**
     * プレイヤー群を取得。
     *
     * @return プレイヤー群
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * プレイヤー群をセット。
     *
     * @param players プレイヤー群
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = new ArrayList<>(players);
    }

    /**
     * プレイヤー群の夫々の得点群を取得します。
     *
     * @return プレイヤー群の夫々の得点群
     */
    public List<HashMap<Integer, Integer>> getPlayersPointList() {
        return playersPointList;
    }

    /**
     * プレイヤー群の夫々の得点群をセットします。
     *
     * @param playersPointList プレイヤー群の夫々の得点群
     */
    public void setPlayersPointList(List<HashMap<Integer, Integer>> playersPointList) {
        this.playersPointList = new ArrayList<>(playersPointList);
    }

    /**
     * プレイヤー群の夫々の得点をリストに追加します。
     *
     * @param playersPoint プレイヤー群の夫々の得点
     */
    public void addPlayersPoint(HashMap<Integer, Integer> playersPoint) {
        this.playersPointList.add(playersPoint);
    }

    /**
     * 供託棒の数を取得します。
     *
     * @return 供託棒数
     */
    public int getNumOfDepositBar() {
        return numOfDepositBar;
    }

    /**
     * 供託棒の数をセットします。
     *
     * @param numOfDepositBar 供託棒数
     */
    public void setNumOfDepositBar(int numOfDepositBar) {
        this.numOfDepositBar = numOfDepositBar;
    }

    /**
     * 試合中の和了数を取得します。
     *
     * @return プレイヤーの和了数
     */
    public HashMap<Integer, Integer> getWinningHashMap() {
        return winningHashMap;
    }

    /**
     * 試合中の和了数をセットします。
     *
     * @param winningHashMap プレイヤーの和了数
     */
    public void setWinningHashMap(HashMap<Integer, Integer> winningHashMap) {
        this.winningHashMap = winningHashMap;
    }

    /**
     * 試合中の放銃数を取得します。
     *
     * @return プレイヤーの放銃数
     */
    public HashMap<Integer, Integer> getDiscardingHashMap() {
        return discardingHashMap;
    }

    /**
     * 試合中の放銃数をセットします。
     *
     * @param discardingHashMap プレイヤーの放銃数
     */
    public void setDiscardingHashMap(HashMap<Integer, Integer> discardingHashMap) {
        this.discardingHashMap = discardingHashMap;
    }
}
