package eval.wit.ai.calcmahjong.resources;

import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;

import eval.wit.ai.calcmahjong.R;

/**
 * Created by koba on 2015/01/26.
 */
public class ConstsManager {

    /**
     * プレイヤーの参加人数を取得。
     * @return 参加人数
     */
    public static int getNumOfPlayer() {
        return Consts.NUM_OF_PLAYER_YONMA;
    }

    /**
     * 飜数のアダプターを取得。
     *
     * @param c コンテキスト
     * @return アダプター
     */
    public static ArrayAdapter<String> getHanAdapter(Context c) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(c, R.layout.spinner_item);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(c, R.layout.spinner_item);
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
     * プレイヤーの最初の持ち点を取得します。
     *
     * @return 最初の持ち点
     */
    public static int getFirstScore(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context)
                .getString("distribution_origin_point_list", "25000"));
    }

    /**
     * オカを取得します。
     *
     * @param context コンテキスト
     * @return オカ
     */
    public static int getOka(Context context) {
        int oka = 0;
        switch (PreferenceManager.getDefaultSharedPreferences(context)
                .getString("distribution_origin_point_list", "25000")) {
            case Consts.DO_20000:
                oka = Consts.OKA_20000_30000;
                break;
            case Consts.DO_25000:
                oka = Consts.OKA_25000_30000;
                break;
            case Consts.DO_26000:
                oka = Consts.OKA_26000_30000;
                break;
            case Consts.DO_27000:
                oka = Consts.OKA_27000_30000;
                break;
            case Consts.DO_30000:
                oka = Consts.OKA_30000_30000;
                break;
        }
        return oka;
    }

    /**
     * 順位ウマを取得します。
     *
     * @param context コンテキスト
     * @param ranking 順位
     * @return ウマ
     */
    public static int getUma(Context context, int ranking) {
        int[] bufOfUma = new int[2];
        switch (PreferenceManager.getDefaultSharedPreferences(context)
                .getString("ranking_uma_list", "ゴットー")) {
            case Consts.GOTTO:
                bufOfUma = Consts.GOTTO_POINT;
                break;
            case Consts.ONE_TWO:
                bufOfUma = Consts.ONE_TWO_POINT;
                break;
            case Consts.ONE_THREE:
                bufOfUma = Consts.ONE_THREE_POINT;
                break;
            case Consts.TWO_THREE:
                bufOfUma = Consts.TWO_THREE_POINT;
                break;
        }

        int rankingUma = 0;
        switch (ranking) {
            case 1:
                rankingUma += bufOfUma[1];
                break;
            case 2:
                rankingUma += bufOfUma[0];
                break;
            case 3:
                rankingUma -= bufOfUma[0];
                break;
            case 4:
                rankingUma -= bufOfUma[1];
                break;
            default:
                break;
        }
        return rankingUma;
    }

    /**
     * 符数と飜数から得点を返します。
     *
     * @param fu  符
     * @param han 翻
     * @return 得点
     */
    public static int[] calcPoint(String fu, String han, boolean isParent, boolean tumo, int honba) {
        int[] point;

        if (tumo) {
            point = getPointTumo(fu, han, isParent, honba);
        } else {
            point = getPointRon(fu, han, isParent, honba);
        }

        return point;
    }

    /**
     * 自摸和了時の点数を返します。
     *
     * @param fu       符
     * @param han      翻
     * @param isParent 親ならtrue
     * @param honba    本場数
     * @return 得点
     */
    private static int[] getPointTumo(String fu, String han, boolean isParent, int honba) {
        int numOfFu = Integer.parseInt(fu.replace("符", ""));
        int[] point = new int[3];
        int parentPoint = 0;
        int childPoint = 0;

        // 満貫
        if (isMangan(numOfFu, han)) {
            for (int i = 0; i < point.length; i++) {
                if (isParent || i == 0) {
                    point[i] = Consts.YONSE;
                } else {
                    point[i] = Consts.NISEN;
                }
            }
        } else {
            switch (fu) {
                case Consts.NIZYU_FU:
                    point = getPoint20Fu(han, isParent);
                    break;
                // 25符
                case Consts.NIZYUGO_FU:
                    point = getPoint25Fu(han, isParent, true);
                    break;

                // 30符
                case Consts.SANZYU_FU:
                    point = getPoint30Fu(han, isParent, true);
                    break;

                // 40符
                case Consts.YONZYU_FU:
                    point = getPoint40Fu(han, isParent, true);
                    break;

                // 50符
                case Consts.GOZYU_FU:
                    point = getPoint50Fu(han, isParent, true);
                    break;

                // 60符
                case Consts.ROKUZYU_FU:
                    point = getPoint60Fu(han, isParent, true);
                    break;

                // 70符
                case Consts.NANAZYU_FU:
                    point = getPoint70Fu(han, isParent, true);
                    break;

                // 80符
                case Consts.HATIZYU_FU:
                    point = getPoint80Fu(han, isParent, true);
                    break;

                // 90符
                case Consts.KYUZYU_FU:
                    point = getPoint90Fu(han, isParent, true);
                    break;

                // 100符
                case Consts.HYAKU_FU:
                    point = getPoint100Fu(han, isParent, true);
                    break;

                // 110符
                case Consts.HYAKUZYU_FU:
                    point = getPoint110Fu(han, isParent, true);
                    break;
            }
        }

        // 跳満以上
        if (point[0] == 0) {
            switch (han) {
                case Consts.HANEMAN:
                    parentPoint = Consts.ROKUSE;
                    childPoint = Consts.SANZE;
                    break;
                case Consts.BAIMAN:
                    parentPoint = Consts.HASSE;
                    childPoint = Consts.YONSE;
                    break;
                case Consts.SAN_BAIMAN:
                    parentPoint = Consts.ITIMANNISE;
                    childPoint = Consts.ROKUSE;
                    break;
                case Consts.YAKUMAN:
                    parentPoint = Consts.ITIMANROKUSE;
                    childPoint = Consts.HASSE;
                    break;
                case Consts.DOUBLE_YAKUMAN:
                    parentPoint = Consts.SANMANNISE;
                    childPoint = Consts.ITIMANROKUSE;
                    break;
            }

            for (int i = 0; i < point.length; i++) {
                if (isParent || i == 0) {
                    point[i] = parentPoint;
                } else {
                    point[i] = childPoint;
                }
            }
        }

        for (int i = 0; i < point.length; i++) {
            point[i] += honba * 100;
        }

        return point;
    }

    /**
     * ロン和了時の点数を返します。
     *
     * @param fu       符
     * @param han      翻
     * @param isParent 親ならtrue
     * @param honba    本場数
     * @return 得点
     */
    private static int[] getPointRon(String fu, String han, boolean isParent, int honba) {
        int numOfFu = Integer.parseInt(fu.replace("符", ""));
        int[] point = new int[3];

        // 満貫
        if (isMangan(numOfFu, han)) {
            point[0] = isParent ? Consts.ITIMANNISE : Consts.HASSE;
        } else {
            switch (fu) {
                // 25符
                case Consts.NIZYUGO_FU:
                    point = getPoint25Fu(han, isParent, false);
                    break;

                // 30符
                case Consts.SANZYU_FU:
                    point = getPoint30Fu(han, isParent, false);
                    break;

                // 40符
                case Consts.YONZYU_FU:
                    point = getPoint40Fu(han, isParent, false);
                    break;

                // 50符
                case Consts.GOZYU_FU:
                    point = getPoint50Fu(han, isParent, false);
                    break;

                // 60符
                case Consts.ROKUZYU_FU:
                    point = getPoint60Fu(han, isParent, false);
                    break;

                // 70符
                case Consts.NANAZYU_FU:
                    point = getPoint70Fu(han, isParent, false);
                    break;

                // 80符
                case Consts.HATIZYU_FU:
                    point = getPoint80Fu(han, isParent, false);
                    break;

                // 90符
                case Consts.KYUZYU_FU:
                    point = getPoint90Fu(han, isParent, false);
                    break;

                // 100符
                case Consts.HYAKU_FU:
                    point = getPoint100Fu(han, isParent, false);
                    break;

                // 110符
                case Consts.HYAKUZYU_FU:
                    point = getPoint110Fu(han, isParent, false);
                    break;
            }
        }

        // 跳満以上
        if (point[0] == 0) {
            switch (han) {
                case Consts.HANEMAN:
                    point[0] = isParent ? Consts.ITIMANHASSE : Consts.ITIMANNISE;
                    break;
                case Consts.BAIMAN:
                    point[0] = isParent ? Consts.NIMANYONSE : Consts.ITIMANROKUSE;
                    break;
                case Consts.SAN_BAIMAN:
                    point[0] = isParent ? Consts.SANMANROKUSE : Consts.NIMANYONSE;
                    break;
                case Consts.YAKUMAN:
                    point[0] = isParent ? Consts.YONMANHASSE : Consts.SANMANNISE;
                    break;
                case Consts.DOUBLE_YAKUMAN:
                    point[0] = isParent ? Consts.KYUMANROKUSE : Consts.ROKUMANYONSE;
                    break;
            }
        }

        point[0] += honba * 300;

        return point;
    }

    /**
     * 20符の時の得点を取得。
     *
     * @param han      翻
     * @param isParent 親ならtrue
     * @return 得点
     */
    private static int[] getPoint20Fu(String han, boolean isParent) {
        int[] point = new int[3];
        int parentPoint = 0;
        int childPoint = 0;

        switch (han) {
            case Consts.RYAN_HAN:
                parentPoint = Consts.NANAHYAKU;
                childPoint = Consts.YONHYAKU;
                break;
            case Consts.SAN_HAN:
                parentPoint = Consts.ITISAN;
                childPoint = Consts.NANAHYAKU;
                break;
            case Consts.SHU_HAN:
                parentPoint = Consts.NINROKU;
                childPoint = Consts.ITISAN;
                break;
        }

        for (int i = 0; i < point.length; i++) {
            if (isParent || i == 0) {
                point[i] = parentPoint;
            } else {
                point[i] = childPoint;
            }
        }

        return point;
    }

    /**
     * 25符の時の得点を取得。
     *
     * @param han      翻
     * @param isParent 親ならtrue
     * @return 得点
     */
    private static int[] getPoint25Fu(String han, boolean isParent, boolean tumo) {
        int[] point = new int[3];
        int parentPoint = 0;
        int childPoint = 0;

        if (tumo) {
            switch (han) {
                case Consts.RYAN_HAN:
                    parentPoint = Consts.NINROKU;
                    childPoint = Consts.ITISAN;
                    break;
                case Consts.SAN_HAN:
                    parentPoint = Consts.NINROKU;
                    childPoint = Consts.ITISAN;
                    break;
                case Consts.SHU_HAN:
                    parentPoint = Consts.NINROKU;
                    childPoint = Consts.ITISAN;
                    break;
            }

            for (int i = 0; i < point.length; i++) {
                if (isParent || i == 0) {
                    point[i] = parentPoint;
                } else {
                    point[i] = childPoint;
                }
            }
        } else {
            switch (han) {
                case Consts.RYAN_HAN:
                    point[0] = isParent ? Consts.NIYON : Consts.ITIROKU;
                    break;
                case Consts.SAN_HAN:
                    point[0] = isParent ? Consts.YONPA : Consts.ZANNI;
                    break;
                case Consts.SHU_HAN:
                    point[0] = isParent ? Consts.KUNROKU : Consts.ROKUYON;
                    break;
            }
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
    private static int[] getPoint30Fu(String han, boolean isParent, boolean tumo) {
        int[] point = new int[3];
        int parentPoint = 0;
        int childPoint = 0;

        if (tumo) {
            switch (han) {
                case Consts.I_HAN:
                    parentPoint = Consts.GOHYAKU;
                    childPoint = Consts.SANBYAKU;
                    break;
                case Consts.RYAN_HAN:
                    parentPoint = Consts.SEN;
                    childPoint = Consts.GOHYAKU;
                    break;
                case Consts.SAN_HAN:
                    parentPoint = Consts.NISEN;
                    childPoint = Consts.SEN;
                    break;
                case Consts.SHU_HAN:
                    parentPoint = Consts.ZANKU;
                    childPoint = Consts.NISEN;
                    break;
            }

            for (int i = 0; i < point.length; i++) {
                if (isParent || i == 0) {
                    point[i] = parentPoint;
                } else {
                    point[i] = childPoint;
                }
            }
        } else {
            switch (han) {
                case Consts.I_HAN:
                    point[0] = isParent ? Consts.ITIGO : Consts.SEN;
                    break;
                case Consts.RYAN_HAN:
                    point[0] = isParent ? Consts.NIKKU : Consts.NISEN;
                    break;
                case Consts.SAN_HAN:
                    point[0] = isParent ? Consts.GOPPA : Consts.ZANKU;
                    break;
                case Consts.SHU_HAN:
                    point[0] = isParent ? Consts.PINPINROKU : Consts.TITTI;
                    break;
            }
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
    private static int[] getPoint40Fu(String han, boolean isParent, boolean tumo) {
        int[] point = new int[3];
        int parentPoint = 0;
        int childPoint = 0;

        if (tumo) {
            switch (han) {
                case Consts.I_HAN:
                    parentPoint = Consts.NANAHYAKU;
                    childPoint = Consts.YONHYAKU;
                    break;
                case Consts.RYAN_HAN:
                    parentPoint = Consts.ITISAN;
                    childPoint = Consts.NANAHYAKU;
                    break;
                case Consts.SAN_HAN:
                    parentPoint = Consts.NINROKU;
                    childPoint = Consts.ITISAN;
                    break;
            }

            for (int i = 0; i < point.length; i++) {
                if (isParent || i == 0) {
                    point[i] = parentPoint;
                } else {
                    point[i] = childPoint;
                }
            }
        } else {
            switch (han) {
                case Consts.I_HAN:
                    point[0] = isParent ? Consts.NISEN : Consts.ITISAN;
                    break;
                case Consts.RYAN_HAN:
                    point[0] = isParent ? Consts.ZANKU : Consts.NINROKU;
                    break;
                case Consts.SAN_HAN:
                    point[0] = isParent ? Consts.TITTI : Consts.GOTU;
                    break;
            }
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
    private static int[] getPoint50Fu(String han, boolean isParent, boolean tumo) {
        int[] point = new int[3];
        int parentPoint = 0;
        int childPoint = 0;

        if (tumo) {
            switch (han) {
                case Consts.I_HAN:
                    parentPoint = Consts.HAPPYAKU;
                    childPoint = Consts.YONHYAKU;
                    break;
                case Consts.RYAN_HAN:
                    parentPoint = Consts.ITIROKU;
                    childPoint = Consts.HAPPYAKU;
                    break;
                case Consts.SAN_HAN:
                    parentPoint = Consts.ZANNI;
                    childPoint = Consts.ITIROKU;
                    break;
            }

            for (int i = 0; i < point.length; i++) {
                if (isParent || i == 0) {
                    point[i] = parentPoint;
                } else {
                    point[i] = childPoint;
                }
            }
        } else {
            switch (han) {
                case Consts.I_HAN:
                    point[0] = isParent ? Consts.NIYON : Consts.ITIROKU;
                    break;
                case Consts.RYAN_HAN:
                    point[0] = isParent ? Consts.YONPA : Consts.ZANNI;
                    break;
                case Consts.SAN_HAN:
                    point[0] = isParent ? Consts.KUNROKU : Consts.ROKUYON;
                    break;
            }
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
    private static int[] getPoint60Fu(String han, boolean isParent, boolean tumo) {
        int[] point = new int[3];
        int parentPoint = 0;
        int childPoint = 0;

        if (tumo) {
            switch (han) {
                case Consts.I_HAN:
                    parentPoint = Consts.SEN;
                    childPoint = Consts.GOHYAKU;
                    break;
                case Consts.RYAN_HAN:
                    parentPoint = Consts.NISEN;
                    childPoint = Consts.SEN;
                    break;
                case Consts.SAN_HAN:
                    parentPoint = Consts.ZANKU;
                    childPoint = Consts.NISEN;
                    break;
            }

            for (int i = 0; i < point.length; i++) {
                if (isParent || i == 0) {
                    point[i] = parentPoint;
                } else {
                    point[i] = childPoint;
                }
            }
        } else {
            switch (han) {
                case Consts.I_HAN:
                    point[0] = isParent ? Consts.NIKKU : Consts.NISEN;
                    break;
                case Consts.RYAN_HAN:
                    point[0] = isParent ? Consts.GOPPA : Consts.ZANKU;
                    break;
                case Consts.SAN_HAN:
                    point[0] = isParent ? Consts.PINPINROKU : Consts.TITTI;
                    break;
            }
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
    private static int[] getPoint70Fu(String han, boolean isParent, boolean tumo) {
        int[] point = new int[3];
        int parentPoint = 0;
        int childPoint = 0;

        if (tumo) {
            switch (han) {
                case Consts.I_HAN:
                    parentPoint = Consts.SENNIHYAKU;
                    childPoint = Consts.ROPPYAKU;
                    break;
                case Consts.RYAN_HAN:
                    parentPoint = Consts.NISAN;
                    childPoint = Consts.SENNIHYAKU;
                    break;
            }

            for (int i = 0; i < point.length; i++) {
                if (isParent || i == 0) {
                    point[i] = parentPoint;
                } else {
                    point[i] = childPoint;
                }
            }
        } else {
            switch (han) {
                case Consts.I_HAN:
                    point[0] = isParent ? Consts.SANYON : Consts.NISAN;
                    break;
                case Consts.RYAN_HAN:
                    point[0] = isParent ? Consts.ROPPA : Consts.YONGO;
                    break;
            }
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
    private static int[] getPoint80Fu(String han, boolean isParent, boolean tumo) {
        int[] point = new int[3];
        int parentPoint = 0;
        int childPoint = 0;

        if (tumo) {
            switch (han) {
                case Consts.I_HAN:
                    parentPoint = Consts.ITISAN;
                    childPoint = Consts.NANAHYAKU;
                    break;
                case Consts.RYAN_HAN:
                    parentPoint = Consts.NINROKU;
                    childPoint = Consts.ITISAN;
                    break;
            }

            for (int i = 0; i < point.length; i++) {
                if (isParent || i == 0) {
                    point[i] = parentPoint;
                } else {
                    point[i] = childPoint;
                }
            }
        } else {
            switch (han) {
                case Consts.I_HAN:
                    point[0] = isParent ? Consts.ZANKU : Consts.NINROKU;
                    break;
                case Consts.RYAN_HAN:
                    point[0] = isParent ? Consts.TITTI : Consts.GOTU;
                    break;
            }
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
    private static int[] getPoint90Fu(String han, boolean isParent, boolean tumo) {
        int[] point = new int[3];
        int parentPoint = 0;
        int childPoint = 0;

        if (tumo) {
            switch (han) {
                case Consts.I_HAN:
                    parentPoint = Consts.ITIGO;
                    childPoint = Consts.HAPPYAKU;
                    break;
                case Consts.RYAN_HAN:
                    parentPoint = Consts.NIKKU;
                    childPoint = Consts.ITIGO;
                    break;
            }

            for (int i = 0; i < point.length; i++) {
                if (isParent || i == 0) {
                    point[i] = parentPoint;
                } else {
                    point[i] = childPoint;
                }
            }
        } else {
            switch (han) {
                case Consts.I_HAN:
                    point[0] = isParent ? Consts.YONYON : Consts.NIKKU;
                    break;
                case Consts.RYAN_HAN:
                    point[0] = isParent ? Consts.HATINANA : Consts.GOPPA;
                    break;
            }
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
    private static int[] getPoint100Fu(String han, boolean isParent, boolean tumo) {
        int[] point = new int[3];
        int parentPoint = 0;
        int childPoint = 0;

        if (tumo) {
            switch (han) {
                case Consts.I_HAN:
                    parentPoint = Consts.ITIROKU;
                    childPoint = Consts.HAPPYAKU;
                    break;
                case Consts.RYAN_HAN:
                    parentPoint = Consts.ZANNI;
                    childPoint = Consts.ITIROKU;
                    break;
            }

            for (int i = 0; i < point.length; i++) {
                if (isParent || i == 0) {
                    point[i] = parentPoint;
                } else {
                    point[i] = childPoint;
                }
            }
        } else {
            switch (han) {
                case Consts.I_HAN:
                    point[0] = isParent ? Consts.YONPA : Consts.ZANNI;
                    break;
                case Consts.RYAN_HAN:
                    point[0] = isParent ? Consts.KUNROKU : Consts.ROKUYON;
                    break;
            }
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
    private static int[] getPoint110Fu(String han, boolean isParent, boolean tumo) {
        int[] point = new int[3];
        int parentPoint = 0;
        int childPoint = 0;

        if (tumo) {
            switch (han) {
                case Consts.I_HAN:
                    parentPoint = Consts.SENHAPPYAKU;
                    childPoint = Consts.KYUHYAKU;
                    break;
                case Consts.RYAN_HAN:
                    parentPoint = Consts.SABUROKU;
                    childPoint = Consts.SENHAPPYAKU;
                    break;
            }

            for (int i = 0; i < point.length; i++) {
                if (isParent || i == 0) {
                    point[i] = parentPoint;
                } else {
                    point[i] = childPoint;
                }
            }
        } else {
            switch (han) {
                case Consts.I_HAN:
                    point[0] = isParent ? Consts.GOSAN : Consts.SABUROKU;
                    break;
                case Consts.RYAN_HAN:
                    point[0] = isParent ? Consts.PINMARUROKU : Consts.NANASENHYAKU;
                    break;
            }
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
