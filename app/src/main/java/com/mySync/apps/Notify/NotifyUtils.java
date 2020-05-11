package com.mySync.apps.Notify;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.collection.SimpleArrayMap;
import androidx.fragment.app.FragmentManager;

import com.mySync.DialogUtils;
import com.mySync.HttpRequestUtils;
import com.mySync.SharedPreferenceUtils;
import com.mySync.values.HttpErrorCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Response;

public class NotifyUtils {
    private String TAG = "WH_" + NotifyUtils.class.getSimpleName();

    private static boolean ServerIsFine = false;

    private NotifyHandler notifyHandler;
    private SharedPreferenceUtils sharedPreferenceUtils;


    private HttpRequestUtils httpRequestUtils;

    public NotifyUtils(SharedPreferenceUtils sharedPreferenceUtils,
                       NotifyHandler notifyHandler) {
        this.sharedPreferenceUtils = sharedPreferenceUtils;

        this.notifyHandler = notifyHandler;

        httpRequestUtils = new HttpRequestUtils();
    }

    // 获取全部数据
    public void getAllNotifies() {
//        Log.d(TAG, String.valueOf(ServerIsFine));
        new Thread() {
            @Override
            public void run() {
                configHttpRequestUtils("/app/Notify/v1.0/notifies");
                Response response;
                try {
                    response = httpRequestUtils.sendGetRequest();
                    Message message = Message.obtain();
                    if (response.code() == 200) {
                        message.what = NotifyHandler.MSG_GET_ALL_REQUEST_200;
                        String response_body = Objects.requireNonNull(response.body()).string();
                        Objects.requireNonNull(response.body()).close();
                        message.obj = new JSONArray(response_body);
                    } else {
                        message.what = response.code();
                    }
                    notifyHandler.sendMessage(message);
                    Objects.requireNonNull(response.body()).close();
                } catch (IOException | JSONException e) {
                    Message message = Message.obtain();
                    message.what = NotifyHandler.MSG_GET_ALL_REQUEST_BROKEN;
                    notifyHandler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // 删除全部数据
    public void deleteAllNotifies() {
        new Thread() {
            @Override
            public void run() {
                configHttpRequestUtils("/app/Notify/v1.0/notifies");
                Response response;
                try {
                    response = httpRequestUtils.sendDeleteRequest();
                    Message message = Message.obtain();
                    if (response.code() == 200) {
                        message.what = NotifyHandler.MSG_DELETE_ALL_REQUEST_200;
                    } else {
                        message.what = response.code();
                    }
                    notifyHandler.sendMessage(message);
                    Objects.requireNonNull(response.body()).close();
                } catch (IOException e) {
                    Message message = Message.obtain();
                    message.what = NotifyHandler.MSG_DELETE_ALL_REQUEST_BROKEN;
                    notifyHandler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // 发送一条记录
    public void sendOneNotify(final String[] keys, final String[] value) {
        new Thread() {
            @Override
            public void run() {
                configHttpRequestUtils("/app/Notify/v1.0/notifies");
                try {
                    Response response = httpRequestUtils.sendPostRequest(keys, value);
                    Message message = Message.obtain();
                    if (response.code() == 200) {
                        message.what = NotifyHandler.MSG_SEND_NOTIFY_REQUEST_200;
                    } else {
                        message.what = response.code();
                    }
                    notifyHandler.sendMessage(message);
                    Objects.requireNonNull(response.body()).close();
                } catch (IOException e) {
                    Message message = Message.obtain();
                    message.what = NotifyHandler.MSG_SEND_NOTIFY_REQUEST_BROKEN;
                    notifyHandler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // 发送多条记录
    public void sendNotifies(final JSONArray jsonArray) {
        new Thread() {
            @Override
            public void run() {
                configHttpRequestUtils("/app/Notify/v1.0/notifies");
                try {
                    Response response = httpRequestUtils.sendPostRequest(jsonArray.toString());
                    Message message = Message.obtain();
                    if (response.code() == 200) {
                        message.what = NotifyHandler.MSG_SEND_NOTIFY_REQUEST_200;
                    } else {
                        message.what = response.code();
                    }
                    notifyHandler.sendMessage(message);
                    Objects.requireNonNull(response.body()).close();
                } catch (IOException e) {
                    Message message = Message.obtain();
                    message.what = NotifyHandler.MSG_SEND_NOTIFY_REQUEST_BROKEN;
                    notifyHandler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // 删除一条记录by id
    public void deleteOneNotify(final int id_id) {
        new Thread() {
            @Override
            public void run() {
                configHttpRequestUtils("/app/Notify/v1.0/notify" + id_id);
                try {
                    Response response = httpRequestUtils.sendDeleteRequest();
                    Message message = Message.obtain();
                    if (response.code() == 200) {
                        message.what = NotifyHandler.MSG_DELETE_ONE_NOTIFY_REQUEST_200;
                    } else {
                        message.what = response.code();
                    }
                    notifyHandler.sendMessage(message);
                    Objects.requireNonNull(response.body()).close();
                } catch (IOException e) {
                    Message message = Message.obtain();
                    message.what = NotifyHandler.MSG_DELETE_ONE_NOTIFY_REQUEST_BROKEN;
                    notifyHandler.sendMessage(message);
                    e.printStackTrace();
                }

            }
        }.start();
    }

    // 测试连接

    public void testConnectToNotify() {
        new Thread() {
            @Override
            public void run() {
                configHttpRequestUtils("/app/Notify/test");
                try {
                    Response response = httpRequestUtils.sendGetRequest();
                    Message message = Message.obtain();
                    if (response.code() == 200) {
                        message.what = NotifyHandler.MSG_TEST_CONNECTION_REQUEST_200;
                    } else {
                        message.what = response.code();
                    }
                    notifyHandler.sendMessage(message);
                    Objects.requireNonNull(response.body()).close();
                } catch (IOException e) {
                    Message message = Message.obtain();
                    message.what = NotifyHandler.MSG_TEST_CONNECTION_REQUEST_BROKEN;
                    notifyHandler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void configHttpRequestUtils(String routePath){
        httpRequestUtils.setMySyncServerAddress(sharedPreferenceUtils.getFullServerAddress() + routePath);
        httpRequestUtils.setMySyncAccessTokenKey(sharedPreferenceUtils.getSetting_access_token_key());
        httpRequestUtils.setMySyncAccessTokenValue(sharedPreferenceUtils.getSetting_access_token_value());
    }

    public static class NotifyHandler extends Handler {
        private final String TAG = "WH_" + NotifyHandler.class.getSimpleName();

        private final static int MSG_GET_ALL_REQUEST_200 = 1;
        private final static int MSG_GET_ALL_REQUEST_BROKEN = 11;

        private final static int MSG_DELETE_ALL_REQUEST_200 = 2;
        private final static int MSG_DELETE_ALL_REQUEST_BROKEN = 22;

        private final static int MSG_DELETE_ONE_NOTIFY_REQUEST_200 = 5;
        private final static int MSG_DELETE_ONE_NOTIFY_REQUEST_BROKEN = 55;


        private final static int MSG_SEND_NOTIFY_REQUEST_200 = 3;
        private final static int MSG_SEND_NOTIFY_REQUEST_BROKEN = 33;

        private final static int MSG_TEST_CONNECTION_REQUEST_200 = 4;
        private final static int MSG_TEST_CONNECTION_REQUEST_BROKEN = 44;

        private static SimpleArrayMap<Integer, String> MSG_HTTP_REQUEST_ERROR = new SimpleArrayMap<>();


        private Context mParent;
        private ClipboardManager clipboardManager;
        private FragmentManager fragmentManager;
        private boolean DialogAutoExit;

        public NotifyHandler(Context mParent, ClipboardManager clipboardManager, FragmentManager fragmentManager,
                             boolean dialogAutoExit) {
            this.mParent = mParent;
            this.clipboardManager = clipboardManager;
            this.fragmentManager = fragmentManager;
            this.DialogAutoExit = dialogAutoExit;

            MSG_HTTP_REQUEST_ERROR = HttpErrorCode.getHttpRequestErrorSet();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_GET_ALL_REQUEST_200: {
                    JSONArray jsonArray = (JSONArray) msg.obj;
                    final String[] conList = new String[jsonArray.length()];
                    final String[] conList_short = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            String con_text = jsonObject.get("con").toString();
                            conList[i] = con_text;
                            conList_short[i] = (con_text.length() > 50) ? (con_text.substring(0, 46) + " ...") : con_text;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    showListDialog(conList, conList_short);
                    break;
                }
                case MSG_GET_ALL_REQUEST_BROKEN: {
                    Toast.makeText(mParent, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                }

                case MSG_DELETE_ALL_REQUEST_200: {
                    Toast.makeText(mParent, "删除全部记录成功", Toast.LENGTH_SHORT).show();
                    break;
                }
                case MSG_DELETE_ALL_REQUEST_BROKEN: {
                    Toast.makeText(mParent, "删除全部记录时连接服务器失败", Toast.LENGTH_SHORT).show();
                    break;
                }

                case MSG_SEND_NOTIFY_REQUEST_200: {
                    Toast.makeText(mParent, "上传成功", Toast.LENGTH_SHORT).show();
                    break;
                }
                case MSG_SEND_NOTIFY_REQUEST_BROKEN: {
                    Toast.makeText(mParent, "上传失败", Toast.LENGTH_SHORT).show();
                    break;
                }

                case MSG_TEST_CONNECTION_REQUEST_200: {
                    NotifyUtils.ServerIsFine = true;
                    Toast.makeText(mParent, "连接成功", Toast.LENGTH_SHORT).show();
                    break;
                }
                case MSG_TEST_CONNECTION_REQUEST_BROKEN: {
                    NotifyUtils.ServerIsFine = false;
                    Toast.makeText(mParent, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                }
                default: {
                    Toast.makeText(mParent, MSG_HTTP_REQUEST_ERROR.get(msg.what), Toast.LENGTH_SHORT).show();
                }
            }
        }

        private void showListDialog(final String[] conList, String[] conList_short) {
            AlertDialog.Builder listAlterDialog = new AlertDialog.Builder(mParent);
            listAlterDialog.setItems(conList_short, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, conList[which]);
                    if (clipboardManager == null) {
                        Toast.makeText(mParent, "剪切板服务初始化失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ClipData clipData = ClipData.newPlainText(TAG, conList[which]);
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(mParent, "文本已复制", Toast.LENGTH_SHORT).show();
                }
            });
            listAlterDialog.setTitle("Notify");
            listAlterDialog.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            DialogUtils dialogUtils = new DialogUtils(listAlterDialog);
            dialogUtils.setGRAVITY(Gravity.BOTTOM);
            if (DialogAutoExit) {
                dialogUtils.setAutoExitParent();

            }
            dialogUtils.setHideFromRecentScreen();
            dialogUtils.show(fragmentManager, "show");
        }
    }
}
