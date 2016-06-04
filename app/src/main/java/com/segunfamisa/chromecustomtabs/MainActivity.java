package com.segunfamisa.chromecustomtabs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity{

    Button buttonWebView;
    Button buttonBrowser;
    Button buttonChromeTab;
    EditText editUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = editUrl.getText().toString();

            if (validateUrl(url)) {

                if (v == buttonWebView) {
                    // TODO: 04/06/2016 handle webview
                } else if (v == buttonBrowser) {
                    // TODO: 04/06/2016 handle browser
                } else if (v == buttonChromeTab) {
                    // TODO: 04/06/2016 handle chrome custom tab
                }
            }
        }
    };

    private boolean validateUrl(String url) {


        return true;
    }
}
