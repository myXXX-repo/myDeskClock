package com.wh.mydeskclock;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.wh.mydeskclock.utils.UiUtils;

import java.util.Objects;

public class MyDialog extends DialogFragment {
    private boolean DIALOG_FULL_SCREEN;
    private int DIALOG_GRAVITY = Gravity.CENTER;
    private androidx.appcompat.app.AlertDialog.Builder dialog;
    private View view;

    public MyDialog() {

    }

    public MyDialog(androidx.appcompat.app.AlertDialog.Builder dialog) {
        this.dialog = dialog;
    }
    public MyDialog(androidx.appcompat.app.AlertDialog.Builder dialog,View view) {
        this.view = view;
        this.dialog = dialog;
    }

    public void setFullScreen() {
        DIALOG_FULL_SCREEN = true;
    }

    public void setGRAVITY(int GRAVITY) {
        DIALOG_GRAVITY = GRAVITY;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (DIALOG_FULL_SCREEN) {
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        setCancelable(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 设定dialog中显示的数据
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        if (window == null) {
            return;
        }

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = DIALOG_GRAVITY;
        window.setAttributes(layoutParams);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        UiUtils.setFullScreen(requireActivity().getWindow());
        if (dialog == null) {
            dialog = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
            dialog.setTitle("Dialog标题");
            dialog.setMessage("你好，这是一条信息，点击按钮进行相应操作");
            dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getContext(), "YES pressed", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getContext(), "No pressed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(view!=null){
            dialog.setView(view);
        }
        return dialog.create();
    }
}
