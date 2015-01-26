package eval.wit.ai.calcmahjong.models.clients;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import eval.wit.ai.calcmahjong.models.entities.Player;

/**
 * Created by koba on 2015/01/25.
 */
public class AppController extends Application {
    private Context mContext;
    private ArrayList<Player> players;

    private int gameCnt;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        gameCnt = 0;
    }

    /**
     * 現在の半荘数を返します。
     * @return 半荘数
     */
    public int getGameCnt() {
        return ++gameCnt;
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
        // mock
        Player p1 = new Player(1, "A君", "よろしく");
        Player p2 = new Player(2, "B君", "よろしく");
        Player p3 = new Player(3, "C君", "よろしく");
        Player p4 = new Player(4, "D君", "よろしく");
        players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);

        return players;
    }

    /**
     * プレイヤー群をセット。
     * @param players プレイヤー群
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
