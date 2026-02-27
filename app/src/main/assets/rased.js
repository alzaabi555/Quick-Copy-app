(function() {
    // 1. منع تكرار ظهور اللوحة إذا تم حقن الكود مرتين
    if (document.getElementById('rased-magic-panel')) return;

    // 2. السحر الأول: استدعاء مكتبة SheetJS لقراءة الإكسل من الإنترنت
    var script = document.createElement('script');
    script.src = 'https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.18.5/xlsx.full.min.js';
    document.head.appendChild(script);

    // 3. تصميم لوحة التحكم العائمة والاحترافية
    var panel = document.createElement('div');
    panel.id = 'rased-magic-panel';
    panel.innerHTML = `
        <div style="position: fixed; bottom: 20px; right: 20px; z-index: 999999; background: #ffffff; border-radius: 12px; box-shadow: 0 8px 24px rgba(0,0,0,0.15); padding: 15px; width: 280px; font-family: Tahoma, Arial, sans-serif; direction: rtl; border: 2px solid #00897B;">
            <h3 style="margin-top: 0; margin-bottom: 15px; color: #00897B; text-align: center; font-size: 18px;">🚀 الراصد السريع</h3>
            
            <input type="file" id="excel-upload" accept=".xlsx, .xls" style="display: none;" />
            
            <button id="btn-upload" style="width: 100%; padding: 12px; background: #039BE5; color: white; border: none; border-radius: 6px; font-weight: bold; font-size: 14px; margin-bottom: 10px; cursor: pointer;">1. 📂 اختر ملف الإكسل</button>
            
            <button id="btn-fill" style="width: 100%; padding: 12px; background: #43A047; color: white; border: none; border-radius: 6px; font-weight: bold; font-size: 14px; cursor: pointer; opacity: 0.5;" disabled>2. ✨ ابدأ الرصد التلقائي</button>
            
            <div id="rased-status" style="margin-top: 12px; font-size: 12px; color: #555; text-align: center; font-weight: bold;">بانتظار رفع ملف الدرجات...</div>
        </div>
    `;
    document.body.appendChild(panel);

    // 4. متغيرات لحفظ البيانات
    var excelData = [];

    // 5. برمجة الأزرار (العمليات)
    
    // عند الضغط على زر اختيار الملف
    document.getElementById('btn-upload').onclick = function() {
        document.getElementById('excel-upload').click();
    };

    // عند اختيار ملف الإكسل
    document.getElementById('excel-upload').onchange = function(e) {
        var file = e.target.files[0];
        if (!file) return;
        
        document.getElementById('rased-status').innerText = '⏳ جاري القراءة...';
        document.getElementById('rased-status').style.color = '#E65100';
        
        var reader = new FileReader();
        reader.onload = function(e) {
            var data = new Uint8Array(e.target.result);
            
            // التأكد من تحميل المكتبة
            if(typeof XLSX === 'undefined') {
                alert('المكتبة قيد التحميل، يرجى الانتظار ثواني قليلة والمحاولة مرة أخرى.');
                return;
            }
            
            // قراءة الإكسل
            var workbook = XLSX.read(data, {type: 'array'});
            var firstSheet = workbook.SheetNames[0];
            
            // تحويل ورقة العمل إلى مصفوفة بيانات (Array of Arrays)
            excelData = XLSX.utils.sheet_to_json(workbook.Sheets[firstSheet], {header: 1}); 
            
            var studentsCount = excelData.length > 1 ? excelData.length - 1 : 0;
            document.getElementById('rased-status').innerText = '✅ جاهز! تم قراءة ' + studentsCount + ' طالب.';
            document.getElementById('rased-status').style.color = '#43A047';
            
            // تفعيل زر الرصد
            var btnFill = document.getElementById('btn-fill');
            btnFill.disabled = false;
            btnFill.style.opacity = '1';
        };
        reader.readAsArrayBuffer(file);
    };

    // عند الضغط على زر "ابدأ الرصد" (هنا يكمن السحر)
    document.getElementById('btn-fill').onclick = function() {
        if(excelData.length === 0) return;
        
        /* ===========================================================
         ملاحظة هامة للجراح: هذا السطر يبحث عن حقول الدرجات في البوابة.
         إذا لم يعمل، يجب تغيير كلمة "input[type='text']" لتطابق كود البوابة الحقيقي.
         ===========================================================
        */
        // البحث عن جميع مربعات إدخال الدرجات في الصفحة الحالية
        var inputs = document.querySelectorAll('input[type="text"]'); 
        
        if(inputs.length === 0) {
            alert('⚠️ لم يتم العثور على حقول رصد الدرجات! تأكد أنك فتحت شاشة إدخال الدرجات.');
            return;
        }

        let successCount = 0;
        
        // حلقة التكرار: تجاوز الصف الأول في الإكسل (لأنه عناوين) ونبدأ من 1
        for(let i = 1; i < excelData.length; i++) {
            
            // افتراض أن الدرجة موجودة في العمود الأول (العمود A) في الإكسل
            let grade = excelData[i][0]; 
            
            // التأكد من وجود درجة ووجود مربع إدخال يقابلها
            if(grade !== undefined && grade !== null && grade !== "" && inputs[i-1]) {
                
                // كتابة الدرجة في المربع
                inputs[i-1].value = grade;
                
                // أمر برمجي لخداع البوابة بأن المعلم هو من كتب الدرجة بيده (لتفعيل الحفظ التلقائي)
                inputs[i-1].dispatchEvent(new Event('input', { bubbles: true }));
                inputs[i-1].dispatchEvent(new Event('change', { bubbles: true }));
                inputs[i-1].dispatchEvent(new Event('blur', { bubbles: true }));
                
                successCount++;
            }
        }
        
        alert('🎉 السحر اكتمل! تم رصد ' + successCount + ' درجة بنجاح.');
    };
})();
