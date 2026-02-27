package com.rased.quick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. إنشاء خلفية الشاشة (بيضاء ونظيفة)
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(60, 60, 60, 60);
        layout.setBackgroundColor(0xFFFFFFFF);

        // 2. عنوان التطبيق
        TextView title = new TextView(this);
        title.setText("مرحباً بك في كيبورد الراصد السريع 🚀");
        title.setTextSize(22);
        title.setTextColor(0xFF00897B); // لون أخضر أنيق
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 40);

        // 3. تعليمات الاستخدام للمعلم
        TextView instructions = new TextView(this);
        instructions.setText("هذا التطبيق يعمل كلوحة مفاتيح (كيبورد) لتسهيل رصد الدرجات.\n\n" +
                "الخطوات:\n" +
                "1️⃣ اضغط على الزر بالأسفل لفتح الإعدادات.\n" +
                "2️⃣ قم بتفعيل (كيبورد الراصد السريع).\n" +
                "3️⃣ افتح ملف الإكسل وانسخ عمود الدرجات.\n" +
                "4️⃣ اذهب للبوابة التعليمية، اختر كيبورد الراصد واضغط الزر الأخضر!");
        instructions.setTextSize(16);
        instructions.setTextColor(0xFF424242);
        instructions.setLineSpacing(0, 1.4f);
        instructions.setPadding(0, 0, 0, 60);

        // 4. زر ذكي يفتح إعدادات الكيبورد في الهاتف مباشرة!
        Button btnEnable = new Button(this);
        btnEnable.setText("⚙️ تفعيل الكيبورد من الإعدادات");
        btnEnable.setBackgroundColor(0xFF00897B);
        btnEnable.setTextColor(0xFFFFFFFF);
        btnEnable.setTextSize(16);
        btnEnable.setPadding(30, 30, 30, 30);

        btnEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // أمر برمجي يفتح شاشة "إدارة لوحات المفاتيح" في نظام أندرويد
                Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                startActivity(intent);
            }
        });

        // 5. تجميع العناصر وعرضها
        layout.addView(title);
        layout.addView(instructions);
        layout.addView(btnEnable);

        setContentView(layout);
    }
}
