package com.wh.mydeskclock.app.task;

import android.content.Context;
import android.util.Log;

import com.wh.mydeskclock.utils.ReturnDataUtils;
import com.yanzhenjie.andserver.annotation.DeleteMapping;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PathVariable;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.util.MediaType;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    String TAG= "WH_"+ TaskController.class.getSimpleName();
    private TaskRepository taskRepository;

    /**
     * @path /task/get/{taskId}
     * @describe to get task by id
     * @method GET
     */
    @GetMapping(path = "/get/{taskId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get_task_id(Context context,@PathVariable("taskId") int taskId) {
        if(null == taskRepository){
            taskRepository = new TaskRepository(context);
        }
        Task task = taskRepository.getById(taskId);
        return ReturnDataUtils.successfulJson(task);
    }

    /**
     * @path /task/get/all
     * @describe to get all tasks
     * @method GET
     * */
    @GetMapping(path = "/get/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get_task_all(Context context) {
        if(null == taskRepository){
            taskRepository = new TaskRepository(context);
        }
        List<Task> tasks = taskRepository.getAll();
        Log.d(TAG, "get_task_all: "+tasks.size());
        return ReturnDataUtils.successfulJson(tasks);
    }

    /**
     * @path /task/delete/{taskId}
     * @describe delete task by id
     * @method DELETE
     * */
    @DeleteMapping(path = "/delete/{taskId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String delete_task_id(Context context,@PathVariable("taskId") int taskId) {
        // delete task by id
        if(null == taskRepository){
            taskRepository = new TaskRepository(context);
        }
        taskRepository.delete(new Task(taskId));
        return ReturnDataUtils.successfulJson("delete task done with id " + taskId);
    }

    /**
     * @path = /task/delete/all
     * @describe delete all tasks
     * @method DELETE
     * */
    @DeleteMapping(path = "/delete/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String delete_task_all(Context context) {
        if(null == taskRepository){
            taskRepository = new TaskRepository(context);
        }
        taskRepository.deleteAll();
        return ReturnDataUtils.successfulJson("delete all task done");
    }

    /**
     * @path = /task/add
     * @describe add task by get
     * @method GET
     * */
    @GetMapping(path = "/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String add_task_with_get(
            Context context,
            @RequestParam(name = "task", required = true) String TASK,
            @RequestParam(name = "device", required = false, defaultValue = "default device") String DEVICE,
            @RequestParam(name = "title", required = false, defaultValue = "default title") String TITLE) {
        if(null == taskRepository){
            taskRepository = new TaskRepository(context);
        }
        Task task = new Task(TASK,TITLE,DEVICE);
        taskRepository.insert(task);
        return ReturnDataUtils.successfulJson("add new task done");
    }

    /**
     * @path /task/done/{taskId}
     * @describe set task done by id
     * @method GET
     * */
    @GetMapping(path = "/done/{taskId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String set_task_done_id(Context context,
                            @PathVariable(name="taskId")int taskId){
        if(null == taskRepository){
            taskRepository = new TaskRepository(context);
        }
        Task task = taskRepository.getById(taskId);
        task.setReadDone(true);
        taskRepository.update(task);
        return ReturnDataUtils.successfulJson("set task done successful "+taskId);
    }
}