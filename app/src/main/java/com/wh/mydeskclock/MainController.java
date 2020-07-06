package com.wh.mydeskclock;

import android.content.Context;
import android.util.Log;

import com.wh.mydeskclock.TaskNode.Task;
import com.wh.mydeskclock.TaskNode.TaskRepository;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;

@RestController
public class MainController {
    String TAG= "WH_"+ MainController.class.getSimpleName();


    @GetMapping("/task")
    public String notify_new(
            final Context context,
            @RequestParam(value = "device",defaultValue = "default device",required = false) final String DEVICE,
            @RequestParam(value = "title",defaultValue = "default title",required = false) final String TITLE,
            @RequestParam(value = "task",defaultValue = "blank task") final String TASK){
        Log.d(TAG, "notify_new: new notify");
        TaskRepository taskRepository = new TaskRepository(context);
        taskRepository.insert(new Task(TASK,TITLE,DEVICE));
        return "notify recvd";
    }
}