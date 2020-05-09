package com.wh.mydeskclock.Panel;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wh.mydeskclock.MainActivity;
import com.wh.mydeskclock.R;

public class PanelMoreFragment extends Fragment {
    private MainActivity mParent;

    public PanelMoreFragment() {
    }

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
        linearLayout.addView(addCheckBox(mParent, "自动切换暗色背景", "4"));
        linearLayout.addView(addCheckBox(mParent, "接收媒体信息", "3"));
        linearLayout.addView(addCheckBox(mParent, "自动恢复屏幕方向", "2"));
        linearLayout.addView(addCheckBox(mParent, "自动恢复屏幕亮度", "1"));
    }

    private View addCheckBox(Context context, String TITLE, String KEY) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkbox_preference, null);
        TextView textView = view.findViewById(R.id.tv_title_item_checkbox_preference);
        textView.setText(TITLE);

        CheckBox checkBox = view.findViewById(R.id.cb_item_checkbox_preference);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 将数据保存到preference中
            }
        });

        return view;
    }
}
