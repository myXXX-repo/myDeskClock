package com.wh.mydeskclock.Widget;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wh.mydeskclock.R;

public class Preference {
    public static View getInstance(final AppCompatActivity mParent, String TITLE, String KEY, final MyDialog myDialog, final String dialogTag) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(mParent).inflate(R.layout.item_preference, null);
        TextView textView = view.findViewById(R.id.tv_title_item_preference);
        textView.setText(TITLE);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.show(mParent.getSupportFragmentManager(), dialogTag);
            }
        });

        return view;
    }

    public static View getInstance(final AppCompatActivity mParent, String TITLE, String KEY) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(mParent).inflate(R.layout.item_preference, null);
        TextView textView = view.findViewById(R.id.tv_title_item_preference);
        textView.setText(TITLE);

        return view;
    }
}
