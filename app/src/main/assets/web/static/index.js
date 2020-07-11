var task = new Vue({
    el: '#send_task',
    data: {
        title: "",
        task: "",
        device: ""
    },
    methods: {
        send: function () {
            if (this.task == "") {
                alert("task can't be blank");
                return;
            }
            axios.get("/task/add", {
                params: {
                    "title": this.title,
                    "task": this.task,
                    "device": this.device
                }
            }).then(function (response) {
                alert("ok");
                location.reload();
            }).catch(function (error) {
                alert(error);
            });
        }
    }
});

var task_list = new Vue({
    el: '#task_list',
    data: {
        tasks: []
    },
    methods: {
        fetch: function () {
            that = this
            axios.get("/task/get/all")
                .then(function (response) {
                    that.tasks = response.data.data;
                });
        },
        set_done: function(e){
            var id = e.currentTarget.parentElement.parentElement.firstElementChild.innerHTML;
            console.log(id);
            axios.get("/task/done/"+id)
            .then(function(response){
                location.reload();
            });
        }
    },
    created() {
        this.fetch()
    }
});

        var api_list = new Vue({
        el: '#api_list',
        data:{
            show:false,
            apis:[
                {
                    app:"task",
                    path:"/task/get/all",
                    method:"GET",
                    describe:"get all tasks",
                    sample:"http://ip:port/task/get/all"
                },
                {
                    app:"task",
                    path:"/task/get/{taskId}",
                    method:"GET",
                    describe:"get task by id",
                    sample:"http://ip:port/task/get/1"
                },
                {
                    app:"task",
                    path:"/task/delete/all",
                    method:"DELETE",
                    describe:"clear task list",
                    sample:"http://ip:port/task/delete/all"
                },
                {
                    app:"task",
                    path:"/task/delete/{taskId}",
                    method:"DELETE",
                    describe:"delete task by id",
                    sample:"http://ip:port/task/delete/2"
                },
                {
                    app:"task",
                    path:"/task/add",
                    method:"GET",
                    describe:"add new task item",
                    sample:'http://ip:port/task/add?task="newTask"&title="title",device="phone"'
                },
                {
                    app:"task",
                    path:"/task/done/{taskId}",
                    method:"GET",
                    describe:"set task done by id",
                    sample:'http://ip:port/task/done/15'
                }
            ]
        }
    });