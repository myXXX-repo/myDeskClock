package com.wh.mydeskclock.Panel;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wh.mydeskclock.MainActivity;
import com.wh.mydeskclock.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PanelMoreFragment extends PreferenceFragmentCompat {
    private MainActivity mParent;

    public PanelMoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mParent = (MainActivity) context;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pre_more, rootKey);
    }
}
