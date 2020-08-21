package com.wh.mydeskclock.app.task;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.wh.mydeskclock.BaseApp;
import com.wh.mydeskclock.server.MainServer;
import com.wh.mydeskclock.utils.ApiNode;
import com.wh.mydeskclock.utils.ReturnDataUtils;
import com.yanzhenjie.andserver.annotation.DeleteMapping;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PathVariable;
import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.RequestBody;
import com.yanzhenjie.andserver.annotation.RequestHeader;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.util.MediaType;

import java.util.ArrayList;
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
                "获取指定id的task",
                "GET",
                "taskId int类型",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/get/all",
                "http://ip:port/task/get/all",
                "用来获取全部task",
                "GET",
                "taskId int类型",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/get/undone",
                "http://ip:port/task/get/undone",
                "用来获取全部未完成的task",
                "GET",
                "taskId int类型",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/delete/{taskId}",
                "http://ip:port/task/delete/11",
                "用来删除指定id的task",
                "DELETE",
                "taskId int类型",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/delete/all",
                "http://ip:port/task/delete/all",
                "用来删除全部task",
                "DELETE",
                "",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/add",
                "http://ip:port/task/add",
                "用来创建新的task",
                "GET",
                "",
                "task,device,title 均为string类型"
        ));
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/add/multi",
                "http://ip:port/task/add/multi",
                "用来创建新的task 上传多条",
                "POST",
                "",
                "con,device 均为string类型,其中con是包含title和con的json数组"
        ));
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/done/{taskId}",
                "http://ip:port/task/done/11",
                "将指定id的task设置为完成",
                "GET",
                "taskId int类型",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "task",
                "/task/undone/{taskId}",
                "http://ip:port/task/undone/11",
                "将指定id的task设置未未完成",
                "GET",
                "taskId int类型",
                ""
        ));
    }

    /**
     *
     * @path /task/get/{taskId}
     * @describe to get task by id
     * @method GET
     */
    @GetMapping(path = "/get/{taskId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get_task_id(Context context,
                       @PathVariable("taskId") int taskId,
                       @RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        Task task = BaseApp.taskRepository.getById(taskId);
        return ReturnDataUtils.successfulJson(task);
    }

    /**
     *
     * @path /task/get/all
     * @describe to get all tasks
     * @method GET
     */
    @GetMapping(path = "/get/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get_task_all(@RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        List<Task> tasks = BaseApp.taskRepository.getAll();
        Log.d(TAG, "get_task_all: " + tasks.size());
        return ReturnDataUtils.successfulJson(tasks);
    }

    /**
     *
     * @path /task/get/undone
     * @describe to get all tasks
     * @method GET
     */
    @GetMapping(path = "/get/undone", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get_task_undone(@RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        List<Task> tasks = BaseApp.taskRepository.getNotDoneAll();
        Log.d(TAG, "get_task_not_: " + tasks.size());
        return ReturnDataUtils.successfulJson(tasks);
    }


    /**
     *
     * @param returnData 1 2
     *                   1 返回全部task数据
     *                   2 返回undone的task
     * @path /task/delete/{taskId}
     * @describe delete task by id
     * @method DELETE
     */
    @DeleteMapping(path = "/delete/{taskId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String delete_task_id(@PathVariable("taskId") int taskId,
                          @RequestParam(name = "return", defaultValue = "0", required = false) int returnData,
                          @RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        BaseApp.taskRepository.delete(new Task(taskId));
        if (returnData == 1) {
            return ReturnDataUtils.successfulJson(BaseApp.taskRepository.getAll());
        } else if (returnData == 2) {
            return ReturnDataUtils.successfulJson(BaseApp.taskRepository.getNotDoneAll());
        }
        return ReturnDataUtils.successfulJson("delete task done with id " + taskId);
    }

    /**
     *
     * @path = /task/delete/all
     * @describe delete all tasks
     * @method DELETE
     */
    @DeleteMapping(path = "/delete/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String delete_task_all(@RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        BaseApp.taskRepository.deleteAll();
        return ReturnDataUtils.successfulJson("delete all task done");
    }

    /**
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
            @RequestParam(name = "task") String TASK,
            @RequestParam(name = "device", required = false, defaultValue = "default device") String DEVICE,
            @RequestParam(name = "title", required = false, defaultValue = "default title") String TITLE,
            @RequestParam(name = "return", defaultValue = "0", required = false) int returnData,
            @RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
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
     *
     * @path = /task/add/multi
     * @describe add tasks by post
     * @method POST
     * @requestParam con String Json
     * @requestParam device String
     * @requestParam access_token String
     *
     */
    @PostMapping(path = "/add/multi", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String add_task_with_post_json(
            @RequestParam(name = "con") String JSON_CON,
            @RequestParam(name = "device", required = false, defaultValue = "default device") String DEVICE,
            @RequestParam(name = "access_token", required = false) String ACCESS_TOKEN
    ) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        List<Task.TaskSmall>taskSmalls = JSON.parseArray(JSON_CON,Task.TaskSmall.class);
        Task[] tasks = new Task[taskSmalls.size()];
        for(int i=0;i<taskSmalls.size();i++){
            Task.TaskSmall tmp = taskSmalls.get(i);
            Log.d(TAG, "add_task_with_post_json: "+ tmp.getTitle()+" "+tmp.getCon());
            tasks[i]=new Task(tmp.getCon(),tmp.getTitle(),DEVICE);
        }
        BaseApp.taskRepository.insert(tasks);
        return ReturnDataUtils.successfulJson("task list recved");
    }

    /**
     *
     * @param returnData 1 2
     *                   1 返回全部task数据
     *                   2 返回undone的task
     * @path /task/done/{taskId}
     * @describe set task done by id
     * @method GET
     */
    @GetMapping(path = "/done/{taskId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String set_task_done_id(
            @PathVariable(name = "taskId") int taskId,
            @RequestParam(name = "return", defaultValue = "0", required = false) int returnData,
            @RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
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
     *
     * @param returnData 1 2
     *                   1 返回全部task数据
     *                   2 返回undone的数据
     * @path /task/undone/{taskId}
     * @describe set task done by id
     * @method GET
     */
    @GetMapping(path = "/undone/{taskId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String set_task_undone_id(
            @PathVariable(name = "taskId") int taskId,
            @RequestParam(name = "return", defaultValue = "0", required = false) int returnData,
            @RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
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