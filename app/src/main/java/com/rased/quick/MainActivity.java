package com.rased.quick;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView myWebView = new WebView(this);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // هنا يكمن السحر: إعداد عميل الويب ليقوم بحقن الكود بعد تحميل الصفحة
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // كود جافاسكربت لإخفاء الخيارات الزائدة وتوسيط خيار الحاسوب
                String jsCode = "javascript:(function() { " +
                        "var boxes = document.querySelectorAll('.boxContainer .box'); " +
                        "if(boxes.length >= 3) { " +
                        "   boxes[1].style.display = 'none'; " + // إخفاء البطاقة الذكية
                        "   boxes[2].style.display = 'none'; " + // إخفاء الهاتف الذكي
                        "} " +
                        "var container = document.querySelector('.boxContainer'); " +
                        "if(container) { " +
                        "   container.style.display = 'flex'; " +
                        "   container.style.justifyContent = 'center'; " + // توسيط المربع المتبقي
                        "} " +
                        "})()";
                
                // تنفيذ الكود داخل الصفحة
                view.evaluateJavascript(jsCode, null);
            }
        });
        
        // رابط البوابة التعليمية
        myWebView.loadUrl("https://eportal.moe.gov.om/");
        setContentView(myWebView);
    }
}
