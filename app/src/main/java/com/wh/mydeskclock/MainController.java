package com.wh.mydeskclock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wh.mydeskclock.TaskNode.Task;
import com.wh.mydeskclock.TaskNode.TaskRepository;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;

@RestController
public class MainController {
    String TAG= "WH_"+ MainController.class.getSimpleName();

    @GetMapping("/")
    public String index(){
        return "/notify device= title= notify=\n" +
                "/task device= title= task=\n";
    }


    @GetMapping("/task")
    public String task_new(
            final Context context,
            @RequestParam(value = "device",defaultValue = "default device",required = false) final String DEVICE,
            @RequestParam(value = "title",defaultValue = "default title",required = false) final String TITLE,
            @RequestParam(value = "task",defaultValue = "blank task") final String TASK){
        Log.d(TAG, "task_new: new task");
        TaskRepository taskRepository = new TaskRepository(context);
        taskRepository.insert(new Task(TASK,TITLE,DEVICE));
        return "recvd";
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
        return "recvd";
    }
}