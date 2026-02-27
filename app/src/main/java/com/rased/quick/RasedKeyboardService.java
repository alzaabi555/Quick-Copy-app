package com.rased.quick;

import android.content.ClipboardManager;
import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RasedKeyboardService extends InputMethodService {

    @Override
    public View onCreateInputView() {
        // 1. التخطيط الرئيسي (عمودي)
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(0xFFECEFF1);
        rootLayout.setPadding(15, 15, 15, 15);

        // 2. زر الرصد السريع (العملاق)
        Button btnRased = new Button(this);
        btnRased.setText("🚀 رصد الدرجات المنسوخة");
        btnRased.setTextSize(18);
        btnRased.setBackgroundColor(0xFF00897B); // أخضر أنيق
        btnRased.setTextColor(0xFFFFFFFF);
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200
        );
        paramsMain.setMargins(0, 0, 0, 15); // مسافة سفلية
        btnRased.setLayoutParams(paramsMain);
        
        btnRased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAutoFill();
            }
        });

        // 3. تخطيط أزرار التحكم (أفقي)
        LinearLayout utilLayout = new LinearLayout(this);
        utilLayout.setOrientation(LinearLayout.HORIZONTAL);
        utilLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // 4. زر إغلاق الكيبورد
        Button btnHide = new Button(this);
        btnHide.setText("⬇️ إغلاق");
        btnHide.setBackgroundColor(0xFF607D8B); // رمادي غامق
        btnHide.setTextColor(0xFFFFFFFF);
        LinearLayout.LayoutParams paramsHide = new LinearLayout.LayoutParams(
                0, 140, 1.0f
        );
        paramsHide.setMargins(0, 0, 10, 0); // مسافة يسارية
        btnHide.setLayoutParams(paramsHide);
        
        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestHideSelf(0); // أمر برمجي يخفي الكيبورد فوراً
            }
        });

        // 5. زر تغيير الكيبورد (للعودة للكتابة العادية)
        Button btnSwitch = new Button(this);
        btnSwitch.setText("🌐 كيبورد الهاتف");
        btnSwitch.setBackgroundColor(0xFF546E7A);
        btnSwitch.setTextColor(0xFFFFFFFF);
        LinearLayout.LayoutParams paramsSwitch = new LinearLayout.LayoutParams(
                0, 140, 1.0f
        );
        btnSwitch.setLayoutParams(paramsSwitch);
        
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // أمر برمجي يفتح نافذة اختيار كيبورد النظام
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showInputMethodPicker();
                }
            }
        });

        // تجميع العناصر
        utilLayout.addView(btnHide);
        utilLayout.addView(btnSwitch);

        rootLayout.addView(btnRased);
        rootLayout.addView(utilLayout);

        return rootLayout;
    }

    private void startAutoFill() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null || !clipboard.hasPrimaryClip() || clipboard.getPrimaryClip().getItemCount() == 0) {
            Toast.makeText(this, "⚠️ الذاكرة فارغة! انسخ الدرجات من الإكسل أولاً.", Toast.LENGTH_LONG).show();
            return;
        }

        String copiedText = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
        final String[] grades = copiedText.split("\\r?\\n"); 

        if (grades.length == 0 || grades[0].trim().isEmpty()) {
            Toast.makeText(this, "⚠️ لم يتم العثور على أرقام صالحة!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "⏳ جاري رصد " + grades.length + " درجة... لا تلمس الشاشة!", Toast.LENGTH_SHORT).show();

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
                            ic.commitText(grade, 1);
                            ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB));
                            ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_TAB));
                        }
                    }
                    
                    if (index == grades.length - 1) {
                        Toast.makeText(RasedKeyboardService.this, "✅ اكتمل الرصد بنجاح!", Toast.LENGTH_LONG).show();
                        requestHideSelf(0); // السحر: إغلاق الكيبورد تلقائياً بعد الانتهاء من الرصد!
                    }
                }
            }, i * 200); 
        }
    }
}
