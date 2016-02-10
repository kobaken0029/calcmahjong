package com.kobaken0029.calcmahjong;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kobaken0029.calcmahjong.model.db.DatabaseAdapter;
import com.kobaken0029.calcmahjong.model.entity.Player;
import com.kobaken0029.calcmahjong.resource.ConstsManager;

public class CalcMahjong extends Application {
    private DatabaseAdapter dbAdapter;

    /** 試合参加プレイヤー  */
    private ArrayList<Player> players;

    /** 試合数カウント用 */
    private int gameCnt;

    /** 試合中の各プレイヤーの得点リスト */
    private List<HashMap<Integer, Integer>> playersPointList;

    /** 和了者<プレイヤーID, 和了数> */
    private HashMap<Integer, Integer> winningHashMap;

    /** 放銃者<プレイヤーID, 放銃数> */
    private HashMap<Integer, Integer> discardingHashMap;

    /** 供託棒数 */
    private int numOfDepositBar;

    /** 連荘数 */
    private int numOfhonba;

    /** 誰が親か */
    private boolean[] isParent = new boolean[ConstsManager.getNumOfPlayer()];

    /** 場 */
    public enum Round {
        EAST("東"),
        SOUTH("南"),
        WEST("西"),
        NORTH("北");

        private String wind;
        private Round(String wind) {
            setWind(wind);
        }
        public String getWind() {
            return wind;
        }
        private void setWind(String wind) {
            this.wind = wind;
        }
    }

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
     * プレイヤーと順位が紐付いたHashMapを取得する。
     *
     * @param playersPoint プレイヤー毎の得点
     * @return プレイヤー毎の順位
     */
    public HashMap<Integer, Integer> getRankingHashMap(HashMap<Integer, Integer> playersPoint) {
        HashMap<Integer, Integer> playersRankingHashMap = new HashMap<>();
        for (Player p : players) {

            // ランキングをつける
            playersRankingHashMap.put(p.getId(),
                    getRanking(p.getId(), playersPoint, playersRankingHashMap));

            Log.d("RANKING", p.getName() + " " + playersRankingHashMap.get(p.getId()) + "位");
        }

        return playersRankingHashMap;
    }

    /**
     * ランキングを取得します。
     *
     * @param playerId プレイヤーID
     * @param playersPoint プレイヤーのスコア
     * @return 順位
     */
    private int getRanking(int playerId, HashMap<Integer, Integer> playersPoint,
                          HashMap<Integer, Integer> playersRankingHashMap) {
        int ranking = 1;

        // 現在のプレイヤー以外のリストを生成
        List<Player> exceptingPlayers = new ArrayList<>();
        for (Player exp : players) {
            if (exp.getId() != playerId) {
                exceptingPlayers.add(exp);
            }
        }

        // 順位を計算
        double buf = playersPoint.get(playerId);
        for (Player exp : exceptingPlayers) {
            if (buf < playersPoint.get(exp.getId())) {
                ranking++;
            }
        }

        // 既にその順位のプレイヤーが存在していたら、順位を繰り上げる
        while (playersRankingHashMap.containsValue(ranking)) {
            ranking++;
        }

        return ranking;
    }

    /**
     * 親を流す。
     */
    public void flowParent() {
        int buf = 0;
        for (int i = 0; i < this.isParent.length; i++) {
            if (this.isParent[i]) {
                buf = i + 1;
                this.isParent[i] = false;
            }
        }

        if (buf < isParent.length) {
            this.isParent[buf] = true;
        }
    }

    /**
     * 半荘が終了したかを判定する。
     *
     * @return 半荘が終了したらtrue
     */
    public boolean isLastGame() {
        boolean isFinish = true;

        for (boolean buff : isParent) {
            if (buff) {
                isFinish = false;
            }
        }

        return isFinish;
    }

    /**
     * 局数を取得する。
     *
     * @return 局数
     */
    public int getNumOfHand() {
        int numOfHand = 1;

        for (int i = 0; i < isParent.length; i++) {
            if (isParent[i]) {
                numOfHand = i + 1;
            }
        }

        return numOfHand;
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

    /**
     * 連荘数を取得します。
     *
     * @return 連荘数
     */
    public int getNumOfhonba() {
        return numOfhonba;
    }

    /**
     * 連荘数をセットします。
     *
     * @param numOfhonba 連荘数
     */
    public void setNumOfhonba(int numOfhonba) {
        this.numOfhonba = numOfhonba;
    }

    public boolean[] getIsParent() {
        return isParent;
    }

    public void setIsParent(boolean[] isParent) {
        this.isParent = isParent;
    }
}
