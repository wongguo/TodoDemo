package com.example.tododemo.todo;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.tododemo.Dialog.BaseDialog;
import com.example.tododemo.R;
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
    private MaterialButton mb_add;
    private TextInputEditText ti_et_title;
    private TextInputEditText ti_et_classify;
    private TextInputEditText ti_et_time;
    private TextInputLayout til_title;

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
        til_title = findViewById(R.id.til_title);
        ti_et_title = findViewById(R.id.ti_et_title);
        ti_et_classify = findViewById(R.id.ti_et_classify);
        ti_et_time = findViewById(R.id.ti_et_time);
        mb_add = findViewById(R.id.mb_add);
    }

    @Override
    protected void initListener() {
        ti_et_time.setOnClickListener(view -> {
            MaterialDatePicker.Builder<Long> builder=MaterialDatePicker.Builder.datePicker();
            builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
            builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
            MaterialDatePicker<Long> picker=builder.build();
            picker.show(fragmentManager,picker.toString());
            picker.addOnPositiveButtonClickListener(selection -> ti_et_time.setText(longToDate(selection)));
        });


        mb_add.setOnClickListener(view -> {
            String title=ti_et_title.getText().toString();
            String classify=ti_et_classify.getText().toString();
            if(title.isEmpty()){
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String longToDate(long lo){
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return sd.format(date);
    }
}
