package com.rased.quick;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.inputmethodservice.InputMethodService;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
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
        // 1. خلفية الكيبورد (Dark Mode فخم)
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(0xFF1C1C1E); // لون أيفون الداكن
        rootLayout.setPadding(25, 30, 25, 30);

        // 2. الزر الرئيسي للرصد (أخضر زاهي مع حواف ناعمة)
        Button btnRased = new Button(this);
        btnRased.setText("🚀 بدء الرصد التلقائي للدرجات");
        btnRased.setTextSize(18);
        btnRased.setTextColor(0xFFFFFFFF);
        btnRased.setAllCaps(false); // لمنع تكبير الحروف الإنجليزية إن وجدت
        btnRased.setTypeface(null, android.graphics.Typeface.BOLD);
        btnRased.setBackground(createPremiumButton(0xFF34C759)); // Apple Green

        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 220
        );
        paramsMain.setMargins(0, 0, 0, 25);
        btnRased.setLayoutParams(paramsMain);
        
        btnRased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAutoFill();
            }
        });

        // 3. تخطيط الأزرار السفلية (أفقي)
        LinearLayout utilLayout = new LinearLayout(this);
        utilLayout.setOrientation(LinearLayout.HORIZONTAL);
        utilLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // 4. زر العودة للكيبورد العادي
        Button btnSwitch = new Button(this);
        btnSwitch.setText("🌐 لوحة المفاتيح");
        btnSwitch.setTextColor(0xFFFFFFFF);
        btnSwitch.setTextSize(15);
        btnSwitch.setBackground(createPremiumButton(0xFF3A3A3C)); // لون رمادي فاخر
        
        LinearLayout.LayoutParams paramsSwitch = new LinearLayout.LayoutParams(
                0, 150, 1.0f
        );
        paramsSwitch.setMargins(0, 0, 15, 0); // مسافة بين الزرين
        btnSwitch.setLayoutParams(paramsSwitch);
        
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showInputMethodPicker();
                }
            }
        });

        // 5. زر الإغلاق
        Button btnHide = new Button(this);
        btnHide.setText("⌨️ إخفاء");
        btnHide.setTextColor(0xFFFFFFFF);
        btnHide.setTextSize(15);
        btnHide.setBackground(createPremiumButton(0xFF3A3A3C));
        
        LinearLayout.LayoutParams paramsHide = new LinearLayout.LayoutParams(
                0, 150, 1.0f
        );
        btnHide.setLayoutParams(paramsHide);
        
        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestHideSelf(0);
            }
        });

        // تجميع العناصر
        utilLayout.addView(btnSwitch);
        utilLayout.addView(btnHide);

        rootLayout.addView(btnRased);
        rootLayout.addView(utilLayout);

        return rootLayout;
    }

    // دالة سحرية لصنع أزرار بحواف دائرية وتصميم مسطح (Flat Design)
    private GradientDrawable createPremiumButton(int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(40f); // حواف دائرية جداً وناعمة
        shape.setColor(color);
        return shape;
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
                        Toast.makeText(RasedKeyboardService.this, "✅ اكتمل الرصد بنجاح للجميع!", Toast.LENGTH_LONG).show();
                        requestHideSelf(0);
                    }
                }
            }, i * 200); 
        }
    }
}
