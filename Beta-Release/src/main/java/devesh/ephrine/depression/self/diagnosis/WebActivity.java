package devesh.ephrine.depression.self.diagnosis;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class WebActivity extends AppCompatActivity {
    public boolean installed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        isAppInstalled("devesh.ephrine.depression.self.diagnosis.pro");


        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.option);

        isNetworkAvailable();

        if (installed == true) {

        } else {
            MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));
            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }


        WebView myWebView = (WebView) findViewById(R.id.webView);
        if (message.equals("f")) {
            myWebView.loadUrl("http://admob-app-id-7252308654.firebaseapp.com/dp/treatment");

        } else if (message.equals("w")) {
            myWebView.loadUrl("http://admob-app-id-7252308654.firebaseapp.com/dp");

        } else if (message.equals("c")) {
            myWebView.loadUrl("http://goo.gl/forms/fpuYFy9wWubW6QK12"); //contact form

        }
        //  myWebView.loadUrl("https://admob-app-id-7252308654.firebaseapp.com/dp");

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.getSettings().setBuiltInZoomControls(true);


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null) {

            LinearLayout net = (LinearLayout) findViewById(R.id.net);
            net.setVisibility(View.INVISIBLE);

        }

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();


    }

    private boolean isAppInstalled(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    private class MyWebViewClient extends WebViewClient {


        LinearLayout loading = (LinearLayout) findViewById(R.id.loading);

        /*      @Override
              public boolean shouldOverrideUrlLoading(WebView view, String url) {

                  if (Uri.parse(url).getHost().equals("microsoft.com")) {
                      // This is my web site, so do not override; let my WebView load the page

                      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                      startActivity(intent);
                      return false;
                  }


                  return true;
              }
      */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            loading.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            loading.setVisibility(View.GONE);
            super.onPageFinished(view, url);


        }


    }
}
