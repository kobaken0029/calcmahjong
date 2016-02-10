package com.kobaken0029.calcmahjong.model.entity;

import java.io.Serializable;

public class PlayerBase implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String message;
    private boolean isPlay;
    private boolean isParent;

    /**
     * デフォルトコンストラクタ
     */
    public PlayerBase() {
    }

    /**
     * プレイヤーを初期化。
     * @param id プレイヤーID
     * @param name プレイヤー名
     * @param message ひとこと
     * @param isPlay プレイ状態
     */
    public PlayerBase(int id, String name, String message, boolean isPlay) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.isPlay = isPlay;
    }

    /**
     * プレイヤーのIDを取得。
     * @return プレイヤーID
     */
    public int getId() {
        return id;
    }

    /**
     * プレイヤーのIDをセット。
     * @param id プレイヤーID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * プレイヤー名を取得。
     * @return プレイヤー名
     */
    public String getName() {
        return name;
    }

    /**
     * プレイヤー名をセット。
     * @param name プレイヤー名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * ひとことを取得。
     * @return ひとこと
     */
    public String getMessage() {
        return message;
    }

    /**
     * ひとことをセット。
     * @param message ひとこと
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * プレイ中かどうかを取得。
     * @return プレイ中ならtrue
     */
    public boolean isPlay() {
        return isPlay;
    }

    /**
     * プレイ中かどうかをセット。
     * @param isPlay プレイ状態
     */
    public void setPlay(boolean isPlay) {
        this.isPlay = isPlay;
    }

    /**
     * 親かどうかを取得。
     *
     * @return 親ならtrue
     */
    public boolean isParent() {
        return isParent;
    }

    /**
     * 親かどうかをセット。
     *
     * @param isParent 親かどうか
     */
    public void setParent(boolean isParent) {
        this.isParent = isParent;
    }
}
