package com.wh.mydeskclock.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;


/**
 * @describe 下载工具 调用系统下载管理器 下载到公共Download目录
 * @args addr 下载链接
 *       fileName 保存的文件名称
 *       mine 文件类型
 * */
public class DownloadUtils {
    public static long download(String addr, String fileName, String mine,Context context) {
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
        return downloadManager.enqueue(request);
    }

    /**
     * @describe 下载工具 调用系统下载管理器 下载到公共Download目录
     * @args addr 下载链接
     *       mine 文件类型
     *       本方法自动从url拿文件名
     * */
    public static long download(String addr,String mime, Context context) {
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
        return downloadManager.enqueue(request);
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
