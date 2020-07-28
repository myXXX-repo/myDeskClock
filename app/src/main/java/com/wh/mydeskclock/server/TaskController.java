package com.wh.mydeskclock.server;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.wh.mydeskclock.BaseApp;
import com.wh.mydeskclock.app.task.Task;
import com.wh.mydeskclock.server.MainServer;
import com.wh.mydeskclock.utils.ApiNode;
import com.wh.mydeskclock.utils.ReturnDataUtils;
import com.yanzhenjie.andserver.annotation.DeleteMapping;
import com.yanzhenjie.andserver.annotation.FormPart;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PathVariable;
import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.util.MediaType;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    String TAG = "WH_" + getClass().getSimpleName();

    public TaskController() {
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/get/{taskId}",
                "http://ip:port/task/get/11",
                "get certain task item by id",
                "GET",
                "taskId int",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/get/all",
                "http://ip:port/task/get/all",
                "",
                "GET",
                "taskId int",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/get/undone",
                "http://ip:port/task/get/all",
                "",
                "GET",
                "taskId int",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/delete/{taskId}",
                "http://ip:port/task/get/all",
                "",
                "GET",
                "taskId int",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/delete/all",
                "http://ip:port/task/get/all",
                "",
                "GET",
                "taskId int",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/add",
                "http://ip:port/task/get/all",
                "",
                "GET",
                "taskId int",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/done/{taskId}",
                "http://ip:port/task/get/all",
                "",
                "GET",
                "taskId int",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/undone/{taskId}",
                "http://ip:port/task/get/all",
                "",
                "GET",
                "taskId int",
                ""
        ));
    }

    /**
     * api 0
     *
     * @path /task/get/{taskId}
     * @describe to get task by id
     * @method GET
     */
    @GetMapping(path = "/get/{taskId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get_task_id(Context context, @PathVariable("taskId") int taskId) {
        Task task = BaseApp.taskRepository.getById(taskId);
        return ReturnDataUtils.successfulJson(task);
    }

    /**
     * api 1
     *
     * @path /task/get/all
     * @describe to get all tasks
     * @method GET
     */
    @GetMapping(path = "/get/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get_task_all(Context context) {
        List<Task> tasks = BaseApp.taskRepository.getAll();
        Log.d(TAG, "get_task_all: " + tasks.size());
        return ReturnDataUtils.successfulJson(tasks);
    }

    /**
     * api 2
     *
     * @path /task/get/undone
     * @describe to get all tasks
     * @method GET
     */
    @GetMapping(path = "/get/undone", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get_task_undone(Context context) {
        List<Task> tasks = BaseApp.taskRepository.getNotDoneAll();
        Log.d(TAG, "get_task_not_: " + tasks.size());
        return ReturnDataUtils.successfulJson(tasks);
    }


    /**
     * api 3
     *
     * @param returnData 1 2
     *                   1 返回全部task数据
     *                   2 返回undone的task
     * @path /task/delete/{taskId}
     * @describe delete task by id
     * @method DELETE
     */
    @DeleteMapping(path = "/delete/{taskId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String delete_task_id(Context context, @PathVariable("taskId") int taskId,
                          @RequestParam(name = "return", defaultValue = "0", required = false) int returnData) {
        BaseApp.taskRepository.delete(new Task(taskId));
        if (returnData == 1) {
            return ReturnDataUtils.successfulJson(BaseApp.taskRepository.getAll());
        } else if (returnData == 2) {
            return ReturnDataUtils.successfulJson(BaseApp.taskRepository.getNotDoneAll());
        }
        return ReturnDataUtils.successfulJson("delete task done with id " + taskId);
    }

    /**
     * api 4
     *
     * @path = /task/delete/all
     * @describe delete all tasks
     * @method DELETE
     */
    @DeleteMapping(path = "/delete/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String delete_task_all(Context context) {
        BaseApp.taskRepository.deleteAll();
        return ReturnDataUtils.successfulJson("delete all task done");
    }

    /**
     * api 5
     *
     * @param returnData 1 2
     *                   1 返回全部task数据
     *                   2 返回undone的task
     * @path = /task/add
     * @describe add task by get
     * @method GET
     */
    @GetMapping(path = "/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String add_task_with_get(
            Context context,
            @RequestParam(name = "task", required = true) String TASK,
            @RequestParam(name = "device", required = false, defaultValue = "default device") String DEVICE,
            @RequestParam(name = "title", required = false, defaultValue = "default title") String TITLE,
            @RequestParam(name = "return", defaultValue = "0", required = false) int returnData) {
        Task task = new Task(TASK, TITLE, DEVICE);
        BaseApp.taskRepository.insert(task);
        if (returnData == 1) {
            return ReturnDataUtils.successfulJson(BaseApp.taskRepository.getAll());
        } else if (returnData == 2) {
            return ReturnDataUtils.successfulJson(BaseApp.taskRepository.getNotDoneAll());
        }
        return ReturnDataUtils.successfulJson("add new task done");
    }

    /**
     * api 6
     *
     * @param returnData 1 2
     *                   1 返回全部task数据
     *                   2 返回undone的task
     * @path /task/done/{taskId}
     * @describe set task done by id
     * @method GET
     */
    @GetMapping(path = "/done/{taskId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String set_task_done_id(Context context,
                            @PathVariable(name = "taskId") int taskId,
                            @RequestParam(name = "return", defaultValue = "0", required = false) int returnData) {
        Task task = BaseApp.taskRepository.getById(taskId);
        task.setReadDone(true);
        BaseApp.taskRepository.update(task);
        if (returnData == 1) {
            return ReturnDataUtils.successfulJson(BaseApp.taskRepository.getAll());
        } else if (returnData == 2) {
            return ReturnDataUtils.successfulJson(BaseApp.taskRepository.getNotDoneAll());
        }
        return ReturnDataUtils.successfulJson("set task done successful " + taskId);
    }

    /**
     * api 7
     *
     * @param returnData 1 2
     *                   1 返回全部task数据
     *                   2 返回undone的数据
     * @path /task/undone/{taskId}
     * @describe set task done by id
     * @method GET
     */
    @GetMapping(path = "/undone/{taskId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String set_task_undone_id(Context context,
                              @PathVariable(name = "taskId") int taskId,
                              @RequestParam(name = "return", defaultValue = "0", required = false) int returnData) {
        Task task = BaseApp.taskRepository.getById(taskId);
        task.setReadDone(false);
        BaseApp.taskRepository.update(task);
        if (returnData == 1) {
            return ReturnDataUtils.successfulJson(BaseApp.taskRepository.getAll());
        } else if (returnData == 2) {
            return ReturnDataUtils.successfulJson(BaseApp.taskRepository.getNotDoneAll());
        }
        return ReturnDataUtils.successfulJson("set task done successful " + taskId);
    }
}