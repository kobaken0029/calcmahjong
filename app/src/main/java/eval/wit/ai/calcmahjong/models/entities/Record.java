package eval.wit.ai.calcmahjong.models.entities;

import java.io.Serializable;

/**
 * Created by koba on 2015/01/26.
 */
public class Record implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int playerId;
    private double totalScore;
    private int totalPlay;
    private int top;
    private int second;
    private int third;
    private int last;
    private int winning;
    private int discarding;

    public Record(int id, int playerId, double totalScore, int totalPlay,
                  int top, int second, int third, int last, int winning, int discarding) {
        this.id = id;
        this.playerId = playerId;
        this.totalScore = totalScore;
        this.totalPlay = totalPlay;
        this.top = top;
        this.second = second;
        this.third = third;
        this.last = last;
        this.winning = winning;
        this.discarding = discarding;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public int getTotalPlay() {
        return totalPlay;
    }

    public void setTotalPlay(int totalPlay) {
        this.totalPlay = totalPlay;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getThird() {
        return third;
    }

    public void setThird(int third) {
        this.third = third;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public int getWinning() {
        return winning;
    }

    public void setWinning(int winning) {
        this.winning = winning;
    }

    public int getDiscarding() {
        return discarding;
    }

    public void setDiscarding(int discarding) {
        this.discarding = discarding;
    }
}
