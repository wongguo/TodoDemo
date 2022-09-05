package com.example.tododemo.todo;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.tododemo.Dialog.BaseDialog;
import com.example.tododemo.R;
import com.example.tododemo.SQLite.CRUD;
import com.example.tododemo.SQLite.Constant;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoDialog extends BaseDialog {

    private final Context context;
    private final FragmentManager fragmentManager;
    private String select_time = null;
    private MaterialButton mb_add;
    private TextInputEditText ti_et_title;
    private TextInputEditText ti_et_classify;
    private TextInputEditText ti_et_time;
    private MaterialDatePicker<Long> picker;

    public TodoDialog(@NonNull Context context, FragmentManager fragmentManager) {
        super(context);
        this.context=context;
        this.fragmentManager=fragmentManager;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_add;
    }

    @Override
    protected void initView() {
        ti_et_title = findViewById(R.id.ti_et_title);
        ti_et_classify = findViewById(R.id.ti_et_classify);
        ti_et_time = findViewById(R.id.ti_et_time);
        mb_add = findViewById(R.id.mb_add);
        //初始化日期选择器
        initDatePicker();
    }

    private void initDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        //设置MaterialDatePicker初始选中今天
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        //设置MaterialDatePicker模式
        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
        //初始化日期选择器->选择今天
        select_time=longToDate(MaterialDatePicker.todayInUtcMilliseconds());
        ti_et_time.setText(select_time);
        picker = builder.build();
    }

    @Override
    protected void initListener() {
        ti_et_time.setOnClickListener(view -> {
            picker.show(fragmentManager,picker.toString());
            //监听日期选中后
            picker.addOnPositiveButtonClickListener(selection -> {
                select_time=longToDate(selection);
                ti_et_time.setText(select_time);
            });
        });


        mb_add.setOnClickListener(view -> {
            String title=ti_et_title.getText().toString();
            String classify=ti_et_classify.getText().toString();
            if(title.isEmpty()){
                Toast.makeText(context, "待办事项不为空", Toast.LENGTH_SHORT).show();
            }else {
                if (classify.isEmpty()) classify="默认";
                CRUD crud=new CRUD(context, Constant.TODO_TABLE_NAME);
                ContentValues values=new ContentValues();
                values.put("username",Constant.username);
                values.put("title",title);
                values.put("classify",classify);
                values.put("date",select_time);
                crud.add(values);
                values.clear();
                Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    public static String longToDate(long lo){
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return sd.format(date);
    }
}
