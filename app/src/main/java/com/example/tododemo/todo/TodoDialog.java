package com.example.tododemo.todo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.tododemo.R;
import com.example.tododemo.bean.TodoBean;
import com.example.tododemo.dialog.BaseDialog;
import com.example.tododemo.sqlite.DateUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoDialog extends BaseDialog {

    private final Context context;
    private final FragmentManager fragmentManager;
    private long select_time = 0L;
    private MaterialButton mb_add;
    private MaterialToolbar mtb_add;
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
        mtb_add = findViewById(R.id.mtb_add);
        //初始化日期选择器
        initDatePicker();
    }
    // 编辑--获取该item的bean参数
    public void initSetDate(TodoBean bean){
        mtb_add.setTitle("修改代办");
        // 取值-设入
        ti_et_title.setText(bean.getTitle());
        // 设置焦点位置
        ti_et_title.setSelection(bean.getTitle().length());
        ti_et_classify.setText(bean.getClassify());
        // 因为data为2022-9-13的String格式，需转化为Date
        ti_et_time.setText(bean.getDate());
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        //设置MaterialDatePicker初始选中日期
        builder.setSelection(DateUtils.stringToLong(bean.getDate()));
        //设置MaterialDatePicker模式
        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
        //初始化日期选择器->选择今天
        picker = builder.build();
        mb_add.setText("修改");
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
