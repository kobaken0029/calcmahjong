package eval.wit.ai.calcmahjong.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import eval.wit.ai.calcmahjong.R;
import eval.wit.ai.calcmahjong.resources.Consts;

public class WikipediaWebViewActivity extends ActionBarActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wikipedia_web_view);

        String menu = getIntent().getStringExtra("menu");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_web);
        toolbar.setTitle(menu);
        toolbar.setBackgroundColor(getResources().getColor(R.color.action_bar));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        webView = (WebView) findViewById(R.id.wikipedia_view);
//        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        switch (menu) {
            case Consts.MAHJONG_YAKU_LIST:
//                webView.loadUrl(Consts.MAHJONG_YAKU_LIST_URL);
                webView.loadUrl("file:///android_asset/mahjong_yaku_list_2.html");
                break;
            case Consts.MAHJONG_POINT_LIST:
//                webView.loadUrl(Consts.MAHJONG_POINT_LIST_URL);
                webView.loadUrl("file:///android_asset/mahjong_point_list.html");
                break;
            case Consts.MAHJONG_RULES:
                webView.loadUrl(Consts.MAHJONG_RULES_URL);
                break;
            default:
                break;
        }


        // 広告バナーを表示
        ((AdView)this.findViewById(R.id.adView)).loadAd(new AdRequest.Builder()
                .addTestDevice("21499EE04196C2E0E48CB407366D501F")
                .build());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
