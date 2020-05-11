package com.mySync.apps.ApplicationData;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.collection.SimpleArrayMap;
import androidx.fragment.app.FragmentManager;

import com.mySync.HttpRequestUtils;
import com.mySync.SharedPreferenceUtils;
import com.mySync.values.HttpErrorCode;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Response;

public class ApplicationDataUtils {
    private String TAG = "WH_" + ApplicationDataUtils.class.getSimpleName();

    private ApplicationDataHandler applicationDataHandler;
    private SharedPreferenceUtils sharedPreferenceUtils;

    private HttpRequestUtils httpRequestUtils;

    public ApplicationDataUtils(SharedPreferenceUtils sharedPreferenceUtils,
                                ApplicationDataHandler applicationDataHandler) {
        this.sharedPreferenceUtils = sharedPreferenceUtils;
        this.applicationDataHandler = applicationDataHandler;
        httpRequestUtils = new HttpRequestUtils();
    }

    public void getAllAppData() {
        Log.d(TAG,"in get function");
        new Thread() {
            @Override
            public void run() {
                Log.d(TAG,"in get thread run");
                configHttpRequestUtils("/app/ApplicationData/v1.0/data_all");
                Response response;
                try {
                    response = httpRequestUtils.sendGetRequest();
                    Message message = Message.obtain();
                    Log.d(TAG, String.valueOf(response.code()));
                    if (response.code() == 200) {
//                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                        JSONArray jsonArray = new JSONArray(Objects.requireNonNull(response.body()).string());
                        Objects.requireNonNull(response.body()).close();
                        message.what = ApplicationDataHandler.MSG_GET_ALL_REQUEST_200;
                        message.obj = jsonArray;
                    } else {
                        message.what = response.code();
                    }
                    applicationDataHandler.sendMessage(message);
                } catch (IOException | JSONException e) {
                    Message message = Message.obtain();
                    message.what = ApplicationDataHandler.MSG_GET_ALL_REQUEST_BROKEN;
                    applicationDataHandler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void configHttpRequestUtils(String routePath) {
        httpRequestUtils.setMySyncServerAddress(sharedPreferenceUtils.getFullServerAddress() + routePath);
        httpRequestUtils.setMySyncAccessTokenKey(sharedPreferenceUtils.getSetting_access_token_key());
        httpRequestUtils.setMySyncAccessTokenValue(sharedPreferenceUtils.getSetting_access_token_value());
    }


    public static class ApplicationDataHandler extends Handler {

        private String TAG = "WH_"+ApplicationDataHandler.class.getSimpleName();

        private final static int MSG_GET_ALL_REQUEST_200 = 1;
        private final static int MSG_GET_ALL_REQUEST_BROKEN = 11;

        private static SimpleArrayMap<Integer, String> MSG_HTTP_REQUEST_ERROR;

        private Context mParent;
        private ClipboardManager clipboardManager;
        private FragmentManager fragmentManager;
        private boolean DialogAutoExit;

        public ApplicationDataHandler(Context mParent, ClipboardManager clipboardManager, FragmentManager fragmentManager) {
            this.mParent = mParent;
            this.clipboardManager = clipboardManager;
            this.fragmentManager = fragmentManager;

            MSG_HTTP_REQUEST_ERROR = HttpErrorCode.getHttpRequestErrorSet();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_GET_ALL_REQUEST_200: {
                    Toast.makeText(mParent, "获取成功", Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray = (JSONArray) msg.obj;

                    Log.d(TAG, "get data length"+ jsonArray.length());

                    for(int i=0;i<jsonArray.length();i++){
                        try {
                            Log.d(TAG,jsonArray.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                default: {
                    Toast.makeText(mParent, MSG_HTTP_REQUEST_ERROR.get(msg.what), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
