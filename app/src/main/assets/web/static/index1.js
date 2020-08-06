let toBool = function(bool_str) {
    if (bool_str == 'true') {
        return true;
    } else {
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
        localStorage.setItem('task_list_get_undone_only', true);
    }
}

init();
new Vue({
    el: "#page_main",
    data: {
        panels: {
            task: {
                active: "active",
                inited: true
            },
            notify: {
                active: "",
                inited: false
            },
            sticky: {
                active: "",
                inited: false
            },
            remote_ctrl: {
                active: "",
                inited: false
            },
            settings: {
                active: "",
                inited: false
            }
        },
        task_title: localStorage.getItem('task_default_title'),
        device: localStorage.getItem('device_name'),

        tasks: [],
        task: '',
        task_item_for_show: {
            id: -1,
            title: "blank title",
            con: "blank con",
            device: "blank device",
            createTime: "0000-00-00 00:00:00",
            readDone: false
        },
        task_list_get_undone_only: toBool(localStorage.getItem('task_list_get_undone_only')),
        task_list_done_show: toBool(localStorage.getItem('task_list_done_show')),

        api_list_show: toBool(localStorage.getItem('api_list_show')),
        apis: [],

        stickies: []
    },
    methods: {
        send: function() {
            if (this.task == "") {
                alert("task can't be blank");
                return;
            }
            var that = this;
            var task_tmp = this.task;
            this.task = "";
            axios.get("/task/add", {
                params: {
                    "title": this.task_title,
                    "task": task_tmp,
                    "device": this.device
                }
            }).then(function(response) {
                alert("ok");
                that.fetch();
            }).catch(function(error) {
                alert(error);
            });
        },
        fetch: function() {
            var address = '/task/get/';
            if (this.task_list_get_undone_only) {
                address += 'undone';
            } else {
                address += 'all';
            }
            that = this
            axios.get(address)
                .then(function(response) {
                    that.tasks = response.data.data;
                });
            axios.get('/sticky/get/all').then(function(response) {
                that.stickies = response.data.data;
            })
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
        save: function(show) {
            // 保存api list 的配置
            localStorage.setItem('api_list_show', this.api_list_show);
            // 保存task list 的配置
            localStorage.setItem('task_list_done_show', this.task_list_done_show);
            // 保存 default done task title
            localStorage.setItem('task_default_title', this.task_title);
            // 保存 device name
            localStorage.setItem('device_name', this.device);
            // 保存 是否只获取un done items
            localStorage.setItem('task_list_get_undone_only', this.task_list_get_undone_only);
            if (show == "1") {
                alert("save done");
            }
        },
        openSetting: function() {
            $("#settingDialogPanel").modal();
        },
        show_task_detail: function(id) {
            this.tasks.forEach(element => {
                if (element.id == id) {
                    this.task_item_for_show.id = id;
                    this.task_item_for_show.title = element.title;
                    this.task_item_for_show.con = element.con;
                    this.task_item_for_show.device = element.deviceName;
                    this.task_item_for_show.createTime = element.createTime;
                    this.task_item_for_show.readDone = element.readDone;
                }
            });

            $("#task_detail").modal();
        },
        setUnInit: function() {
            localStorage.setItem('inited', 0);
            alert('Set UnInit Done\nPage Will ReFresh');
            location.reload();
        },
        resetTitle: function() {
            this.task_title = localStorage.getItem("task_default_title");
        },
        help: function() {
            var a = "1.提交和获取task条目过程使用的是纯正的ajax技术，因此可以实现无刷新更新数据\n";
            var b = "2.点击Send Task中 ‘title’ 文字，可以重置输入的值\n";
            var c = "3.类比上一条，点击task相应位置，清空task输入框\n";
            var d = "4.为了提高响应速度，减少资源浪费，建议开启 Only Get UnDone Task Items 设置项目\n";
            alert(a + b + c + d);
        },
        switchPanel: function(panelName) {
            if (panelName == "sticky") {
                if (this.panels.sticky.active == "active") {
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
                this.panels[element].active = active_status[element];
            });
        },
    },
    created() {
        this.fetch();
        var that = this;
        axios.get("/api/get").then(function(response) {
            that.apis = response.data.data;
        });
    }
})