package eval.wit.ai.calcmahjong.resources;

import android.content.Context;
import android.widget.ArrayAdapter;

import eval.wit.ai.calcmahjong.utilities.UiUtil;

/**
 * Created by koba on 2015/01/26.
 */
public class ConstsManager {

    /**
     * 飜数のアダプターを取得。
     *
     * @param c コンテキスト
     * @return アダプター
     */
    public static ArrayAdapter<String> getHanAdapter(Context c) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(c, android.R.layout.simple_spinner_item);
        adapter.add(Consts.I_HAN);
        adapter.add(Consts.RYAN_HAN);
        adapter.add(Consts.SAN_HAN);
        adapter.add(Consts.SHU_HAN);
        adapter.add(Consts.MANGAN);
        adapter.add(Consts.HANEMAN);
        adapter.add(Consts.BAIMAN);
        adapter.add(Consts.SAN_BAIMAN);
        adapter.add(Consts.YAKUMAN);
        adapter.add(Consts.DOUBLE_YAKUMAN);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    /**
     * 符数のアダプターを取得。
     *
     * @param c コンテキスト
     * @return アダプター
     */
    public static ArrayAdapter<String> getFuAdapter(Context c) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(c, android.R.layout.simple_spinner_item);
        adapter.add(Consts.NIZYU_FU);
        adapter.add(Consts.NIZYUGO_FU);
        adapter.add(Consts.SANZYU_FU);
        adapter.add(Consts.YONZYU_FU);
        adapter.add(Consts.GOZYU_FU);
        adapter.add(Consts.ROKUZYU_FU);
        adapter.add(Consts.NANAZYU_FU);
        adapter.add(Consts.HATIZYU_FU);
        adapter.add(Consts.KYUZYU_FU);
        adapter.add(Consts.HYAKU_FU);
        adapter.add(Consts.HYAKUZYU_FU);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    /**
     * 符数と飜数から得点を返します。
     *
     * @param fu  符
     * @param han 翻
     * @return 得点
     */
    public static int calcPoint(String fu, String han, boolean isParent, boolean tumo, int honba) {
        int numOfFu = Integer.parseInt(fu.replace("符", ""));
        int point = 0;


        // 満貫
        if (isMangan(numOfFu, han)) {
            point = isParent ? Consts.ITIMANNISE : Consts.HASSE;
        } else {
            switch (fu) {
                case Consts.NIZYU_FU:
                    break;
                // 25符
                case Consts.NIZYUGO_FU:
                    point = getPoint25Fu(han, isParent);
                    break;

                // 30符
                case Consts.SANZYU_FU:
                    point = getPoint30Fu(han, isParent);
                    break;

                // 40符
                case Consts.YONZYU_FU:
                    point = getPoint40Fu(han, isParent);
                    break;

                // 50符
                case Consts.GOZYU_FU:
                    point = getPoint50Fu(han, isParent);
                    break;

                // 60符
                case Consts.ROKUZYU_FU:
                    point = getPoint60Fu(han, isParent);
                    break;

                // 70符
                case Consts.NANAZYU_FU:
                    point = getPoint70Fu(han, isParent);
                    break;

                // 80符
                case Consts.HATIZYU_FU:
                    point = getPoint80Fu(han, isParent);
                    break;

                // 90符
                case Consts.KYUZYU_FU:
                    point = getPoint90Fu(han, isParent);
                    break;

                // 100符
                case Consts.HYAKU_FU:
                    point = getPoint100Fu(han, isParent);
                    break;

                // 110符
                case Consts.HYAKUZYU_FU:
                    point = getPoint110Fu(han, isParent);
                    break;
            }
        }

        // 跳満以上
        if (point == 0) {
            switch (han) {
                case Consts.HANEMAN:
                    point = isParent ? Consts.ITIMANHASSE : Consts.ITIMANNISE;
                    break;
                case Consts.BAIMAN:
                    point = isParent ? Consts.NIMANYONSE : Consts.ITIMANROKUSE;
                    break;
                case Consts.SAN_BAIMAN:
                    point = isParent ? Consts.SANMANROKUSE : Consts.NIMANYONSE;
                    break;
                case Consts.YAKUMAN:
                    point = isParent ? Consts.YONMANHASSE : Consts.SANMANNNISE;
                    break;
                case Consts.DOUBLE_YAKUMAN:
                    point = isParent ? Consts.KYUMANROKUSE : Consts.ROKUMANYONSE;
                    break;
            }
        }

        point += honba * 300;

        return point;
    }

    /**
     * 25符の時の得点を取得。
     *
     * @param han      翻
     * @param isParent 親ならtrue
     * @return 得点
     */
    private static int getPoint25Fu(String han, boolean isParent) {
        int point = 0;

        switch (han) {
            case Consts.RYAN_HAN:
                point = isParent ? Consts.NIYON : Consts.ITIROKU;
                break;
            case Consts.SAN_HAN:
                point = isParent ? Consts.YONPA : Consts.ZANNI;
                break;
            case Consts.SHU_HAN:
                point = isParent ? Consts.KUNROKU : Consts.ROKUYON;
                break;
        }

        return point;
    }

    /**
     * 30符の時の得点を取得。
     *
     * @param han      翻
     * @param isParent 親ならtrue
     * @return 得点
     */
    private static int getPoint30Fu(String han, boolean isParent) {
        int point = 0;

        switch (han) {
            case Consts.I_HAN:
                point = isParent ? Consts.ITIGO : Consts.SEN;
                break;
            case Consts.RYAN_HAN:
                point = isParent ? Consts.NIKKU : Consts.NISEN;
                break;
            case Consts.SAN_HAN:
                point = isParent ? Consts.GOPPA : Consts.ZANKU;
                break;
            case Consts.SHU_HAN:
                point = isParent ? Consts.PINPINROKU : Consts.TITTI;
                break;
        }

        return point;
    }

    /**
     * 40符の時の得点を取得。
     *
     * @param han      翻
     * @param isParent 親ならtrue
     * @return 得点
     */
    private static int getPoint40Fu(String han, boolean isParent) {
        int point = 0;

        switch (han) {
            case Consts.I_HAN:
                point = isParent ? Consts.NISEN : Consts.ITISAN;
                break;
            case Consts.RYAN_HAN:
                point = isParent ? Consts.ZANKU : Consts.NINROKU;
                break;
            case Consts.SAN_HAN:
                point = isParent ? Consts.TITTI : Consts.GOTU;
                break;
        }

        return point;
    }

    /**
     * 50符の時の得点を取得。
     *
     * @param han      翻
     * @param isParent 親ならtrue
     * @return 得点
     */
    private static int getPoint50Fu(String han, boolean isParent) {
        int point = 0;

        switch (han) {
            case Consts.I_HAN:
                point = isParent ? Consts.NIYON : Consts.ITIROKU;
                break;
            case Consts.RYAN_HAN:
                point = isParent ? Consts.YONPA : Consts.ZANNI;
                break;
            case Consts.SAN_HAN:
                point = isParent ? Consts.KUNROKU : Consts.ROKUYON;
                break;
        }

        return point;
    }

    /**
     * 60符の時の得点を取得。
     *
     * @param han      翻
     * @param isParent 親ならtrue
     * @return 得点
     */
    private static int getPoint60Fu(String han, boolean isParent) {
        int point = 0;

        switch (han) {
            case Consts.I_HAN:
                point = isParent ? Consts.NIKKU : Consts.NISEN;
                break;
            case Consts.RYAN_HAN:
                point = isParent ? Consts.GOPPA : Consts.ZANKU;
                break;
            case Consts.SAN_HAN:
                point = isParent ? Consts.PINPINROKU : Consts.TITTI;
                break;
        }

        return point;
    }

    /**
     * 70符の時の得点を取得。
     *
     * @param han      翻
     * @param isParent 親ならtrue
     * @return 得点
     */
    private static int getPoint70Fu(String han, boolean isParent) {
        int point = 0;

        switch (han) {
            case Consts.I_HAN:
                point = isParent ? Consts.SANYON : Consts.NISAN;
                break;
            case Consts.RYAN_HAN:
                point = isParent ? Consts.ROPPA : Consts.YONGO;
                break;
        }

        return point;
    }

    /**
     * 80符の時の得点を取得。
     *
     * @param han      翻
     * @param isParent 親ならtrue
     * @return 得点
     */
    private static int getPoint80Fu(String han, boolean isParent) {
        int point = 0;

        switch (han) {
            case Consts.I_HAN:
                point = isParent ? Consts.ZANKU : Consts.NINROKU;
                break;
            case Consts.RYAN_HAN:
                point = isParent ? Consts.TITTI : Consts.GOTU;
                break;
        }

        return point;
    }

    /**
     * 90符の時の得点を取得。
     *
     * @param han      翻
     * @param isParent 親ならtrue
     * @return 得点
     */
    private static int getPoint90Fu(String han, boolean isParent) {
        int point = 0;

        switch (han) {
            case Consts.I_HAN:
                point = isParent ? Consts.YONYON : Consts.NIKKU;
                break;
            case Consts.RYAN_HAN:
                point = isParent ? Consts.HATINANA : Consts.GOPPA;
                break;
        }

        return point;
    }

    /**
     * 100符の時の得点を取得。
     *
     * @param han      翻
     * @param isParent 親ならtrue
     * @return 得点
     */
    private static int getPoint100Fu(String han, boolean isParent) {
        int point = 0;

        switch (han) {
            case Consts.I_HAN:
                point = isParent ? Consts.YONPA : Consts.ZANNI;
                break;
            case Consts.RYAN_HAN:
                point = isParent ? Consts.KUNROKU : Consts.ROKUYON;
                break;
        }

        return point;
    }

    /**
     * 110符の時の得点を取得。
     *
     * @param han      翻
     * @param isParent 親ならtrue
     * @return 得点
     */
    private static int getPoint110Fu(String han, boolean isParent) {
        int point = 0;

        switch (han) {
            case Consts.I_HAN:
                point = isParent ? Consts.GOSAN : Consts.SABUROKU;
                break;
            case Consts.RYAN_HAN:
                point = isParent ? Consts.PINMARUROKU : Consts.NANASENHYAKU;
                break;
        }

        return point;
    }

    /**
     * 満貫かどうか判定をする。
     *
     * @param fu  符数
     * @param han 翻
     * @return 満貫ならtrue
     */
    private static boolean isMangan(int fu, String han) {
        return han.equals(Consts.MANGAN)
                || han.equals(Consts.SHU_HAN) && fu >= 40
                || han.equals(Consts.SAN_HAN) && fu >= 70;
    }
}
