package eval.wit.ai.calcmahjong.models.clients;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eval.wit.ai.calcmahjong.models.entities.Player;

/**
 * Created by koba on 2015/01/25.
 */
public class AppController extends Application {
    private ArrayList<Player> players;

    private int gameCnt;
    private List<HashMap<Integer, Integer>> playersPointList;

    @Override
    public void onCreate() {
        super.onCreate();
        gameCnt = 1;
        playersPointList = new ArrayList<>();

        // mock
        Player p1 = new Player(1, "A君", "よろしく");
        Player p2 = new Player(2, "B君", "よろしく");
        Player p3 = new Player(3, "C君", "よろしく");
        Player p4 = new Player(4, "D君", "よろしく");

        ArrayList<Player> ps = new ArrayList<>();
        ps.add(p1);
        ps.add(p2);
        ps.add(p3);
        ps.add(p4);
        setPlayers(ps);
    }

    /**
     * 現在の半荘数を返します。
     * @return 半荘数
     */
    public int getGameCnt() {
        return gameCnt;
    }

    /**
     * 半荘数をセットします。
     * @param gameCnt 半荘数
     */
    public void setGameCnt(int gameCnt) {
        this.gameCnt = gameCnt;
    }

    /**
     * プレイヤー群を取得。
     * @return プレイヤー群
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * プレイヤー群をセット。
     * @param players プレイヤー群
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = new ArrayList<>(players);
    }

    /**
     * プレイヤー群の夫々の得点群を取得します。
     * @return プレイヤー群の夫々の得点群
     */
    public List<HashMap<Integer, Integer>> getPlayersPointList() {
        return playersPointList;
    }

    /**
     * プレイヤー群の夫々の得点群をセットします。
     * @param playersPointList プレイヤー群の夫々の得点群
     */
    public void setPlayersPointList(List<HashMap<Integer, Integer>> playersPointList) {
        this.playersPointList = new ArrayList<>(playersPointList);
    }

    /**
     * プレイヤー群の夫々の得点をリストに追加します。
     * @param playersPoint プレイヤー群の夫々の得点
     */
    public void addPlayersPoint(HashMap<Integer, Integer> playersPoint) {
        this.playersPointList.add(playersPoint);
    }
}
