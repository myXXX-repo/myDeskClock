package com.wh.mydeskclock.Panel.More;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mySync.SharedPreferenceUtils;
import com.wh.mydeskclock.MainActivity;
import com.wh.mydeskclock.R;
import com.wh.mydeskclock.Widget.MyDialog;
import com.wh.mydeskclock.Widget.Preference;

public class PanelMoreFragment extends Fragment {
    private MainActivity mParent;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mParent = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_panel_more, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view == null) {
            return;
        }
        LinearLayout linearLayout = getView().findViewById(R.id.ll_more_con);
        linearLayout.addView(addCheckBox(mParent, "自动切换暗色背景", "4",false));
        linearLayout.addView(addCheckBox(mParent, "接收媒体信息", "3",false));
        linearLayout.addView(addCheckBox(mParent, "自动恢复屏幕方向", "2",false));
        linearLayout.addView(addCheckBox(mParent, "自动恢复屏幕亮度", "1",false));
        linearLayout.addView(addCheckBox(mParent, "每天检查新版本", "1",true));
        linearLayout.addView(Preference.getInstance(mParent, "横屏方向", "", genScreenORSettingDialog(), "screen_or_setting_dialog"));
    }

    private View addCheckBox(final Context context, String TITLE, String KEY, boolean Enabled) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkbox_preference, null);
        TextView textView = view.findViewById(R.id.tv_title_item_checkbox_preference);
        if(!Enabled){
            textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        textView.setText(TITLE);

        CheckBox checkBox = view.findViewById(R.id.cb_item_checkbox_preference);
        checkBox.setEnabled(Enabled);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(context);
            }
        });

        return view;
    }

    private MyDialog genScreenORSettingDialog() {
        AlertDialog.Builder screenORSettingDialog = new AlertDialog.Builder(mParent)
                .setItems(new String[]{"向左为正", "向右为正"}, null)
                .setTitle("设置横屏方向");
        MyDialog myDialog = new MyDialog(screenORSettingDialog);
        myDialog.setFullScreen();
        return myDialog;
    }
}
