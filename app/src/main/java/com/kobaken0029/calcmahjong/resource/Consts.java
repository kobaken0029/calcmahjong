package com.kobaken0029.calcmahjong.resource;

public class Consts {
    /** プレイヤー数 */
    public final static int NUM_OF_PLAYER_YONMA = 4;
    public final static int NUM_OF_PLAYER_SANMA = 3;

    /** コード */
    public final static int REQUEST_CODE = 200;
    public final static int RYUKYOKU_CODE = 300;

    /** インタースティシャル広告ID */
    public final static String AD_UNIT_ID = "ca-app-pub-7932059771308904/6217972472";

    /** 順位 */
    public final static int TOP = 1;
    public final static int SECOND = 2;
    public final static int THIRD = 3;
    public final static int LAST = 4;

    /** 聴牌情報 */
    public final static int TENPAI_ONE = 1;
    public final static int TENPAI_TWO = 2;
    public final static int TENPAI_THREE = 3;

    /** Wikipedia項目名 */
    public final static String MAHJONG_YAKU_LIST = "麻雀役一覧";
    public final static String MAHJONG_POINT_LIST = "点数早見表";
    public final static String MAHJONG_RULES = "麻雀のルール";

    /** WikipediaURL */
    public final static String MAHJONG_YAKU_LIST_URL = "http://ja.wikipedia.org/wiki/%E9%BA%BB%E9%9B%80%E3%81%AE%E5%BD%B9%E4%B8%80%E8%A6%A7#.E4.B8.80.E9.A3.9C";
    public final static String MAHJONG_POINT_LIST_URL = "http://ja.wikipedia.org/wiki/%E9%BA%BB%E9%9B%80%E3%81%AE%E5%BE%97%E7%82%B9%E8%A8%88%E7%AE%97#.E7.82.B9.E6.95.B0.E3.81.AE.E6.97.A9.E8.A6.8B.E8.A1.A8";
    public final static String MAHJONG_RULES_URL = "http://ja.wikipedia.org/wiki/%E9%BA%BB%E9%9B%80%E3%81%AE%E3%83%AB%E3%83%BC%E3%83%AB";

    /** 音声URL */
    public final static String CALL_VOICE_1_URL = "http://www14.big.or.jp/~amiami/happy/download/voice/208_janken/ri-chi_01.wav";
    public final static String CALL_VOICE_2_URL = "http://majyo3com.ddo.jp/List.files/WAV/Ree00.wav";
    public final static String RYUKYOKU_VOICE_URL = "http://majyo3com.ddo.jp/List.files/WAV/Ryuukyoku00.wav";
    public final static String TUMO_VOICE_URL = "http://www14.big.or.jp/~amiami/happy/download/voice/208_janken/tsumo_01.wav";
    public final static String RON_VOICE_URL = "http://www14.big.or.jp/~amiami/happy/download/voice/208_janken/ron_01.wav";

    /** 遅延時間 */
    public final static int DELAY_TIME = 3000;

    /** 順位ウマ */
    public final static String GOTTO = "ゴットー";
    public final static String ONE_TWO = "ワンツー";
    public final static String ONE_THREE = "ワンスリー";
    public final static String TWO_THREE = "ツースリー";
    public final static int[] GOTTO_POINT = {5, 10};
    public final static int[] ONE_TWO_POINT = {10, 20};
    public final static int[] ONE_THREE_POINT = {10, 30};
    public final static int[] TWO_THREE_POINT = {20, 30};

    /** オカ */
    public final static int OKA_20000_30000 = 40;
    public final static int OKA_25000_30000 = 20;
    public final static int OKA_26000_30000 = 16;
    public final static int OKA_27000_30000 = 12;
    public final static int OKA_30000_30000 = 0;

    /** 配給原点 */
    public final static int DO_20000 = 20000;
    public final static int DO_25000 = 25000;
    public final static int DO_26000 = 26000;
    public final static int DO_27000 = 27000;
    public final static int DO_30000 = 30000;
    public final static int DO_35000 = 35000;
    public final static int DO_40000 = 40000;

    /** 原点 */
    public final static int O_30000 = 30000;
    public final static int O_40000 = 40000;

    /** 飜数 */
    public final static String I_HAN = "1翻";
    public final static String RYAN_HAN = "2翻";
    public final static String SAN_HAN = "3翻";
    public final static String SHU_HAN = "4翻";
    public final static String MANGAN = "5翻";
    public final static String HANEMAN = "6~7翻";
    public final static String BAIMAN = "8~10翻";
    public final static String SAN_BAIMAN = "11~12翻";
    public final static String YAKUMAN = "13翻以上";
    public final static String DOUBLE_YAKUMAN = "ダブル役満";

    /** 符数 */
    public final static String NIZYU_FU = "20符";
    public final static String NIZYUGO_FU = "25符";
    public final static String SANZYU_FU = "30符";
    public final static String YONZYU_FU = "40符";
    public final static String GOZYU_FU = "50符";
    public final static String ROKUZYU_FU = "60符";
    public final static String NANAZYU_FU = "70符";
    public final static String HATIZYU_FU = "80符";
    public final static String KYUZYU_FU = "90符";
    public final static String HYAKU_FU = "100符";
    public final static String HYAKUZYU_FU = "110符";

    /** 点数 */
    public final static int SCORE_25000 = 25000;

    /** 子 */
    public final static int SANBYAKU = 300;
    public final static int YONHYAKU = 400;
    public final static int GOHYAKU = 500;
    public final static int ROPPYAKU = 600;
    public final static int NANAHYAKU = 700;
    public final static int HAPPYAKU = 800;
    public final static int KYUHYAKU = 900;
    public final static int SEN = 1000;
    public final static int NISEN = 2000;
    public final static int ZANKU = 3900;
    public final static int TITTI = 7700;
    public final static int ITISAN = 1300;
    public final static int NINROKU = 2600;
    public final static int GOTU = 5200;
    public final static int ITIROKU = 1600;
    public final static int ZANNI = 3200;
    public final static int ROKUYON = 6400;
    public final static int NISAN = 2300;
    public final static int YONGO = 4500;
    public final static int NIKKU = 2900;
    public final static int GOPPA = 5800;
    public final static int SABUROKU = 3600;
    public final static int NANASENHYAKU = 7100;
    public final static int HASSE = 8000;
    public final static int ITIMANNISE = 12000;
    public final static int ITIMANROKUSE = 16000;
    public final static int NIMANYONSE = 24000;
    public final static int SANMANNISE = 32000;
    public final static int ROKUMANYONSE = 64000;


    /** 親 */
    public final static int ITIGO = 1500;
    public final static int PINPINROKU = 11600;
    public final static int NIYON = 2400;
    public final static int YONPA = 4800;
    public final static int KUNROKU = 9600;
    public final static int SANYON = 3400;
    public final static int ROPPA = 6800;
    public final static int YONYON = 4400;
    public final static int HATINANA = 8700;
    public final static int GOSAN = 5300;
    public final static int PINMARUROKU = 10600;
    public final static int ITIMANHASSE = 18000;
    public final static int SANMANROKUSE = 36000;
    public final static int YONMANHASSE = 48000;
    public final static int KYUMANROKUSE = 96000;

    public final static int SENNIHYAKU = 1200;
    public final static int SENHAPPYAKU = 1800;
    public final static int SANZE = 3000;
    public final static int YONSE = 4000;
    public final static int ROKUSE = 6000;
}
