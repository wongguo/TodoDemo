package com.example.tododemo.todo;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.tododemo.dialog.BaseDialog;
import com.example.tododemo.R;
import com.example.tododemo.sqlite.CRUD;
import com.example.tododemo.sqlite.Constant;
import com.example.tododemo.sqlite.DateUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoDialog extends BaseDialog {

    private final Context context;
    private final FragmentManager fragmentManager;
    private long select_time = 0L;
    private MaterialButton mb_add;
    private TextInputEditText ti_et_title;
    private TextInputEditText ti_et_classify;
    private TextInputEditText ti_et_time;
    private MaterialDatePicker<Long> picker;
    private ButtonOnClickListener buttonOnClickListener;

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
        select_time=MaterialDatePicker.todayInUtcMilliseconds();
        ti_et_time.setText(DateUtils.longToDate(select_time));
        picker = builder.build();


    }

    @Override
    protected void initListener() {
        ti_et_time.setOnClickListener(view -> {
            picker.show(fragmentManager,picker.toString());
            //监听日期选中后
            picker.addOnPositiveButtonClickListener(selection -> {
                select_time=selection;
                ti_et_time.setText(DateUtils.longToDate(select_time));
            });
        });


        mb_add.setOnClickListener(view -> {
            if(buttonOnClickListener!=null){
                String title=ti_et_title.getText().toString();
                String classify=ti_et_classify.getText().toString();
                buttonOnClickListener.addTodoClick(title,classify,select_time);
            }
        });
    }


    public interface ButtonOnClickListener{
        void addTodoClick(String title,String classify,long time);
    }

    public void setButtonOnClickListener(ButtonOnClickListener buttonOnClickListener) {
        this.buttonOnClickListener = buttonOnClickListener;
    }

}
