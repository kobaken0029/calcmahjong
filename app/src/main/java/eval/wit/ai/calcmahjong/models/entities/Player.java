package eval.wit.ai.calcmahjong.models.entities;


/**
 * Created by koba on 2015/01/25.
 */
public class Player extends PlayerBase {

    /**
     * デフォルトコンストラクタ
     */
    public Player() {
    }

    /**
     * プレイヤーを初期化。
     * @param id プレイヤーID
     * @param name プレイヤー名
     * @param message ひとこと
     * @param isPlay プレイ状態
     */
    public Player(int id, String name, String message, boolean isPlay) {
        super(id, name, message, isPlay);
    }
}
