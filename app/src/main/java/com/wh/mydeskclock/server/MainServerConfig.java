package com.wh.mydeskclock.server;

import android.content.Context;

import com.yanzhenjie.andserver.annotation.Config;

import com.yanzhenjie.andserver.framework.config.Multipart;
import com.yanzhenjie.andserver.framework.config.WebConfig;
import com.yanzhenjie.andserver.framework.website.AssetsWebsite;

import java.io.File;

@Config
public class MainServerConfig implements WebConfig {
    @Override
    public void onConfig(Context context, Delegate delegate) {
        delegate.addWebsite(new AssetsWebsite(context,"/web"));
        delegate.setMultipart(Multipart.newBuilder()
                .allFileMaxSize(1024 * 1024 * 20)
                .fileMaxSize(1024 * 1024 * 5)
                .maxInMemorySize(1024 * 10)
                .uploadTempDir(new File(context.getCacheDir(), "_server_upload_cache_"))
                .build());
    }
}
