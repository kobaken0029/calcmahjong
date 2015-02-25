package eval.wit.ai.calcmahjong.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by koba on 2015/01/26.
 */
public class Consts {
    /** プレイヤー数 */
    public final static int NUM_OF_PLAYER_YONMA = 4;
    public final static int NUM_OF_PLAYER_SANMA = 3;

    /** コード */
    public final static int REQUEST_CODE = 200;
    public final static int RYUKYOKU_CODE = 300;

    /** 最大・最小本場数 */
    public final static int MAX_NUMBER_OF_HONBA = 7;
    public final static int MIN_NUMBER_OF_HONBA = 0;

    /** 順位 */
    public final static int TOP = 1;
    public final static int SECONDE = 2;
    public final static int THIRD = 3;
    public final static int LAST = 4;

    /** 聴牌情報 */
    public final static int TENPAI_ONE = 1;
    public final static int TENPAI_TWO = 2;
    public final static int TENPAI_THREE = 3;

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
    public final static String DO_20000 = "20000";
    public final static String DO_25000 = "25000";
    public final static String DO_26000 = "26000";
    public final static String DO_27000 = "27000";
    public final static String DO_30000 = "30000";

    /** 飜数 */
    public final static String I_HAN = "1翻";
    public final static String RYAN_HAN = "2翻";
    public final static String SAN_HAN = "3翻";
    public final static String SHU_HAN = "4翻";
    public final static String MANGAN = "5翻";
    public final static String HANEMAN = "6~7翻";
    public final static String BAIMAN = "8~10翻";
    public final static String SAN_BAIMAN = "11~12翻";
    public final static String YAKUMAN = "13翻";
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
