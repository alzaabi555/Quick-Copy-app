// هذا الكود سيتم حقنه في البوابة التعليمية
(function() {
    // التأكد من عدم تكرار الزر
    if (document.getElementById('rased-quick-btn')) return;

    // إنشاء زر "الراصد السريع" العائم
    var btn = document.createElement('button');
    btn.id = 'rased-quick-btn';
    btn.innerHTML = '🚀 الراصد السريع';
    btn.style.cssText = 'position: fixed; bottom: 20px; left: 20px; z-index: 999999; padding: 15px 20px; background-color: #4CAF50; color: white; border: none; border-radius: 50px; font-size: 16px; font-weight: bold; box-shadow: 0 4px 8px rgba(0,0,0,0.2); cursor: pointer;';
    
    btn.onclick = function() {
        alert('أهلاً بك في الراصد السريع! سيتم ربط ملف الإكسل هنا قريباً.');
        // هنا سيتم استدعاء دوال الرصد وتعبئة الدرجات
    };

    document.body.appendChild(btn);
    console.log('تم تشغيل كود الراصد السريع بنجاح!');
})();
