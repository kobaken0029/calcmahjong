package eval.wit.ai.calcmahjong.models.entities;

import java.io.Serializable;

/**
 * Created by koba on 2015/01/25.
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String message;
    private boolean isPlay;

    public Player() {
    }

    public Player(int id, String name, String message) {
        this.id = id;
        this.name = name;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean isPlay) {
        this.isPlay = isPlay;
    }
}
