package com.kobaken0029.calcmahjong.model.entity;

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
