package eval.wit.ai.calcmahjong.resources;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by koba on 2015/01/26.
 */
public class ConstsManager {

    /**
     * 飜数のアダプターを取得。
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
}
