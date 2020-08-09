let toBool = function(bool_str) {
    if (bool_str == 'true') {
        return true;
    } else {
        return false;
    }
}

let init = function() {
    if (localStorage.getItem('api_list_show') == null) {
        localStorage.setItem('api_list_show', false);
    }
    if (localStorage.getItem('task_list_done_show') == null) {
        localStorage.setItem('task_list_done_show', false);
    }
    if (localStorage.getItem('task_default_title') == null) {
        localStorage.setItem('task_default_title', 'default title');
    }
    if (localStorage.getItem('device_name') == null) {
        localStorage.setItem('device_name', 'default device');
    }
    if (localStorage.getItem('task_list_get_undone_only') == null) {
        localStorage.setItem('task_list_get_undone_only', true);
    }
    if (localStorage.getItem('judge_item_con_is_url') == null) {
        localStorage.setItem('judge_item_con_is_url', true);
    }
}

let judge_url = function(strstr) {
    let part = /^(https?:\/\/)?(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()!@:%_\+.~#?&\/\/=]*)/
    return part.test(strstr);
}

let FormatDateTime = function(UnixTime) {
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
}

init();
new Vue({
    el: "#page_main",
    data: {
        apps: {
            task: {
                active: "active",
                showed: true,
                task_title: localStorage.getItem('task_default_title'),
                tasks: [],
                task: '',
                task_item: {
                    id: -1,
                    title: "blank title",
                    con: "blank con",
                    device: "blank device",
                    createTime: "0000-00-00 00:00:00",
                    readDone: false
                },
                task_list_get_undone_only: toBool(localStorage.getItem('task_list_get_undone_only')),
                task_list_done_show: toBool(localStorage.getItem('task_list_done_show')),
            },
            notify: {
                active: "",
                showed: false
            },
            sticky: {
                active: "",
                showed: false,
                stickies: []
            },
            remote_ctrl: {
                active: "",
                showed: false
            },
            settings: {
                active: "",
                showed: false,
                device: localStorage.getItem('device_name'),
                api_list_show: toBool(localStorage.getItem('api_list_show')),
                apis: [],
                judge_url: toBool(localStorage.getItem('judge_item_con_is_url'))
            }
        },
    },
    methods: {
        task_send: function() {
            if (this.apps.task.task == "") {
                alert("task can't be blank");
                return;
            }
            var that = this;
            var task_tmp = this.apps.task.task;
            this.apps.task.task = "";
            axios.get("/task/add", {
                params: {
                    "title": this.apps.task.task_title,
                    "task": task_tmp,
                    "device": this.apps.settings.device
                }
            }).then(function(response) {
                alert("ok");
                that.task_fetch();
            }).catch(function(error) {
                alert(error);
            });
        },
        task_fetch: function() {
            var address = '/task/get/';
            if (this.apps.task.task_list_get_undone_only) {
                address += 'undone';
            } else {
                address += 'all';
            }
            that = this
            axios.get(address)
                .then(function(response) {
                    that.apps.task.tasks = response.data.data;
                });
        },
        task_set_done: function(id) {
            var that = this;
            axios.get("/task/done/" + id)
                .then(function(response) {
                    that.task_fetch();
                });
        },
        task_set_undone: function(id) {
            var that = this;
            axios.get("/task/undone/" + id)
                .then(function(response) {
                    that.task_fetch();
                });
        },
        task_show_detail: function(id) {
            this.apps.task.tasks.forEach(element => {
                if (element.id == id) {
                    this.apps.task.task_item.id = id;
                    this.apps.task.task_item.title = element.title;
                    this.apps.task.task_item.con = element.con;
                    this.apps.task.task_item.device = element.deviceName;
                    this.apps.task.task_item.createTime = element.createTime;
                    this.apps.task.task_item.readDone = element.readDone;
                    $("#task_detail").modal();
                }
                return;
            });
        },
        task_reset_title: function() {
            this.apps.task.task_title = localStorage.getItem("task_default_title");
        },
        sticky_fetch: function() {
            var that = this;
            axios.get('/sticky/get/all').then(function(response) {
                that.apps.sticky.stickies = response.data.data;
            });
        },
        ui_format_time: function(timeMS) {
            return FormatDateTime(timeMS);
        },
        settings_save: function(show) {
            // 保存api list 的配置
            localStorage.setItem('api_list_show', this.apps.settings.api_list_show);
            // 保存task list 的配置
            localStorage.setItem('task_list_done_show', this.apps.task.task_list_done_show);
            // 保存 default done task title
            localStorage.setItem('task_default_title', this.apps.task.task_title);
            // 保存 device name
            localStorage.setItem('device_name', this.apps.settings.device);
            // 保存 是否只获取un done items
            localStorage.setItem('task_list_get_undone_only', this.apps.task.task_list_get_undone_only);

            localStorage.setItem('judge_item_con_is_url', this.apps.settings.judge_url);
            alert("save done");
        },
        settings_api_fetch: function() {
            var that = this;
            axios.get("/api/get").then(function(response) {
                that.apps.settings.apis = response.data.data;
            });
        },
        settings_re_init: function() {
            window.localStorage.clear();
            alert('Set ReInit\nPage Will ReFresh');
            location.reload();
        },
        settings_help: function() {
            var a = "1.提交和获取task条目过程使用的是纯正的ajax技术，因此可以实现无刷新更新数据\n";
            var b = "2.点击Send Task中 ‘title’ 文字，可以重置输入的值\n";
            var c = "3.类比上一条，点击task相应位置，清空task输入框\n";
            var d = "4.为了提高响应速度，减少资源浪费，建议开启 Only Get UnDone Task Items 设置项目\n";
            alert(a + b + c + d);
        },
        switchPanel: function(panelName) {
            if (panelName == "sticky") {
                if (this.apps.sticky.active == "active") {
                    $("#sticky_send").modal();
                }
            }

            var active_status = {
                task: "",
                notify: "",
                sticky: "",
                remote_ctrl: "",
                settings: ""
            }
            active_status[panelName] = "active";
            Object.keys(active_status).forEach(element => {
                this.apps[element].active = active_status[element];
            });
            this.apps[panelName].showed = true;
        },
        utils_judge_url: function(sss) {
            return judge_url(sss);
        },
        util_open_new_window: function(urll) {
            window.open(urll);
        }
    },
    watch: {
        "apps.notify.showed": function(newVal, oldVal) {
            // 更新数据
            if (newVal) {
                console.log(newVal);
            }
        },
        "apps.sticky.showed": function(newVal) {
            if (newVal) {
                this.sticky_fetch();
            }
        },
        "apps.remote_ctrl.showed": function(newVal) {

        },
        "apps.settings.api_list_show": function(newVal) {
            if (newVal && this.apps.settings.apis.length == 0) {
                this.settings_api_fetch();
            }
        }
    },
    mounted() {
        this.task_fetch();
        if (this.apps.settings.api_list_show) {
            this.settings_api_fetch();
        }
    }
})