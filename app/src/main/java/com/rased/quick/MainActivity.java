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
import android.widget.Space;
import android.graphics.drawable.GradientDrawable;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. التخطيط الرئيسي
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setPadding(60, 100, 60, 40);
        layout.setBackgroundColor(0xFFF8F9FA); // لون أبيض ثلجي مريح

        // 2. عنوان التطبيق الفخم
        TextView title = new TextView(this);
        title.setText("الراصد السريع 🚀");
        title.setTextSize(28);
        title.setTextColor(0xFF1E293B); // أزرق داكن جداً (احترافي)
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, 40);

        // 3. تعليمات الاستخدام
        TextView instructions = new TextView(this);
        instructions.setText("أداة احترافية مصممة خصيصاً لتسهيل رصد الدرجات في البوابة التعليمية بضغطة زر واحدة.\n\n" +
                "الخطوات:\n" +
                "1️⃣ اضغط على الزر أدناه لفتح الإعدادات.\n" +
                "2️⃣ قم بتفعيل (كيبورد الراصد السريع).\n" +
                "3️⃣ انسخ الدرجات من ملف الإكسل.\n" +
                "4️⃣ افتح البوابة واضغط زر الرصد في الكيبورد.");
        instructions.setTextSize(16);
        instructions.setTextColor(0xFF475569);
        instructions.setLineSpacing(0, 1.4f);
        instructions.setPadding(0, 0, 0, 80);

        // 4. زر التفعيل بحواف دائرية أنيقة
        Button btnEnable = new Button(this);
        btnEnable.setText("⚙️ تفعيل الكيبورد من الإعدادات");
        btnEnable.setTextColor(0xFFFFFFFF);
        btnEnable.setTextSize(16);
        
        GradientDrawable btnShape = new GradientDrawable();
        btnShape.setShape(GradientDrawable.RECTANGLE);
        btnShape.setCornerRadius(50f); // حواف دائرية
        btnShape.setColor(0xFF2563EB); // أزرق ملكي
        btnEnable.setBackground(btnShape);
        
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 150
        );
        btnEnable.setLayoutParams(btnParams);

        btnEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                startActivity(intent);
            }
        });

        // 5. مساحة مرنة لدفع التوقيع للأسفل
        Space space = new Space(this);
        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f
        );
        space.setLayoutParams(spaceParams);

        // 6. توقيع المطور الفخم (alzaabi555)
        TextView developerCredit = new TextView(this);
        developerCredit.setText("✨ تم التطوير بكل فخر بواسطة\nAlzaabi555");
        developerCredit.setTextSize(14);
        developerCredit.setTextColor(0xFF94A3B8); // رمادي أنيق
        developerCredit.setGravity(Gravity.CENTER);
        developerCredit.setTypeface(null, android.graphics.Typeface.ITALIC);

        // تجميع العناصر
        layout.addView(title);
        layout.addView(instructions);
        layout.addView(btnEnable);
        layout.addView(space); // المساحة المرنة
        layout.addView(developerCredit); // التوقيع في الأسفل

        setContentView(layout);
    }
}
