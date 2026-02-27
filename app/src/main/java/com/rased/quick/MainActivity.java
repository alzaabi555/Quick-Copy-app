package com.rased.quick;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.InputStream;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView myWebView = new WebView(this);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // تفعيل النوافذ المنبثقة (Alerts)
        myWebView.setWebChromeClient(new WebChromeClient()); 

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // 1. تجميل صفحة الدخول (إخفاء البطاقة والهاتف)
                String uiCleanUpJs = "javascript:(function() { " +
                        "var boxes = document.querySelectorAll('.boxContainer .box'); " +
                        "if(boxes.length >= 3) { boxes[1].style.display = 'none'; boxes[2].style.display = 'none'; } " +
                        "var container = document.querySelector('.boxContainer'); " +
                        "if(container) { container.style.display = 'flex'; container.style.justifyContent = 'center'; } " +
                        "})()";
                view.evaluateJavascript(uiCleanUpJs, null);

                // 2. حقن إضافة الراصد السريع
                String rasedJs = readAssetFile("rased.js");
                if (!rasedJs.isEmpty()) {
                    view.evaluateJavascript("javascript:" + rasedJs, null);
                }
            }
        });
        
        // الرابط الصحيح الذي سيعمل عليه سحر الراصد!
        myWebView.loadUrl("https://moe.gov.om/Portal/Services/UserLoginnew.aspx");
        setContentView(myWebView);
    }

    private String readAssetFile(String filename) {
        try {
            InputStream is = getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
