package eval.wit.ai.calcmahjong.models.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import eval.wit.ai.calcmahjong.models.entities.Player;

/**
 * Created by koba on 2015/01/25.
 */
public class DatabaseAdapter {
    static final String DATABASE_NAME = "mahjong.db";
    static final int DATABASE_VERSION = 1;

    public static final String PLAYERS_TABLE_NAME = "players";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_MESSAGE = "message";
    public static final String COL_IS_PLAY = "is_play";
    public static final String COL_CREATE_AT = "create_at";
    public static final String COL_UPDATE_AT = "update_at";

    public static final String RECORD_TABLE_NAME = "record";
    public static final String COL_PLAYER_ID = "player_id";
    public static final String COL_SCORE = "score";
    public static final String COL_NUM_OF_PLAY = "play";
    public static final String COL_TOP = "top";
    public static final String COL_SECOND = "second";
    public static final String COL_THIRD = "third";
    public static final String COL_LAST = "last";
    public static final String COL_WINNING = "winning";
    public static final String COL_DISCARDING = "discarding";

    protected final Context context;
    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase db;

    /**
     * コンストラクタ。
     * @param context コンテキスト
     */
    public DatabaseAdapter(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    /**
     * DBを開きます。
     * @return dbHelper
     */
    public DatabaseAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    /**
     * DBを閉じます。
     */
    public void close() {
        dbHelper.close();
    }


    //
    // プレイヤー用
    //

    /**
     * すべてのプレイヤーを取得します。
     * @return 検索結果カーソル
     */
    public Cursor getAllPlayers() {
        return db.query(PLAYERS_TABLE_NAME, null, null, null, null, null, null);
    }

    /**
     * プレイ中のプレイヤーを取得します。
     * @return 検索結果カーソル
     */
    public Cursor getIsPlayPlayer() {
        String where = COL_IS_PLAY + " = ?";
        String[] param = { "1" };
        return db.query(PLAYERS_TABLE_NAME, null, where, param, null, null, null);
    }

    /**
     * プレイヤーを保存します。
     * @param name 名前
     * @param message ひとこと
     */
    public void savePlayer(String name, String message) {
        Date dateNow = new Date();
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        values.put(COL_NAME, name);
        values.put(COL_MESSAGE, message);
        values.put(COL_IS_PLAY, 0);
        values.put(COL_CREATE_AT, sdf.format(dateNow));
        values.put(COL_UPDATE_AT, sdf.format(dateNow));
        try {
            db.insertOrThrow(PLAYERS_TABLE_NAME, null, values);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
    }

    /**
     * プレイヤーを更新します。
     * @param player プレイヤー
     */
    public void updatePlayer(Player player) {
        Date dateNow = new Date();
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String whereClause = COL_ID + " = ?";
        String[] whereArgs = { String.valueOf(player.getId()) };
        values.put(COL_NAME, player.getName());
        values.put(COL_MESSAGE, player.getMessage());
        values.put(COL_IS_PLAY, player.isPlay());
        values.put(COL_UPDATE_AT, sdf.format(dateNow));
        db.update(PLAYERS_TABLE_NAME, values, whereClause, whereArgs);
    }

    /**
     * すべてのプレイヤーを削除します。
     * @return 成否
     */
    public boolean daleteAllPlayers() {
        return db.delete(PLAYERS_TABLE_NAME, null, null) > 0;
    }

    /**
     * 指定IDのプレイヤーを削除します。
     * @param id プレイヤーID
     * @return 成否
     */
    public boolean deletePlayer(int id) {
        return db.delete(PLAYERS_TABLE_NAME, COL_ID + "=" + id, null) > 0;
    }

    /**
     * 名前が重複している場合はtrueを返します。
     * @param name 名前
     * @return 重複している場合はtrue
     */
    public boolean isDuplicationPlayer(String name) {
        String where = COL_NAME + " = ?";
        String[] param = { name };

        Cursor c = db.query(PLAYERS_TABLE_NAME, null, where, param, null, null, null);


        ArrayList<Player> list = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Player duplicatedPlayer = new Player(
                        c.getInt(c.getColumnIndex(DatabaseAdapter.COL_ID)),
                        c.getString(c.getColumnIndex(DatabaseAdapter.COL_NAME)),
                        c.getString(c.getColumnIndex(DatabaseAdapter.COL_MESSAGE)),
                        c.getString(c.getColumnIndex(DatabaseAdapter.COL_IS_PLAY)).equals("1"));
                list.add(duplicatedPlayer);
            } while (c.moveToNext());
        }

        return !list.isEmpty();
    }


    //
    // 成績記録用
    //
    /**
     * 成績を保存します。
     * @param score スコア
     * @param message ひとこと
     */
    public void saveRecord(int score, String message) {
        Date dateNow = new Date();
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        values.put(COL_SCORE, score);
        values.put(COL_NUM_OF_PLAY, 1);
        values.put(COL_TOP, 0);
        values.put(COL_SECOND, 0);
        values.put(COL_THIRD, 0);
        values.put(COL_LAST, 0);
        values.put(COL_CREATE_AT, sdf.format(dateNow));
        values.put(COL_UPDATE_AT, sdf.format(dateNow));
        try {
            db.insertOrThrow(RECORD_TABLE_NAME, null, values);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
    }

    /**
     * 成績を更新します。
     * @param player プレイヤー
     */
    public void updateRecord(Player player) {
        Date dateNow = new Date();
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String whereClause = COL_ID + " = ?";
        String[] whereArgs = { String.valueOf(player.getId()) };
        values.put(COL_NAME, player.getName());
        values.put(COL_MESSAGE, player.getMessage());
        values.put(COL_IS_PLAY, player.isPlay());
        values.put(COL_UPDATE_AT, sdf.format(dateNow));
        db.update(PLAYERS_TABLE_NAME, values, whereClause, whereArgs);
    }


    /**
     * DBのヘルパークラスです。
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + PLAYERS_TABLE_NAME + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_NAME + " TEXT UNIQUE,"
                    + COL_MESSAGE + " TEXT,"
                    + COL_IS_PLAY + " INTEGER,"
                    + COL_CREATE_AT + " TEXT NOT NULL,"
                    + COL_UPDATE_AT + " TEXT NOT NULL);");
            db.execSQL("CREATE TABLE " + RECORD_TABLE_NAME + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "FOREIGN KEY(" + COL_PLAYER_ID + ") REFERENCES" + PLAYERS_TABLE_NAME + "(" + COL_ID + "),"
                    + COL_SCORE + " DOUBLE,"
                    + COL_NUM_OF_PLAY + " INTEGER NOT NULL,"
                    + COL_TOP + " INTEGER NOT NULL,"
                    + COL_SECOND + " INTEGER NOT NULL,"
                    + COL_THIRD + " INTEGER NOT NULL,"
                    + COL_LAST + " INTEGER NOT NULL,"
                    + COL_WINNING + " INTEGER NOT NULL,"
                    + COL_DISCARDING + " INTEGER NOT NULL,"
                    + COL_CREATE_AT + " TEXT NOT NULL,"
                    + COL_UPDATE_AT + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + PLAYERS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + RECORD_TABLE_NAME);
            onCreate(db);
        }
    }
}
