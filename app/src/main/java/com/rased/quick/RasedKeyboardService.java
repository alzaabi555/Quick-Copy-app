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
        // تصميم لوحة المفاتيح: زر واحد ضخم وواضح
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0xFFE0F2F1); // لون خلفية مريح للعين

        Button btnRased = new Button(this);
        btnRased.setText("🚀 اضغط هنا لرصد الدرجات المنسوخة تلقائياً");
        btnRased.setTextSize(18);
        btnRased.setBackgroundColor(0xFF00897B); // أخضر أنيق
        btnRased.setTextColor(0xFFFFFFFF);

        // أبعاد الزر ليملأ منطقة الكيبورد
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 250
        );
        params.setMargins(15, 15, 15, 15);
        btnRased.setLayoutParams(params);

        // عند الضغط على الزر، تبدأ عملية الرصد
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
        // 1. جلب الدرجات التي نسخها المعلم من الإكسل
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null || !clipboard.hasPrimaryClip() || clipboard.getPrimaryClip().getItemCount() == 0) {
            Toast.makeText(this, "⚠️ الذاكرة فارغة! انسخ الدرجات من الإكسل أولاً.", Toast.LENGTH_LONG).show();
            return;
        }

        String copiedText = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
        // تقسيم النص بناءً على الأسطر (كل درجة في سطر)
        final String[] grades = copiedText.split("\\r?\\n"); 

        if (grades.length == 0 || grades[0].trim().isEmpty()) {
            Toast.makeText(this, "⚠️ لم يتم العثور على أرقام صالحة!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "⏳ جاري رصد " + grades.length + " درجة... لا تلمس الشاشة!", Toast.LENGTH_SHORT).show();

        // 2. عملية اللصق المتسلسل (اللوب السحري)
        Handler handler = new Handler(Looper.getMainLooper());
        for (int i = 0; i < grades.length; i++) {
            final String grade = grades[i].trim();
            final int index = i;
            
            // نضع تأخير 200 ملي ثانية بين كل درجة لكي يستوعب المتصفح الانتقال
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!grade.isEmpty()) {
                        InputConnection ic = getCurrentInputConnection();
                        if (ic != null) {
                            // أ. كتابة الدرجة
                            ic.commitText(grade, 1);
                            
                            // ب. محاكاة ضغطة زر Tab للانتقال للمربع التالي تلقائياً
                            ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB));
                            ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_TAB));
                        }
                    }
                    
                    // إشعار النجاح في النهاية
                    if (index == grades.length - 1) {
                        Toast.makeText(RasedKeyboardService.this, "✅ اكتمل الرصد بنجاح للجميع!", Toast.LENGTH_LONG).show();
                    }
                }
            }, i * 200); 
        }
    }
}
