package com.rased.quick;

import android.app.Activity; // <-- استخدمنا الواجهة الأساسية الخفيفة
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity { // <-- تم التعديل هنا
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView myWebView = new WebView(this);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        
        // رابط البوابة
        myWebView.loadUrl("https://lms.moe.gov.om/");
        setContentView(myWebView);
    }
}
