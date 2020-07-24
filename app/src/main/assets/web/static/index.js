let toBool = function(bool_str){
    if(bool_str == 'true'){
        return true;
    }else{
        return false;
    }
}

let init = function() {
    if (localStorage.getItem('inited') != 1) {
        localStorage.setItem('inited', 1);
        localStorage.setItem('api_list_show', false);
        localStorage.setItem('task_list_done_show', false);
        localStorage.setItem('task_default_title', 'default title');
        localStorage.setItem('device_name', 'default device');
    }
}

init();

var body_main = new Vue({
    el: '#body_main',
    data: {
        task_title: localStorage.getItem('task_default_title'),
        task: '',
        device: localStorage.getItem('device_name'),

        task_con_for_show: 'blank',

        task_list_done_show: toBool(localStorage.getItem('task_list_done_show')),
        tasks: [],

        api_list_show: toBool(localStorage.getItem('api_list_show')),
        apis: [
        {
                app: "task",
                path: "/task/get/all",
                method: "GET",
                describe: "get all tasks",
                sample: "http://ip:port/task/get/all"
            },
            {
                app: "task",
                path: "/task/get/{taskId}",
                method: "GET",
                describe: "get task by id",
                sample: "http://ip:port/task/get/1"
            },
            {
                app: "task",
                path: "/task/delete/all",
                method: "DELETE",
                describe: "clear task list",
                sample: "http://ip:port/task/delete/all"
            },
            {
                app: "task",
                path: "/task/delete/{taskId}",
                method: "DELETE",
                describe: "delete task by id",
                sample: "http://ip:port/task/delete/2"
            },
            {
                app: "task",
                path: "/task/add",
                method: "GET",
                describe: "add new task item",
                sample: 'http://ip:port/task/add?task=newTask&title=title,device=phone'
            },
            {
                app: "task",
                path: "/task/done/{taskId}",
                method: "GET",
                describe: "set task done by id",
                sample: 'http://ip:port/task/done/15'
            },
            {
                app: "notify",
                path: "/notify",
                method: "GET",
                describe: "add notify (notify required)",
                sample: "http://ip:port/notify?notify=newNotify&title=title&device=devices"
            }
        ]
    },
    methods: {
        send: function() {
            if (this.task == "") {
                alert("task can't be blank");
                return;
            }
            axios.get("/task/add", {
                params: {
                    "title": this.task_title,
                    "task": this.task,
                    "device": this.device
                }
            }).then(function(response) {
                alert("ok");
                location.reload();
            }).catch(function(error) {
                alert(error);
            });
        },
        fetch: function() {
            that = this
            axios.get("/task/get/all")
                .then(function(response) {
                    that.tasks = response.data.data;
                });
        },
        set_done: function(id) {
            var that = this;
            axios.get("/task/done/" + id)
                .then(function(response) {
                    that.fetch();
                });
        },
        set_undone: function(id) {
            var that = this;
            axios.get("/task/undone/" + id)
                .then(function(response) {
                    that.fetch();
                });
        },
        FormatDateTime: function(UnixTime) {
            var date = new Date(parseInt(UnixTime));
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            m = m < 10 ? ('0' + m) : m;
            var d = date.getDate();
            d = d < 10 ? ('0' + d) : d;
            var h = date.getHours();
            h = h < 10 ? ('0' + h) : h;
            var minute = date.getMinutes();
            var second = date.getSeconds();
            minute = minute < 10 ? ('0' + minute) : minute;
            second = second < 10 ? ('0' + second) : second;
            return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
        },
        save: function(){
            // 保存api list 的配置
            localStorage.setItem('api_list_show',this.api_list_show);
            // 保存task list 的配置
            localStorage.setItem('task_list_done_show',this.task_list_done_show);
            // 保存 default done task title
            localStorage.setItem('task_default_title',this.task_title);
            // 保存 device name
            localStorage.setItem('device_name',this.device);
        },
        openSetting: function(){
            $("#settingDialogPanel").modal();
        },
        show_task_detail: function(con){
            this.task_con_for_show = con;
            $("#task_detail").modal();
        }
    },
    created() {
        this.fetch()
    }
});