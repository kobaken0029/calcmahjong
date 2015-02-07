package eval.wit.ai.calcmahjong.activities;

import android.app.Activity;
import android.os.Bundle;

import eval.wit.ai.calcmahjong.dialogs.RyukyokuDialog;

/**
 * Created by koba on 2015/02/07.
 */
public class RyukyokuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RyukyokuDialog().showDialog(RyukyokuActivity.this, this);
    }
}
