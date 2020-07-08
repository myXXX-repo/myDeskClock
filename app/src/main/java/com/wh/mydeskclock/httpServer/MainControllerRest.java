package com.wh.mydeskclock.httpServer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wh.mydeskclock.TaskNode.Task;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;

@RestController
public class MainControllerRest {
    String TAG= "WH_"+ MainControllerRest.class.getSimpleName();

    @GetMapping("/notify_script")
    public String download_py_script(){
        return "forward:notify.py";
    }


    @GetMapping("/task")
    public String task_new(
            final Context context,
            @RequestParam(value = "device",defaultValue = "default device",required = false) final String DEVICE,
            @RequestParam(value = "title",defaultValue = "default title",required = false) final String TITLE,
            @RequestParam(value = "task",defaultValue = "blank task") final String TASK){
        Log.d(TAG, "task_new: new task");
        Task task = new Task(TASK,TITLE,DEVICE);

        Bundle bundle = new Bundle();
        bundle.putString("DEVICE",DEVICE);
        bundle.putString("TITLE",TITLE);
        bundle.putString("TASK",TASK);

        Intent intent = new Intent();
        intent.putExtra("extra",bundle);
        intent.setAction("sendTask");

        context.sendBroadcast(intent);
        return "forward:recved.html";
    }

    @GetMapping("/notify")
    public String notify_new(
            final Context context,
            @RequestParam(value = "device",defaultValue = "default device",required = false) final String DEVICE,
            @RequestParam(value = "title",defaultValue = "default title",required = false) final String TITLE,
            @RequestParam(value = "notify",defaultValue = "blank notify") final String NOTIFY){
        Log.d(TAG, "notify_new: "+NOTIFY);
        Bundle bundle = new Bundle();
        bundle.putString("DEVICE",DEVICE);
        bundle.putString("TITLE",TITLE);
        bundle.putString("NOTIFY",NOTIFY);
        Intent intent = new Intent();
        intent.setAction("showNotify");
        intent.putExtra("extra",bundle);
        context.sendBroadcast(intent);
        return "forward:recved.html";
    }
}