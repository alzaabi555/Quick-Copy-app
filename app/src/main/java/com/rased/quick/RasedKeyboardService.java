package com.rased.quick;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.inputmethodservice.InputMethodService;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RasedKeyboardService extends InputMethodService {

    @Override
    public View onCreateInputView() {
        // 1. الخلفية الرئيسية للكيبورد (Dark Mode احترافي)
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(0xFF1C1C1E); // لون كيبورد أيفون الداكن
        // تحديد ارتفاع ثابت للكيبورد ليكون طبيعياً (حوالي 260dp)
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(260)
        ));

        // 2. شريط الأدوات العلوي (يحتوي على أزرار الإغلاق والتبديل)
        LinearLayout topBar = new LinearLayout(this);
        topBar.setOrientation(LinearLayout.HORIZONTAL);
        topBar.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        topBar.setPadding(dp(10), dp(10), dp(10), dp(0));
        topBar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        Button btnSwitch = createIconButton("🌐 لوحة المفاتيح");
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) imm.showInputMethodPicker();
            }
        });

        Button btnHide = createIconButton("⬇️ إغلاق");
        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestHideSelf(0);
            }
        });

        // إضافة مسافة مرنة لدفع الأزرار لليمين أو اليسار
        View spacer = new View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1.0f));

        topBar.addView(btnSwitch);
        topBar.addView(spacer);
        topBar.addView(btnHide);

        // 3. منطقة المنتصف (تحتوي على زر الرصد الأنيق)
        LinearLayout centerArea = new LinearLayout(this);
        centerArea.setOrientation(LinearLayout.VERTICAL);
        centerArea.setGravity(Gravity.CENTER);
        centerArea.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        ));

        Button btnRased = new Button(this);
        btnRased.setText("🚀 بدء الرصد التلقائي");
        btnRased.setTextSize(16);
        btnRased.setTextColor(0xFFFFFFFF);
        btnRased.setAllCaps(false);
        btnRased.setTypeface(null, android.graphics.Typeface.BOLD);
        btnRased.setBackground(createPremiumButton(0xFF34C759, dp(30))); // أخضر أبل مع حواف دائرية بالكامل

        // حجم الزر الآن احترافي (ليس كبيراً جداً ولا صغيراً)
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                dp(280), dp(55)
        );
        btnRased.setLayoutParams(btnParams);
        btnRased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAutoFill();
            }
        });

        // نص إرشادي أنيق تحت الزر
        TextView statusText = new TextView(this);
        statusText.setText("ضع المؤشر في أول مربع طالب، ثم اضغط الزر");
        statusText.setTextColor(0xFF8E8E93); // رمادي فاتح
        statusText.setTextSize(13);
        statusText.setGravity(Gravity.CENTER);
        statusText.setPadding(0, dp(15), 0, 0);

        centerArea.addView(btnRased);
        centerArea.addView(statusText);

        // 4. تجميع كل القطع
        rootLayout.addView(topBar);
        rootLayout.addView(centerArea);

        return rootLayout;
    }

    // دالة مساعدة لتحويل المقاسات لتبدو متطابقة في كل الشاشات
    private int dp(int value) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics()
        );
    }

    // دالة لصنع الأزرار العلوية الصغيرة
    private Button createIconButton(String text) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setTextColor(0xFFFFFFFF);
        btn.setTextSize(13);
        btn.setAllCaps(false);
        btn.setBackground(createPremiumButton(0xFF3A3A3C, dp(10))); // حواف دائرية خفيفة
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, dp(40)
        );
        params.setMargins(dp(5), 0, dp(5), 0);
        btn.setLayoutParams(params);
        btn.setPadding(dp(15), 0, dp(15), 0);
        return btn;
    }

    // دالة لصنع الحواف الدائرية
    private GradientDrawable createPremiumButton(int color, int radius) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(radius);
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
