package com.segunfamisa.chromecustomtabs;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class WebViewActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "extra.url";

    private WebView mWebView;
    private ProgressBar mProgressLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        String url = getIntent().getStringExtra(EXTRA_URL);

        mWebView = (WebView) findViewById(R.id.webview_content);
        mProgressLoading = (ProgressBar) findViewById(R.id.progress_loading);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                setPageLoadProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl(url);

        // set title
        setTitle(url);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setPageLoadProgress(int progress) {
        mProgressLoading.setProgress(progress);
    }

    private void showProgress() {
        mProgressLoading.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mProgressLoading.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Private webview client to handle visibility of progress
     */
    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showProgress();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            hideProgress();
            super.onPageFinished(view, url);
        }
    }
}
