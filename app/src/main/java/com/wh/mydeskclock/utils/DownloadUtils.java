package com.wh.mydeskclock.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class DownloadUtils {
    public static void download(String addr, String fileName, String mine,Context context) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(addr));
        //指定下载路径和下载文件名
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);
        request.setMimeType(mine);

        //获取下载管理器
        DownloadManager downloadManager = SystemServiceUtils.getDownloadManager(context);
        //将下载任务加入下载队列，否则不会进行下载
        downloadManager.enqueue(request);
    }

    public static void download(String addr,String mime, Context context) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(addr));
        //指定下载路径和下载文件名
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, addr.substring(addr.lastIndexOf("/") + 1));
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);
        request.setMimeType(mime);
        //获取下载管理器
        DownloadManager downloadManager = SystemServiceUtils.getDownloadManager(context);
        //将下载任务加入下载队列，否则不会进行下载
        downloadManager.enqueue(request);
    }

//    public static void download(String addr, File saveFile,String mime,Context context){
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(addr));
//
//        request.setDestinationUri(Uri.fromFile(saveFile));
//        request.setAllowedNetworkTypes(
//                DownloadManager.Request.NETWORK_MOBILE
//                        | DownloadManager.Request.NETWORK_WIFI);
//        request.setMimeType(mime);
//        DownloadManager downloadManager = SystemServiceUtils.getDownloadManager(context);
//        //将下载任务加入下载队列，否则不会进行下载
//        downloadManager.enqueue(request);
//    }

//    public static void download(String addr, String mime, Context context) {
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(addr));
//        //指定下载路径和下载文件名
//        request.setDestinationInExternalFilesDir(context,context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(),context.getPackageName());
//        request.setAllowedNetworkTypes(
//                DownloadManager.Request.NETWORK_MOBILE
//                        | DownloadManager.Request.NETWORK_WIFI);
//        request.setMimeType(mime);
//        //获取下载管理器
//        DownloadManager downloadManager = SystemServiceUtils.getDownloadManager(context);
//        //将下载任务加入下载队列，否则不会进行下载
//        downloadManager.enqueue(request);
//    }

}
