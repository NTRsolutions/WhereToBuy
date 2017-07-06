package com.mainframevampire.ryan.wheretobuy.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.mainframevampire.ryan.wheretobuy.R;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        WebView webView = (WebView) findViewById(R.id.webview);
        Intent intent = getIntent();
        String webUrl = intent.getStringExtra(ProductDetailFragment.PRODUCT_URL);
        webView.loadUrl(webUrl);
    }
}
