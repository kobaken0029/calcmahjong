package eval.wit.ai.calcmahjong.models.clients;

import android.app.Application;
import android.content.Context;

/**
 * Created by koba on 2015/01/25.
 */
public class AppController extends Application {
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
