package com.rased.quick;

import android.content.ClipboardManager;
import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RasedKeyboardService extends InputMethodService {

    @Override
    public View onCreateInputView() {
        // تصميم شكل الكيبورد: زر واحد كبير وواضح
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0xFFECEFF1); // لون خلفية هادئ

        Button btnRased = new Button(this);
        btnRased.setText("🚀 رصد الدرجات المنسوخة (ضغطة واحدة)");
        btnRased.setTextSize(18);
        btnRased.setBackgroundColor(0xFF4CAF50); // لون أخضر مميز
        btnRased.setTextColor(0xFFFFFFFF);

        // أبعاد الزر
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 250
        );
        params.setMargins(20, 20, 20, 20);
        btnRased.setLayoutParams(params);

        // ماذا يحدث عند الضغط على الزر؟
        btnRased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAutoFill();
            }
        });

        layout.addView(btnRased);
        return layout;
    }

    private void startAutoFill() {
        // 1. قراءة الدرجات من الحافظة (النص المنسوخ من الإكسل)
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (!clipboard.hasPrimaryClip() || clipboard.getPrimaryClip().getItemCount() == 0) {
            Toast.makeText(this, "⚠️ الحافظة فارغة! انسخ الدرجات من الإكسل أولاً.", Toast.LENGTH_LONG).show();
            return;
        }

        String copiedText = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
        // تقسيم النص بناءً على النزول لسطر جديد (كل درجة في سطر)
        final String[] grades = copiedText.split("\\r?\\n"); 

        if (grades.length == 0 || grades[0].trim().isEmpty()) {
            Toast.makeText(this, "⚠️ لم يتم العثور على درجات صالحة!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "⏳ جاري رصد " + grades.length + " درجة تلقائياً...", Toast.LENGTH_SHORT).show();

        // 2. عملية الحقن المتسلسل (اللوب)
        // نستخدم Handler لوضع تأخير زمني بسيط (150 جزء من الثانية) بين كل طالب 
        // لكي نعطي البوابة التعليمية وقتاً لاستيعاب الدرجة والانتقال للمربع التالي
        Handler handler = new Handler(Looper.getMainLooper());
        for (int i = 0; i < grades.length; i++) {
            final String grade = grades[i].trim();
            final int index = i;
            
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!grade.isEmpty()) {
                        InputConnection ic = getCurrentInputConnection();
                        if (ic != null) {
                            // كتابة الدرجة
                            ic.commitText(grade, 1);
                            // محاكاة ضغطة زر Tab للانتقال للمربع التالي
                            ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB));
                            ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_TAB));
                        }
                    }
                    // إشعار عند الانتهاء من جميع الطلاب
                    if (index == grades.length - 1) {
                        Toast.makeText(RasedKeyboardService.this, "✅ السحر اكتمل! تمت إضافة جميع الدرجات.", Toast.LENGTH_LONG).show();
                    }
                }
            }, i * 150); // 150 ملي ثانية تأخير (يمكنك تقليلها لاحقاً إذا كانت البوابة سريعة)
        }
    }
}
