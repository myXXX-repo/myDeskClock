package com.wh.mydeskclock.Panel;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wh.mydeskclock.DataBase.Sticky;
import com.wh.mydeskclock.MainActivity;
import com.wh.mydeskclock.Panel.Sticky.PanelStickyFragment;
import com.wh.mydeskclock.Panel.Sticky.StickyViewModel;
import com.wh.mydeskclock.R;
import com.wh.mydeskclock.Utils;
import com.wh.mydeskclock.Widget.MyDialog;


public class MainPanelFragment extends Fragment {
    private String TAG = "WH_PanelAFragment";
    private MainActivity mParent;
    private StickyViewModel stickyViewModel;

    public MainPanelFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mParent = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_panel, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        stickyViewModel = ViewModelProviders.of(mParent).get(StickyViewModel.class);


        BottomNavigationView bottomNavigationView = mParent.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemReselectedListener(
                new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                        Log.d(TAG, String.valueOf(menuItem.getTitle()));
                        switch (menuItem.getTitle().toString()) {
                            case "Sticky": {
                                showStickyDialog(mParent);
                                break;
                            }
                        }
                    }
                });
        NavController navController = Navigation.findNavController(mParent, R.id.fragment3);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(mParent, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    private void showStickyDialog(Context context) {
        final String dialog_title = "Sticky";
        final String[] menuItems = {"add new Sticky", "delete all", "sync data"};

        androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(context);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Utils.setWindowSystemUiVisibility(mParent);
            }
        });
        dialog.setTitle(dialog_title)
                .setItems(menuItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: {
                                addNewStickyDialog(mParent);
                                break;
                            }
                            case 1: {
                                stickyViewModel.deleteAllStickies();
                                break;
                            }
                            case 2: {
                                Toast.makeText(mParent, "sync data", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                });
        MyDialog myDialog = new MyDialog(dialog);
        myDialog.setFullScreen();
        myDialog.show(mParent.getSupportFragmentManager(), "stickyDialog");

    }

    private void addNewStickyDialog(Context context) {

        View dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_add_sticky, null);
        final EditText title = dialogLayout.findViewById(R.id.et_sticky_title);
        final EditText con = dialogLayout.findViewById(R.id.et_sticky_con);


        androidx.appcompat.app.AlertDialog.Builder addStickyDialog = new androidx.appcompat.app.AlertDialog.Builder(mParent);
        addStickyDialog.setTitle("title");
        addStickyDialog.setView(dialogLayout);
        addStickyDialog.setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Sticky sticky = new Sticky();
                sticky.setStickyCreateTime(Utils.getCurrentTimeDate());
                sticky.setStickyTitle(title.getText().toString());
                sticky.setStickyCon(con.getText().toString());
                stickyViewModel.insertStickies(sticky);
            }
        });
        addStickyDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        MyDialog myDialog = new MyDialog(addStickyDialog);
        myDialog.setFullScreen();
        myDialog.show(mParent.getSupportFragmentManager(), "addSticky");
    }
}
