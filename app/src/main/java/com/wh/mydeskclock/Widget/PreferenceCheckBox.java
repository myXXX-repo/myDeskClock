package com.wh.mydeskclock.Widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.wh.mydeskclock.R;

public class PreferenceCheckBox {

    public static View getInstance(Context context, String TITLE, String KEY) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkbox_preference, null);
        TextView textView = view.findViewById(R.id.tv_title_item_checkbox_preference);
        textView.setText(TITLE);

        CheckBox checkBox = view.findViewById(R.id.cb_item_checkbox_preference);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        return view;
    }
}
