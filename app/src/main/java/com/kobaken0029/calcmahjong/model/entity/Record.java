package com.kobaken0029.calcmahjong.model.entity;

public class Record extends RecordBase {
    /**
     * デフォルトコンストラクタ。
     */
    public Record() {
        super();
    }

    public Record(int id, int playerId, double totalScore, int totalPlay,
                  int top, int second, int third, int last, int winning, int discarding) {
        super(id, playerId, totalScore, totalPlay, top, second, third, last, winning, discarding);
    }

    /**
     * ランキングをセットします。
     * @param rank 順位
     */
    public void setRanking(int rank) {
        switch (rank) {
            case 1:
                this.setTop(getTop() + 1);
                break;
            case 2:
                this.setSecond(getSecond() + 1);
                break;
            case 3:
                this.setThird(getThird() + 1);
                break;
            case 4:
                this.setLast(getLast() + 1);
                break;
            default:
                break;
        }
    }
}
