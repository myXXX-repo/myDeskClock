package com.wh.mydeskclock;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.wh.mydeskclock.app.mediaCtrl.MediaCtrlFragment;
import com.wh.mydeskclock.app.notify.NotifyFragment;
import com.wh.mydeskclock.app.task.Task;
import com.wh.mydeskclock.app.task.TaskListFragment;
import com.wh.mydeskclock.server.MainServer;
import com.wh.mydeskclock.utils.DownloadUtils;
import com.wh.mydeskclock.utils.FileUtils;
import com.wh.mydeskclock.utils.NetUtils;
import com.wh.mydeskclock.utils.QRCodeGenerator;
import com.wh.mydeskclock.utils.SharedPreferenceUtils;
import com.wh.mydeskclock.utils.UiUtils;
import com.wh.mydeskclock.utils.Utils;
import com.wh.mydeskclock.widget.MyDialog;


import java.io.File;
import java.util.Objects;


public class MainFragmentLand extends BaseFragment implements View.OnClickListener, View.OnLongClickListener {
    private String TAG = "WH_"+getClass().getSimpleName();

    private TextView tv_address;
    private TextView tv_week;
    private TimeBatteryBroadcastReceiver timeBatteryBroadcastReceiver;
    private FrameLayout fl_media_ctrl;
    private boolean SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER;
    private boolean SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL;
    private int SETTING_HTTP_SERVER_PORT = 0;
    private boolean SETTING_UI_SHOW_SERVER_ADDRESS;
    private boolean SETTING_TASK_HIDE_DONE;
    private FragmentManager fragmentManager;

    public static AppCompatActivity mParent;

    private static boolean SETTING_UI_AUTO_FLASH_SCREEN;

    private String ServerAddress;

    private static long download_id = 0L;

    public MainFragmentLand() {
        super.setTAG("WH_" + getClass().getSimpleName());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mParent = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_land, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        SETTING_HTTP_SERVER_PORT = Integer.parseInt(Objects.requireNonNull(BaseApp.sp_default.getString(SharedPreferenceUtils.sp_default.SETTING_HTTP_SERVER_PORT, "8081")));
        SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER = BaseApp.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER, true);
        SETTING_UI_SHOW_SERVER_ADDRESS = BaseApp.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_UI_SHOW_SERVER_ADDRESS, true);
        SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL = BaseApp.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL, true);
        SETTING_UI_AUTO_FLASH_SCREEN = BaseApp.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_UI_AUTO_FLASH_SCREEN, false);
        if (SETTING_TASK_HIDE_DONE != BaseApp.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_TASK_HIDE_DONE, true)) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_task, new TaskListFragment()).commit();
        }
        if (SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL) {
            fl_media_ctrl.setVisibility(View.VISIBLE);
        } else {
            fl_media_ctrl.setVisibility(View.GONE);
        }
        ServerAddress = "http:/" + NetUtils.getLocalIPAddress() + ":" + SETTING_HTTP_SERVER_PORT;
        if (SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER) {
            requireContext().startService(new Intent(requireContext(), MainService.class));
            if (SETTING_UI_SHOW_SERVER_ADDRESS) {
                tv_address.setText(ServerAddress);
                tv_address.setVisibility(View.VISIBLE);
            } else {
                tv_address.setVisibility(View.GONE);
            }
        } else {
            tv_address.setVisibility(View.GONE);
            Intent intent = new Intent();
            intent.setAction("myDeskClock_server_exit");
            requireContext().sendBroadcast(intent);
        }
        MainServer.access_token = BaseApp.sp_default.getString(SharedPreferenceUtils.sp_default.SETTING_HTTP_SERVER_ACCESS_TOKEN,"0");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timeBatteryBroadcastReceiver != null) {
            requireContext().unregisterReceiver(timeBatteryBroadcastReceiver);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hour: {
                MainActivityLand.flash();
                break;
            }
            case R.id.tv_min: {
                MyDialog myDialog = new MyDialog(
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Operations")
                                .setItems(new String[]{"打开背光", "关闭背光","创建Task"}, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        switch (i) {
                                            case 0: {
                                                UiUtils.setWindowBrightness(requireActivity(), 1);
                                                break;
                                            }
                                            case 1: {
                                                UiUtils.setWindowBrightness(requireActivity(), 0);
                                                break;
                                            }
                                            case 2:{
                                                View v = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_task,null,false);
                                                final EditText title = v.findViewById(R.id.tv_new_task_title);
                                                final EditText task = v.findViewById(R.id.tv_new_task_con);

                                                MyDialog myDialog1 = new MyDialog(
                                                        new AlertDialog.Builder(requireContext())
                                                        .setTitle("New Task Item")
                                                        .setView(v)
                                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                String new_title = title.getText().toString();
                                                                String new_task = task.getText().toString();
                                                                BaseApp.taskRepository.insert(new Task(new_task,new_title,"localhost"));
                                                            }
                                                        })
                                                        .setNegativeButton("cancel",null)
                                                );
                                                myDialog1.setFullScreen();
                                                myDialog1.show(requireActivity().getSupportFragmentManager(),"add_new_task");
                                                break;
                                            }
                                        }
                                    }
                                }));
                myDialog.setFullScreen();
                myDialog.show(requireActivity().getSupportFragmentManager(), "mydc_dialog_test");
                break;
            }
            case R.id.tv_address: {
                ImageView iv_address_qrcode = new ImageView(requireContext());
                iv_address_qrcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ServerAddress));
                        startActivity(intent);
                    }
                });
                QRCodeGenerator qrCodeGenerator = new QRCodeGenerator("http:/" + NetUtils.getLocalIPAddress() + ":" + SETTING_HTTP_SERVER_PORT, 500, 500);
                iv_address_qrcode.setImageBitmap(qrCodeGenerator.getQRCode());
                MyDialog myDialog = new MyDialog(new AlertDialog.Builder(requireContext())
                        .setView(iv_address_qrcode));
                myDialog.setFullScreen();
                myDialog.show(requireActivity().getSupportFragmentManager(), "address_qrcode_dialog");
                break;
            }
            case R.id.tv_battery: {
                // 点击battery文本显示弹框显示
                MyDialog myDialog = new MyDialog(new AlertDialog.Builder(requireContext())
                        .setTitle("Battery Cost Detail")
                        .setMessage("null")
                        .setPositiveButton("Close", null));
                myDialog.setFullScreen();
                myDialog.show(requireActivity().getSupportFragmentManager(), "battery_coast_detail");
                break;
            }
            case R.id.tv_week: {
                // 点击week文本从服务器下载新版本
                if(!BaseApp.isDebug){
                    return;
                }
                if (ContextCompat.checkSelfPermission(mParent, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mParent.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                } else {
                    downloadNewVersion();
                }
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.tv_min) {
            startActivity(new Intent(requireActivity(), SettingActivity.class));
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tv_week.callOnClick();
            }
        }
    }

    private void init() {

        SETTING_HTTP_SERVER_PORT = Integer.parseInt(Objects.requireNonNull(BaseApp.sp_default.getString(SharedPreferenceUtils.sp_default.SETTING_HTTP_SERVER_PORT, "8081")));
        SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER = BaseApp.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER, true);
        SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL = BaseApp.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL, true);
        SETTING_UI_SHOW_SERVER_ADDRESS = BaseApp.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_UI_SHOW_SERVER_ADDRESS, true);
        SETTING_TASK_HIDE_DONE = BaseApp.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_TASK_HIDE_DONE, true);
        SETTING_UI_AUTO_FLASH_SCREEN = BaseApp.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_UI_AUTO_FLASH_SCREEN, false);

        ServerAddress = "http:/" + NetUtils.getLocalIPAddress() + ":" + SETTING_HTTP_SERVER_PORT;
        if (SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER) {
            requireContext().startService(new Intent(requireContext(), MainService.class));
        }

        TextView tv_hour = requireActivity().findViewById(R.id.tv_hour);
        TextView tv_min = requireActivity().findViewById(R.id.tv_min);
        tv_week = requireActivity().findViewById(R.id.tv_week);
        TextView tv_date = requireActivity().findViewById(R.id.tv_date);
        tv_address = requireActivity().findViewById(R.id.tv_address);
        TextView tv_battery = requireActivity().findViewById(R.id.tv_battery);

        if (!SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER) {
            tv_address.setVisibility(View.GONE);
        } else {
            if (SETTING_UI_SHOW_SERVER_ADDRESS) {
                tv_address.setVisibility(View.VISIBLE);
            } else {
                tv_address.setVisibility(View.GONE);
            }
        }

        tv_hour.setOnClickListener(this);
        tv_min.setOnClickListener(this);
        tv_week.setOnClickListener(this);
        tv_address.setOnClickListener(this);
        tv_battery.setOnClickListener(this);

        tv_min.setOnLongClickListener(this);

        tv_address.setText(ServerAddress);

        NotifyFragment notifyFragment = new NotifyFragment();
        MediaCtrlFragment mediaCtrlFragment = new MediaCtrlFragment();
        TaskListFragment taskListFragment = new TaskListFragment();

        fl_media_ctrl = requireActivity().findViewById(R.id.fl_media_ctrl);

        if (!SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL) {
            fl_media_ctrl.setVisibility(View.GONE);
        }


        fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_notify, notifyFragment);
        fragmentTransaction.replace(R.id.fl_media_ctrl, mediaCtrlFragment);
        fragmentTransaction.replace(R.id.fl_task, taskListFragment);
        fragmentTransaction.commit();


        timeBatteryBroadcastReceiver = new TimeBatteryBroadcastReceiver(new MyHandler(
                tv_hour,
                tv_min,
                tv_week,
                tv_date,
                tv_battery));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        requireContext().registerReceiver(timeBatteryBroadcastReceiver, intentFilter);

        UiUtils.setTime_MainFragment(
                tv_hour,
                tv_min,
                tv_week,
                tv_date);
        UiUtils.setBattery_MainFragment(tv_battery);
    }


    static class MyHandler extends Handler {
        private static final int WHAT_TIME = 338;
        private static final int WHAT_BATTERY = 33;
        TextView tv_hour;
        TextView tv_min;
        TextView tv_week;
        TextView tv_date;
        TextView tv_battery;

        public MyHandler(
                TextView tv_hour,
                TextView tv_min,
                TextView tv_week,
                TextView tv_date,
                TextView tv_battery) {
            this.tv_hour = tv_hour;
            this.tv_min = tv_min;
            this.tv_week = tv_week;
            this.tv_date = tv_date;
            this.tv_battery = tv_battery;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case WHAT_TIME: {
                    UiUtils.setTime_MainFragment(
                            tv_hour,
                            tv_min,
                            tv_week,
                            tv_date);
                    break;
                }
                case WHAT_BATTERY: {
                    UiUtils.setBattery_MainFragment(tv_battery);
                    break;
                }
            }
        }
    }

    private static class TimeBatteryBroadcastReceiver extends BroadcastReceiver {
        MyHandler myHandler;
        int FlashDistanceTime = 100;

        public TimeBatteryBroadcastReceiver(MyHandler myHandler) {
            this.myHandler = myHandler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String ACTION = intent.getAction();
            if (ACTION == null) {
                return;
            }
            switch (ACTION) {
                case Intent.ACTION_TIME_TICK: {
                    myHandler.sendEmptyMessage(MyHandler.WHAT_TIME);
                    if (SETTING_UI_AUTO_FLASH_SCREEN) {
                        FlashDistanceTime--;
                        if (FlashDistanceTime == 0) {
                            FlashDistanceTime = 100;
                            MainActivityLand.flash();
                        }
                    }
                    break;
                }
                case Intent.ACTION_BATTERY_CHANGED: {
                    myHandler.sendEmptyMessage(MyHandler.WHAT_BATTERY);
                    break;
                }
                case DownloadManager.ACTION_DOWNLOAD_COMPLETE:{
                    Log.d("WH_D", "onReceive: download complete");

//                    if(download_id ==intent.getExtras().getLong(DownloadManager.EXTRA_DOWNLOAD_ID,-1L)){
//                        File a = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"./myDC.apk");
//                        Intent intent1 = new Intent(Intent.ACTION_VIEW,);
//                    }

                    Utils.unPackBundle(intent.getExtras(),"WH_D");

                    // install file
                    break;
                }
            }
        }
    }

    private void downloadNewVersion() {
        File a = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/myDC.apk");
        if(a.exists()){
            if(a.isFile()){
                a.delete();
            }
        }


        // 将文件下载到download目录
        MyDialog myDialog = new MyDialog(
                new AlertDialog.Builder(requireContext())
                        .setMessage("StartDownload?")
                        .setPositiveButton("download", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                download_id = DownloadUtils.download("http://192.168.50.184/release/myDeskClock/app-release.apk","mydc.apk",FileUtils.MimeType.APPLICATION.APK,requireContext());
                                MyDialog myDialog1 = new MyDialog(new AlertDialog.Builder(requireContext())
                                        .setPositiveButton("fileManager", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) { 
                                                PackageManager packageManager = requireContext().getPackageManager();
                                                Intent it= packageManager.getLaunchIntentForPackage("bin.mt.plus");
                                                requireContext().startActivity(it);
                                            }
                                        }));
                                myDialog1.setFullScreen();
                                myDialog1.show(fragmentManager,"");
                            }
                        }).setNegativeButton("fileManager", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PackageManager packageManager = requireContext().getPackageManager();
                        Intent it= packageManager.getLaunchIntentForPackage("bin.mt.plus");
                        requireContext().startActivity(it);
                    }
                }));
        myDialog.setFullScreen();
        myDialog.show(fragmentManager, "");



    }
}
