package technibits.com.pme.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import technibits.com.pme.R;

/**
 * Created by technibitsuser on 7/11/2015.
 */
public class WebviewActivity extends Activity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vimeo_webview);
        String video_id="24577973"; //getIntent().getExtras().getString("video_id");
        String Url="http://player.vimeo.com/video/"+video_id+"?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1&maxheight=480&maxwidth=800";
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUserAgentString("Android Mozilla/5.0 AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
//        webView.setWebChromeClient(new WebChromeClient());
//        webView.setWebViewClient(new WebViewClient());

// how plugin is enabled change in API 8

//            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        webView.loadUrl(Url);
//        webView.loadUrl("file:///android_asset/playground.html");
        webView.loadUrl("file:///android_asset/jwtest.html");
//        String venkat="<iframe src=\"http://player.vimeo.com/video/24577973?portrait=0&color=333\" width=\"WIDTH\" height=\"HEIGHT\" frameborder=\"0\" webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe>";
//        webView.loadData(venkat,"text/html","UTF-8");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }
    }
