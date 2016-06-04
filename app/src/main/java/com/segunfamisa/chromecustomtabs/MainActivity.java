package com.segunfamisa.chromecustomtabs;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.segunfamisa.chromecustomtabs.shared.CustomTabsHelper;

public class MainActivity extends AppCompatActivity{

    Button buttonWebView;
    Button buttonBrowser;
    Button buttonChromeTab;
    EditText editUrl;

    private CustomTabActivityHelper mCustomTabActivityHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomTabActivityHelper = new CustomTabActivityHelper();

        editUrl = (EditText) findViewById(R.id.edit_url);
        buttonWebView = (Button) findViewById(R.id.button_webview);
        buttonBrowser = (Button) findViewById(R.id.button_external);
        buttonChromeTab = (Button) findViewById(R.id.button_chrome_custom_tab);


        buttonWebView.setOnClickListener(buttonClickListener);
        buttonBrowser.setOnClickListener(buttonClickListener);
        buttonChromeTab.setOnClickListener(buttonClickListener);

        //set selection to the last index of editurl
        editUrl.setSelection(editUrl.getText().toString().length());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCustomTabActivityHelper.bindCustomTabsService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCustomTabActivityHelper.unbindCustomTabsService(this);
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = editUrl.getText().toString();

            if (validateUrl(url)) {
                Uri uri = Uri.parse(url);
                if (uri != null) {
                    if (v == buttonWebView) {
                        openWebView(uri);
                    } else if (v == buttonBrowser) {
                        openInExternalBrowser(uri);
                    } else if (v == buttonChromeTab) {
                        openCustomChromeTab(uri);
                    }
                }
            }
        }
    };

    private boolean validateUrl(String url) {
        return url != null && url.length() > 0;
    }

    /**
     * Handles opening the url in a custom chrome tab
     * @param uri
     */
    private void openCustomChromeTab(Uri uri) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        // set toolbar colors
        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        // add menu items
        intentBuilder.addMenuItem(getString(R.string.title_menu_1),
                createPendingIntent(ChromeTabActionBroadcastReceiver.ACTION_MENU_ITEM_1));
        intentBuilder.addMenuItem(getString(R.string.title_menu_2),
                createPendingIntent(ChromeTabActionBroadcastReceiver.ACTION_MENU_ITEM_2));

        // set start and exit animations
        intentBuilder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

        // build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();

        // call helper to open custom tab
        CustomTabActivityHelper.openCustomTab(this, customTabsIntent, uri, new CustomTabActivityHelper.CustomTabFallback() {
            @Override
            public void openUri(Activity activity, Uri uri) {
                // fall back, call open open webview
                openWebView(uri);
            }
        });
    }

    /**
     * Handles opening the url in a webview
     * @param uri
     */
    private void openWebView(Uri uri) {
        Intent webViewIntent = new Intent(this, WebViewActivity.class);
        webViewIntent.putExtra(WebViewActivity.EXTRA_URL, uri.toString());
        startActivity(webViewIntent);
    }

    /**
     * Handles opening the url in an external browser
     *
     * @param uri
     */
    private void openInExternalBrowser(Uri uri) {
        Intent externalIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(externalIntent);
    }

    /**
     * Creates a pending intent to send a broadcast to the {@link ChromeTabActionBroadcastReceiver}
     * @param actionSource
     * @return
     */
    private PendingIntent createPendingIntent(int actionSource) {
        Intent actionIntent = new Intent(this, ChromeTabActionBroadcastReceiver.class);
        actionIntent.putExtra(ChromeTabActionBroadcastReceiver.KEY_ACTION_SOURCE, actionSource);
        return PendingIntent.getBroadcast(this, actionSource, actionIntent, 0);
    }
}
